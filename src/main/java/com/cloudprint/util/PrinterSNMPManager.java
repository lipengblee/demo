package com.cloudprint.util;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.*;

@Slf4j
public class PrinterSNMPManager {
    private static final String DEFAULT_COMMUNITY = "public";
    private static final int DEFAULT_PORT = 161;
    private static final int DEFAULT_TIMEOUT = 3000;
    private static final int DEFAULT_RETRIES = 2;

    private Snmp snmp;
    private String printerIp;

    // 常见打印机OID定义
    public static class PrinterOIDs {
        // 系统信息
        public static final String SYS_DESCR = "1.3.6.1.2.1.1.1.0";    // 系统描述
        public static final String SYS_OBJECT_ID = "1.3.6.1.2.1.1.2.0"; // 对象ID
        public static final String SYS_UP_TIME = "1.3.6.1.2.1.1.3.0";   // 运行时间
        public static final String SYS_CONTACT = "1.3.6.1.2.1.1.4.0";   // 联系人
        public static final String SYS_NAME = "1.3.6.1.2.1.1.5.0";      // 系统名称
        public static final String SYS_LOCATION = "1.3.6.1.2.1.1.6.0";  // 位置

        // 打印机MIB (RFC 3805)
        public static final String PRINTER_STATUS = "1.3.6.1.2.1.43.16.5.1.2.1.1"; // 打印机状态 - 用于RFC标准
        public static final String PRINTER_STATUS_GENERAL = "1.3.6.1.2.1.25.3.5.1.1.1"; // 通用打印机状态
        public static final String PRINTER_DETECTED_ERROR_STATE = "1.3.6.1.2.1.25.3.5.1.2.1"; // 错误状态
        public static final String PRT_LOCALE = "1.3.6.1.2.1.43.5.1.1.6"; // 区域设置

        // 爱普生私有MIB OIDs (部分示例，具体需要查爱普生MIB文档)
        public static final String EPSON_SERIAL_NUMBER = "1.3.6.1.4.1.1248.1.2.2.1.1.1.5.0"; // 序列号
        public static final String EPSON_FIRMWARE_VERSION = "1.3.6.1.4.1.1248.1.2.2.1.1.1.3.0"; // 固件版本
        public static final String EPSON_TOTAL_PAGES = "1.3.6.1.4.1.1248.1.2.2.1.1.1.9.0"; // 总打印页数
        
        // 更多爱普生OID
        public static final String EPSON_DEVICE_ID = "1.3.6.1.4.1.1248.1.1.2.1.1.1.0"; // 设备ID
        public static final String EPSON_MODEL = "1.3.6.1.4.1.1248.1.2.2.2.2.1.1.3.1"; // 型号
        public static final String EPSON_MARKER_SUPPLIES = "1.3.6.1.2.1.43.11.1.1.6.1.1"; // 标记耗材

        // 柯尼卡美能达私有MIB OIDs
        public static final String KONICA_SERIAL_NUMBER = "1.3.6.1.4.1.18334.1.1.1.1.4.0"; // 序列号
        public static final String KONICA_FIRMWARE_VERSION = "1.3.6.1.4.1.18334.1.1.1.1.3.0"; // 固件版本
        public static final String KONICA_TOTAL_PAGES = "1.3.6.1.2.1.43.10.2.1.4.1.1"; // 总打印页数
        
        // 理光(RICOH)私有MIB OIDs
        public static final String RICOH_SERIAL_NUMBER = "1.3.6.1.4.1.367.3.2.1.1.1.11.0"; // 序列号
        public static final String RICOH_FIRMWARE_VERSION = "1.3.6.1.4.1.367.3.2.1.1.1.9.0"; // 固件版本
        public static final String RICOH_TOTAL_PAGES = "1.3.6.1.4.1.367.3.2.1.1.1.6.0"; // 总打印页数
        public static final String RICOH_DEVICE_STATUS = "1.3.6.1.4.1.367.3.2.1.1.1.4.0"; // 设备状态
        
        // EPSON打印机服务器OID
        public static final String EPSON_SERVER_DEVICE_ID = "1.3.6.1.4.1.1248.1.1.3.1.1.7.1"; // 服务器设备ID
        public static final String EPSON_SERVER_SERIAL = "1.3.6.1.4.1.1248.1.1.3.1.1.8.1"; // 服务器序列号

        // 通用打印机MIB OIDs (RFC 3805)
        public static final String GENERAL_SERIAL_NUMBER = "1.3.6.1.2.1.43.5.1.1.17.1"; // 通用序列号
        public static final String GENERAL_FIRMWARE_VERSION = "1.3.6.1.2.1.43.5.1.1.16.1"; // 通用固件版本
        public static final String GENERAL_TOTAL_PAGES = "1.3.6.1.2.1.43.10.2.1.4.1.1"; // 通用总打印页数

        // 耗材信息
        public static final String SUPPLY_DESCRIPTION = "1.3.6.1.2.1.43.11.1.1.6"; // 耗材描述
        public static final String SUPPLY_LEVEL = "1.3.6.1.2.1.43.11.1.1.9"; // 耗材余量
        public static final String SUPPLY_COLOR = "1.3.6.1.2.1.43.11.1.1.4"; // 耗材颜色
        public static final String SUPPLY_TYPE = "1.3.6.1.2.1.43.11.1.1.2"; // 耗材类型

        // 纸张信息
        public static final String PRINTER_MEDIA_DESCRIPTION = "1.3.6.1.2.1.43.8.2.1.2"; // 纸张描述
        public static final String PRINTER_MEDIA_TYPE = "1.3.6.1.2.1.43.8.2.1.3"; // 纸张类型
        public static final String PRINTER_MEDIA_LEVEL = "1.3.6.1.2.1.43.8.2.1.11"; // 纸张余量
        public static final String PRINTER_MEDIA_STATUS = "1.3.6.1.2.1.43.8.2.1.10"; // 纸张状态
        public static final String PRINTER_MEDIA_SIZE = "1.3.6.1.2.1.43.8.2.1.4"; // 纸张尺寸

        // 网络信息
        public static final String IF_DESCR = "1.3.6.1.2.1.2.2.1.2"; // 接口描述
        public static final String IF_IP_ADDRESS = "1.3.6.1.2.1.4.20.1.1"; // IP地址
        public static final String IF_MAC_ADDRESS = "1.3.6.1.2.1.2.2.1.6"; // MAC地址
    }

    public PrinterSNMPManager(String printerIp) throws IOException {
        this.printerIp = printerIp;
        initSNMP();
    }

    private void initSNMP() throws IOException {
        TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        transport.listen();
    }

    /**
     * 获取单个OID的值
     */
    public String getSingleOID(String oid) throws IOException {
        // log.debug("获取OID值: {}，设备IP: {}", oid, printerIp);
        CommunityTarget target = createTarget();
        PDU pdu = new PDU();
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setType(PDU.GET);

        ResponseEvent event = snmp.send(pdu, target);
        if (event != null && event.getResponse() != null) {
            PDU response = event.getResponse();
            if (response.getErrorStatus() == PDU.noError) {
                VariableBinding vb = response.get(0);
                String value = vb.getVariable().toString();
                // log.debug("成功获取OID值: {} = {}，设备IP: {}", oid, value, printerIp);
                return value;
            } else {
                log.warn("获取OID值失败，错误状态: {}，OID: {}，设备IP: {}", 
                         response.getErrorStatus(), oid, printerIp);
            }
        } else {
            log.warn("未收到SNMP响应，OID: {}，设备IP: {}", oid, printerIp);
        }
        return null;
    }

    /**
     * 批量获取多个OID的值
     */
    public Map<String, String> getMultipleOIDs(List<String> oids) throws IOException {
        Map<String, String> results = new HashMap<>();
        CommunityTarget target = createTarget();
        PDU pdu = new PDU();

        for (String oid : oids) {
            pdu.add(new VariableBinding(new OID(oid)));
        }
        pdu.setType(PDU.GET);

        ResponseEvent event = snmp.send(pdu, target);
        if (event != null && event.getResponse() != null) {
            PDU response = event.getResponse();
            if (response.getErrorStatus() == PDU.noError) {
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb = response.get(i);
                    results.put(vb.getOid().toString(), vb.getVariable().toString());
                }
            }
        }
        return results;
    }

    /**
     * 获取打印机完整信息
     */
    public PrinterInfo getPrinterInfo() throws IOException {
        log.info("开始获取打印机完整信息，设备IP: {}", printerIp);
        
        // 先获取系统描述，用于判断打印机品牌
        String sysDescr = getSingleOID(PrinterOIDs.SYS_DESCR);
        log.debug("获取到系统描述: {}，设备IP: {}", sysDescr, printerIp);
        
        // 如果无法获取系统描述，说明访问不到打印机，直接返回状态为OFFLINE的信息
        if (sysDescr == null) {
            log.warn("无法访问打印机，设备IP: {}", printerIp);
            PrinterInfo info = new PrinterInfo();
            info.setIpAddress(printerIp);
            info.setStatus(PrinterStatus.OFFLINE);
            info.setModel("Unknown");
            info.setName("Unknown");
            info.setLocation("Unknown");
            info.setLastUpdate(new Date());
            return info;
        }
        
        // 根据系统描述判断打印机品牌并选择相应的OID
        List<String> oids = new ArrayList<>(Arrays.asList(
            PrinterOIDs.SYS_DESCR,
            PrinterOIDs.SYS_NAME,
            PrinterOIDs.SYS_LOCATION,
            PrinterOIDs.PRINTER_STATUS,        // RFC标准状态
            PrinterOIDs.PRINTER_STATUS_GENERAL // 通用状态
        ));
        
        // 添加品牌特定的OID
        if (sysDescr != null && sysDescr.toUpperCase().contains("EPSON")) {
            // 爱普生打印机
            // 检测是否是打印服务器
            boolean isPrintServer = sysDescr.toUpperCase().contains("PRINT SERVER");
            
            if (isPrintServer) {
                // 对于打印服务器，使用服务器OID
                oids.add(PrinterOIDs.EPSON_SERVER_SERIAL);
                oids.add(PrinterOIDs.EPSON_SERVER_DEVICE_ID);
            } else {
                // 对于打印机本身，使用打印机OID
                oids.add(PrinterOIDs.EPSON_SERIAL_NUMBER);
                oids.add(PrinterOIDs.EPSON_FIRMWARE_VERSION);
                oids.add(PrinterOIDs.EPSON_TOTAL_PAGES);
                oids.add(PrinterOIDs.EPSON_DEVICE_ID);
                oids.add(PrinterOIDs.EPSON_MODEL);
            }
        } else if (sysDescr != null && (sysDescr.toUpperCase().contains("KONICA") || 
                   sysDescr.toUpperCase().contains("MINOLTA"))) {
            // 柯尼卡美能达打印机
            oids.add(PrinterOIDs.KONICA_SERIAL_NUMBER);
            oids.add(PrinterOIDs.KONICA_FIRMWARE_VERSION);
            oids.add(PrinterOIDs.KONICA_TOTAL_PAGES);
        } else if (sysDescr != null && sysDescr.toUpperCase().contains("RICOH")) {
            // 理光打印机
            oids.add(PrinterOIDs.RICOH_SERIAL_NUMBER);
            oids.add(PrinterOIDs.RICOH_FIRMWARE_VERSION);
            oids.add(PrinterOIDs.RICOH_TOTAL_PAGES);
            oids.add(PrinterOIDs.RICOH_DEVICE_STATUS); // 理光特定状态OID
        } else {
            // 使用通用OID
            oids.add(PrinterOIDs.GENERAL_SERIAL_NUMBER);
            oids.add(PrinterOIDs.GENERAL_FIRMWARE_VERSION);
            oids.add(PrinterOIDs.GENERAL_TOTAL_PAGES);
        }

        Map<String, String> snmpData = getMultipleOIDs(oids);
        PrinterInfo info = new PrinterInfo();
        info.setIpAddress(printerIp);
        info.setModel(sysDescr != null ? sysDescr : "Unknown");
        info.setName(snmpData.getOrDefault(PrinterOIDs.SYS_NAME, "Unknown"));
        info.setLocation(snmpData.getOrDefault(PrinterOIDs.SYS_LOCATION, "Unknown"));
        
        // 根据品牌获取序列号
        String serialNumber = null;
        if (sysDescr != null && sysDescr.toUpperCase().contains("EPSON")) {
            serialNumber = snmpData.get(PrinterOIDs.EPSON_SERIAL_NUMBER);
            // 如果EPSON序列号获取失败，尝试设备ID
            if (serialNumber == null || serialNumber.equals("noSuchObject") || serialNumber.equals("noSuchInstance")) {
                serialNumber = snmpData.get(PrinterOIDs.EPSON_DEVICE_ID);
            }
        } else if (sysDescr != null && (sysDescr.toUpperCase().contains("KONICA") || 
                   sysDescr.toUpperCase().contains("MINOLTA"))) {
            serialNumber = snmpData.get(PrinterOIDs.KONICA_SERIAL_NUMBER);
        } else {
            serialNumber = snmpData.get(PrinterOIDs.GENERAL_SERIAL_NUMBER);
        }
        info.setSerialNumber(serialNumber != null ? serialNumber : "Unknown");
        
        // 根据品牌获取固件版本
        String firmwareVersion = null;
        if (sysDescr != null && sysDescr.toUpperCase().contains("EPSON")) {
            firmwareVersion = snmpData.get(PrinterOIDs.EPSON_FIRMWARE_VERSION);
        } else if (sysDescr != null && (sysDescr.toUpperCase().contains("KONICA") || 
                   sysDescr.toUpperCase().contains("MINOLTA"))) {
            firmwareVersion = snmpData.get(PrinterOIDs.KONICA_FIRMWARE_VERSION);
        } else {
            firmwareVersion = snmpData.get(PrinterOIDs.GENERAL_FIRMWARE_VERSION);
        }
        info.setFirmwareVersion(firmwareVersion != null ? firmwareVersion : "Unknown");

        // 根据品牌获取总打印页数
        try {
            String pageCount = null;
            if (sysDescr != null && sysDescr.toUpperCase().contains("EPSON")) {
                pageCount = snmpData.get(PrinterOIDs.EPSON_TOTAL_PAGES);
            } else if (sysDescr != null && (sysDescr.toUpperCase().contains("KONICA") || 
                       sysDescr.toUpperCase().contains("MINOLTA"))) {
                pageCount = snmpData.get(PrinterOIDs.KONICA_TOTAL_PAGES);
            } else {
                pageCount = snmpData.get(PrinterOIDs.GENERAL_TOTAL_PAGES);
            }
            
            if (pageCount != null && !pageCount.equals("noSuchObject") && !pageCount.equals("noSuchInstance")) {
                info.setTotalPages(Integer.parseInt(pageCount));
            }
        } catch (NumberFormatException e) {
            info.setTotalPages(0);
        }

        // 获取打印机状态 - 尝试多种OID
        PrinterStatus status = PrinterStatus.UNKNOWN;
        
        // 1. 优先尝试品牌特定状态OID
        if (sysDescr != null && sysDescr.toUpperCase().contains("RICOH")) {
            // 理光打印机使用其特定状态OID
            status = getPrinterStatus(snmpData.get(PrinterOIDs.RICOH_DEVICE_STATUS));
        }
        
        // 2. 如果未获取到有效状态，尝试RFC标准状态OID
        if (status == PrinterStatus.UNKNOWN) {
            status = getPrinterStatus(snmpData.get(PrinterOIDs.PRINTER_STATUS));
        }
        
        // 3. 如果仍未获取到有效状态，尝试通用状态OID
        if (status == PrinterStatus.UNKNOWN) {
            status = getPrinterStatus(snmpData.get(PrinterOIDs.PRINTER_STATUS_GENERAL));
        }
        
        // 4. 如果还是未知，尝试从错误状态推断
        if (status == PrinterStatus.UNKNOWN) {
            String errorState = snmpData.get(PrinterOIDs.PRINTER_DETECTED_ERROR_STATE);
            if (errorState != null && !errorState.equals("noSuchObject") && !errorState.equals("noSuchInstance")) {
                try {
                    int errorCode = Integer.parseInt(errorState);
                    if (errorCode > 0) {
                        status = PrinterStatus.OTHER;
                    }
                } catch (NumberFormatException e) {
                    // 忽略错误，保持UNKNOWN状态
                }
            }
        }
        
        info.setStatus(status);

        // 检测是否是打印服务器
        boolean isPrintServer = sysDescr != null && sysDescr.toUpperCase().contains("PRINT SERVER");
        
        // 仅当不是打印服务器时才尝试获取耗材和纸张信息
        if (!isPrintServer) {
            // 尝试获取耗材信息
            info.setSupplies(getSuppliesInfo());

            // 尝试获取纸张信息
            info.setPapers(getPaperInfo());
        } else {
            // 打印服务器不提供耗材和纸张信息
            log.debug("设备是打印服务器，跳过耗材和纸张信息获取，设备IP: {}", printerIp);
            info.setSupplies(new ArrayList<>());
            info.setPapers(new ArrayList<>());
        }

        // 获取网络信息
        info.setNetworkInfo(getNetworkInfo());

        info.setLastUpdate(new Date());
        return info;
    }

    /**
     * 获取打印机状态
     */
    private PrinterStatus getPrinterStatus(String statusCode) {
        if (statusCode == null || statusCode.equals("noSuchObject") || statusCode.equals("noSuchInstance")) {
            return PrinterStatus.UNKNOWN;
        }

        try {
            int code = Integer.parseInt(statusCode);
            switch (code) {
                case 1: return PrinterStatus.OTHER;
                case 2: return PrinterStatus.UNKNOWN;
                case 3: return PrinterStatus.IDLE;
                case 4: return PrinterStatus.PRINTING;
                case 5: return PrinterStatus.WARMUP;
                case 6: return PrinterStatus.STOPPED; // 添加停止状态
                case 7: return PrinterStatus.OFFLINE; // 添加离线状态
                // 理光打印机特定状态码
                case 100: return PrinterStatus.IDLE;    // 理光: 空闲
                case 101: return PrinterStatus.PRINTING; // 理光: 打印中
                case 102: return PrinterStatus.WARMUP;  // 理光: 预热
                case 103: return PrinterStatus.STOPPED; // 理光: 停止
                case 104: return PrinterStatus.OFFLINE; // 理光: 离线
                // 通用打印机扩展状态码
                case 200: return PrinterStatus.IDLE;
                case 201: return PrinterStatus.PRINTING;
                case 202: return PrinterStatus.WARMUP;
                case 203: return PrinterStatus.STOPPED;
                case 204: return PrinterStatus.OFFLINE;
                default: return PrinterStatus.UNKNOWN;
            }
        } catch (NumberFormatException e) {
            // 尝试处理字符串状态值
            String statusStr = statusCode.toUpperCase();
            if (statusStr.contains("IDLE") || statusStr.contains("READY")) {
                return PrinterStatus.IDLE;
            } else if (statusStr.contains("PRINT") || statusStr.contains("BUSY")) {
                return PrinterStatus.PRINTING;
            } else if (statusStr.contains("WARMUP") || statusStr.contains("INITIALIZ")) {
                return PrinterStatus.WARMUP;
            } else if (statusStr.contains("STOP") || statusStr.contains("ERROR")) {
                return PrinterStatus.STOPPED;
            } else if (statusStr.contains("OFFLINE") || statusStr.contains("DISCONNECT")) {
                return PrinterStatus.OFFLINE;
            }
            return PrinterStatus.UNKNOWN;
        }
    }

    /**
     * 获取耗材信息
     */
    private List<SupplyInfo> getSuppliesInfo() throws IOException {
        List<SupplyInfo> supplies = new ArrayList<>();
        log.debug("开始获取耗材信息，设备IP: {}", printerIp);

        // 遍历可能的耗材索引
        for (int i = 1; i <= 10; i++) {
            try {
                String levelOid = PrinterOIDs.SUPPLY_LEVEL + "." + i;
                String descOid = PrinterOIDs.SUPPLY_DESCRIPTION + "." + i;

                String levelStr = getSingleOID(levelOid);
                String description = getSingleOID(descOid);

                // 过滤无效响应
                boolean hasValidData = false;
                if (description != null && !description.equals("noSuchInstance") && !description.equals("noSuchObject")) {
                    hasValidData = true;
                }
                
                if (levelStr != null && !levelStr.equals("noSuchInstance") && !levelStr.equals("noSuchObject")) {
                    hasValidData = true;
                }

                if (hasValidData) {
                    SupplyInfo supply = new SupplyInfo();
                    supply.setDescription(description != null ? description : "Unknown");

                    try {
                        int level = Integer.parseInt(levelStr != null ? levelStr : "0");
                        // SNMP通常返回-2表示未知，-3表示满
                        if (level >= 0 && level <= 100) {
                            supply.setLevel(level);
                        } else if (level == -2) {
                            supply.setLevel(0); // 未知
                        } else if (level == -3) {
                            supply.setLevel(100); // 满
                        } else {
                            supply.setLevel(0);
                        }
                    } catch (NumberFormatException e) {
                        supply.setLevel(0);
                    }

                    supplies.add(supply);
                    log.debug("成功获取耗材信息: {}, 设备IP: {}", supply.getDescription(), printerIp);
                }
            } catch (Exception e) {
                // 继续尝试下一个索引
                log.debug("获取耗材信息失败，索引: {}，设备IP: {}, 错误: {}", i, printerIp, e.getMessage());
                continue;
            }
        }

        log.debug("获取耗材信息完成，共获取到 {} 条有效耗材信息，设备IP: {}", supplies.size(), printerIp);
        return supplies;
    }

    /**
     * 获取纸张信息
     */
    private List<PaperInfo> getPaperInfo() throws IOException {
        List<PaperInfo> papers = new ArrayList<>();
        log.debug("开始获取纸张信息，设备IP: {}", printerIp);

        // 遍历可能的纸张索引
        for (int i = 1; i <= 10; i++) {
            try {
                String descOid = PrinterOIDs.PRINTER_MEDIA_DESCRIPTION + "." + i;
                String typeOid = PrinterOIDs.PRINTER_MEDIA_TYPE + "." + i;
                String levelOid = PrinterOIDs.PRINTER_MEDIA_LEVEL + "." + i;
                String statusOid = PrinterOIDs.PRINTER_MEDIA_STATUS + "." + i;
                String sizeOid = PrinterOIDs.PRINTER_MEDIA_SIZE + "." + i;

                String description = getSingleOID(descOid);
                String type = getSingleOID(typeOid);
                String levelStr = getSingleOID(levelOid);
                String status = getSingleOID(statusOid);
                String size = getSingleOID(sizeOid);

                // 过滤无效响应
                boolean hasValidData = false;
                if (description != null && !description.equals("noSuchInstance") && !description.equals("noSuchObject")) {
                    hasValidData = true;
                } else {
                    description = "Unknown";
                }
                
                if (type != null && !type.equals("noSuchInstance") && !type.equals("noSuchObject")) {
                    hasValidData = true;
                } else {
                    type = "Unknown";
                }
                
                if (status != null && !status.equals("noSuchInstance") && !status.equals("noSuchObject")) {
                    hasValidData = true;
                } else {
                    status = "Unknown";
                }
                
                if (size != null && !size.equals("noSuchInstance") && !size.equals("noSuchObject")) {
                    hasValidData = true;
                } else {
                    size = "Unknown";
                }

                if (hasValidData || (levelStr != null && !levelStr.equals("noSuchInstance") && !levelStr.equals("noSuchObject"))) {
                    PaperInfo paper = new PaperInfo();
                    paper.setDescription(description);
                    paper.setType(type);
                    paper.setStatus(status);
                    paper.setSize(size);

                    // 处理纸张余量
                    if (levelStr != null && !levelStr.equals("noSuchInstance") && !levelStr.equals("noSuchObject")) {
                        try {
                            int level = Integer.parseInt(levelStr);
                            // SNMP通常返回-2表示未知，-3表示满
                            if (level >= 0 && level <= 100) {
                                paper.setLevel(level);
                            } else if (level == -2) {
                                paper.setLevel(0); // 未知
                            } else if (level == -3) {
                                paper.setLevel(100); // 满
                            } else {
                                paper.setLevel(0);
                            }
                        } catch (NumberFormatException e) {
                            paper.setLevel(0);
                        }
                    } else {
                        paper.setLevel(0);
                    }

                    papers.add(paper);
                    log.debug("成功获取纸张信息: {}, 设备IP: {}", paper.getDescription(), printerIp);
                }
            } catch (Exception e) {
                // 继续尝试下一个索引
                log.debug("获取纸张信息失败，索引: {}，设备IP: {}, 错误: {}", i, printerIp, e.getMessage());
                continue;
            }
        }

        log.debug("获取纸张信息完成，共获取到 {} 条有效纸张信息，设备IP: {}", papers.size(), printerIp);
        return papers;
    }

    /**
     * 获取网络信息
     */
    private NetworkInfo getNetworkInfo() throws IOException {
        NetworkInfo networkInfo = new NetworkInfo();
        networkInfo.setIpAddress(printerIp);

        try {
            // 获取MAC地址
            String macAddress = getSingleOID(PrinterOIDs.IF_MAC_ADDRESS + ".1");
            networkInfo.setMacAddress(formatMacAddress(macAddress));
        } catch (Exception e) {
            // 忽略错误
        }

        return networkInfo;
    }

    private String formatMacAddress(String hexString) {
        if (hexString == null || hexString.length() < 12) {
            return "Unknown";
        }

        try {
            StringBuilder mac = new StringBuilder();
            for (int i = 0; i < 12; i += 2) {
                if (i > 0) mac.append(":");
                mac.append(hexString.substring(i, i + 2));
            }
            return mac.toString().toUpperCase();
        } catch (Exception e) {
            return hexString;
        }
    }

    /**
     * 创建SNMP目标
     */
    private CommunityTarget createTarget() {
        Address targetAddress = new UdpAddress(printerIp + "/" + DEFAULT_PORT);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(DEFAULT_COMMUNITY));
        target.setAddress(targetAddress);
        target.setRetries(DEFAULT_RETRIES);
        target.setTimeout(DEFAULT_TIMEOUT);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }

    public void close() {
        try {
            if (snmp != null) {
                snmp.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 数据模型类
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PrinterInfo {
        private String ipAddress;
        private String model;
        private String name;
        private String serialNumber;
        private String firmwareVersion;
        private String location;
        private PrinterStatus status;
        private int totalPages;
        private List<SupplyInfo> supplies;
        private List<PaperInfo> papers;
        private NetworkInfo networkInfo;
        private Date lastUpdate;

        // getters and setters
        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getSerialNumber() { return serialNumber; }
        public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }

        public String getFirmwareVersion() { return firmwareVersion; }
        public void setFirmwareVersion(String firmwareVersion) { this.firmwareVersion = firmwareVersion; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }

        public PrinterStatus getStatus() { return status; }
        public void setStatus(PrinterStatus status) { this.status = status; }

        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

        public List<SupplyInfo> getSupplies() { return supplies; }
        public void setSupplies(List<SupplyInfo> supplies) { this.supplies = supplies; }

        public List<PaperInfo> getPapers() { return papers; }
        public void setPapers(List<PaperInfo> papers) { this.papers = papers; }

        public NetworkInfo getNetworkInfo() { return networkInfo; }
        public void setNetworkInfo(NetworkInfo networkInfo) { this.networkInfo = networkInfo; }

        public Date getLastUpdate() { return lastUpdate; }
        public void setLastUpdate(Date lastUpdate) { this.lastUpdate = lastUpdate; }

        public String toJson() throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        }
    }

    public static class SupplyInfo {
        private String description;
        private int level; // 百分比

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
    }

    public static class PaperInfo {
        private String description;
        private String type;
        private int level; // 百分比
        private String status;
        private String size;

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }

        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
    }

    public static class NetworkInfo {
        private String ipAddress;
        private String macAddress;

        public String getIpAddress() { return ipAddress; }
        public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

        public String getMacAddress() { return macAddress; }
        public void setMacAddress(String macAddress) { this.macAddress = macAddress; }
    }

    public enum PrinterStatus {
        OTHER("其他"),
        UNKNOWN("未知"),
        IDLE("空闲"),
        PRINTING("打印中"),
        WARMUP("预热"),
        STOPPED("已停止"),
        OFFLINE("离线");

        private final String description;

        PrinterStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}