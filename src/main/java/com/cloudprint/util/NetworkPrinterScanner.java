package com.cloudprint.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

// SNMP相关导入
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * 网络打印机扫描器，用于发现局域网中的打印机设备
 */
@Slf4j
public class NetworkPrinterScanner {

    /**
     * 搜索到的网络打印机
     */
    @Data
    public static class NetworkPrinter {
        private String name;       // 打印机名称
        private String ipAddress;  // IP地址
        private int port;          // 端口号
        private String model;      // 设备型号（如果能获取到）
        private String type;       // 设备类型

        public NetworkPrinter(String ipAddress) {
            this.ipAddress = ipAddress;
            this.name = "未命名打印机";
            this.port = 9100; // 默认打印机端口
            this.model = "未知型号";
            this.type = "打印机";
        }

        @Override
        public String toString() {
            return name + " (" + ipAddress + ":" + port + ")";
        }
    }

    private ExecutorService executorService;
    private final int timeout = 1000; // 扫描超时时间（毫秒）
    private final int threadPoolSize = 50; // 线程池大小

    /**
     * 扫描局域网中的打印机
     * @return 搜索到的打印机列表
     */
    public List<NetworkPrinter> scanLocalNetworkPrinters() {
        List<NetworkPrinter> printers = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(threadPoolSize);
        List<Future<NetworkPrinter>> futures = new ArrayList<>();

        try {
            // 获取本地IP地址和子网掩码
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
            List<InterfaceAddress> interfaceAddresses = networkInterface.getInterfaceAddresses();

            for (InterfaceAddress address : interfaceAddresses) {
                InetAddress inetAddress = address.getAddress();
                if (inetAddress instanceof Inet4Address) {
                    // 生成同一子网下的所有IP地址
                    List<String> subnetIps = generateSubnetIps(inetAddress, address.getNetworkPrefixLength());
                    for (String ip : subnetIps) {
                        // 提交扫描任务
                        futures.add(executorService.submit(() -> scanSingleIp(ip)));
                    }
                }
            }

            // 处理扫描结果
            for (Future<NetworkPrinter> future : futures) {
                try {
                    NetworkPrinter printer = future.get(timeout, TimeUnit.MILLISECONDS);
                    log.info("扫描到打印机: {}", printer);
                    if (printer != null) {
                        printers.add(printer);
                    }
                } catch (TimeoutException | ExecutionException | InterruptedException e) {
                    // 忽略超时或执行异常的IP
                }
            }
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }

        return printers;
    }

    /**
     * 生成子网内的所有IP地址
     * @param inetAddress 本地IP地址
     * @param prefixLength 子网前缀长度
     * @return 子网内的IP地址列表
     */
    private List<String> generateSubnetIps(InetAddress inetAddress, short prefixLength) {
        List<String> ips = new ArrayList<>();
        String ipStr = inetAddress.getHostAddress();
        String[] octets = ipStr.split("\\.");
        long ip = (Long.parseLong(octets[0]) << 24) + 
                  (Long.parseLong(octets[1]) << 16) + 
                  (Long.parseLong(octets[2]) << 8) + 
                  Long.parseLong(octets[3]);

        long mask = -1L << (32 - prefixLength);
        long network = ip & mask;
        long broadcast = network | (~mask);

        // 跳过网络地址和广播地址
        for (long i = network + 1; i < broadcast; i++) {
            String ipAddress = String.format("%d.%d.%d.%d", 
                    (i >> 24) & 0xFF, 
                    (i >> 16) & 0xFF, 
                    (i >> 8) & 0xFF, 
                    i & 0xFF);
            ips.add(ipAddress);
        }

        return ips;
    }

    /**
     * 扫描单个IP地址，检测是否为打印机
     * @param ipAddress IP地址
     * @return 如果是打印机，返回NetworkPrinter对象；否则返回null
     */
    private NetworkPrinter scanSingleIp(String ipAddress) {
        try {
            // 尝试连接到打印机默认端口（9100）
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ipAddress, 9100), timeout);
            socket.close();
            
            // 尝试获取打印机名称、型号和类型
            NetworkPrinter printer = new NetworkPrinter(ipAddress);
            printer.setName(detectPrinterName(ipAddress));
            printer.setModel(detectPrinterModel(ipAddress));
            printer.setType(detectPrinterType(ipAddress));
            
            return printer;
        } catch (IOException e) {
            // 连接失败，不是打印机或未开启打印机服务
            return null;
        }
    }

    /**
     * 使用SNMP查询打印机信息
     * @param ipAddress IP地址
     * @param oid SNMP OID
     * @return 查询结果
     */
    private String snmpQuery(String ipAddress, String oid) {
        try {
            // 创建SNMP对象
            TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
            Snmp snmp = new Snmp(transport);
            transport.listen();
            
            // 设置目标设备
            CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString("public")); // 默认社区名
            target.setAddress(new UdpAddress(ipAddress + "/161")); // SNMP默认端口161
            target.setRetries(1);
            target.setTimeout(500);
            target.setVersion(SnmpConstants.version2c);
            
            // 创建PDU，查询指定OID
            PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID(oid)));
            pdu.setType(PDU.GET);
            
            // 发送请求
            ResponseEvent responseEvent = snmp.send(pdu, target);
            PDU response = responseEvent.getResponse();
            
            // 关闭SNMP
            snmp.close();
            
            if (response != null && response.getErrorStatus() == PDU.noError) {
                VariableBinding binding = response.get(0);
                return binding.getVariable().toString();
            }
        } catch (Exception e) {
            log.debug("SNMP查询失败，IP: {}, OID: {}, 错误: {}", ipAddress, oid, e.getMessage());
        }
        return null;
    }

    /**
     * 尝试检测打印机名称
     * @param ipAddress IP地址
     * @return 打印机名称
     */
    private String detectPrinterName(String ipAddress) {
        // 使用SNMP查询打印机名称 (sysName OID: 1.3.6.1.2.1.1.5.0)
        String name = snmpQuery(ipAddress, "1.3.6.1.2.1.1.5.0");
        if (name != null && !name.isEmpty()) {
            return name;
        }
        // 如果SNMP查询失败，使用默认名称
        return "打印机 " + ipAddress;
    }

    /**
     * 尝试检测打印机型号
     * @param ipAddress IP地址
     * @return 打印机型号
     */
    private String detectPrinterModel(String ipAddress) {
        // 使用SNMP查询打印机型号 (sysDescr OID: 1.3.6.1.2.1.1.1.0)
        String descr = snmpQuery(ipAddress, "1.3.6.1.2.1.1.1.0");
        if (descr != null && !descr.isEmpty()) {
            // 从描述中提取型号信息
            return extractModelFromDescription(descr);
        }
        return "未知型号";
    }

    /**
     * 尝试检测打印机类型
     * @param ipAddress IP地址
     * @return 打印机类型
     */
    private String detectPrinterType(String ipAddress) {
        // 使用SNMP查询打印机类型 (sysObjectID OID: 1.3.6.1.2.1.1.2.0)
        String oid = snmpQuery(ipAddress, "1.3.6.1.2.1.1.2.0");
        if (oid != null && !oid.isEmpty()) {
            // 根据sysObjectID判断设备类型
            return determinePrinterTypeFromOID(oid);
        }
        return "打印机";
    }

    /**
     * 从设备描述中提取型号信息
     * @param description 设备描述
     * @return 提取的型号
     */
    private String extractModelFromDescription(String description) {
        // 简单实现：返回前50个字符作为型号
        if (description.length() > 50) {
            return description.substring(0, 50) + "...";
        }
        return description;
    }

    /**
     * 根据SNMP OID确定打印机类型
     * @param oid SNMP OID
     * @return 打印机类型
     */
    private String determinePrinterTypeFromOID(String oid) {
        // 根据不同厂商的OID前缀判断打印机类型
        if (oid.contains("1.3.6.1.4.1.1602.1.1")) { // 爱普生打印机
            return "激光打印机";
        } else if (oid.contains("1.3.6.1.4.1.232")) { // HP打印机
            return "激光打印机";
        } else if (oid.contains("1.3.6.1.4.1.11.2.3.9.1")) { // Canon打印机
            return "喷墨打印机";
        } else if (oid.contains("1.3.6.1.4.1.2435.2")) { // 理光打印机
            return "激光打印机";
        } else if (oid.contains("1.3.6.1.4.1.18334.1.1")) { // Brother打印机
            return "激光打印机";
        } else if (oid.contains("1.3.6.1.4.1.24681")) { // 富士施乐打印机
            return "激光打印机";
        } else {
            return "网络打印机";
        }
    }
}
