package com.cloudprint.util;

import com.cloudprint.dao.PrintDeviceDAO;
import com.cloudprint.model.TradeDeliveryPickUpStore;
import com.cloudprint.model.TradePrintDevice;

import lombok.extern.slf4j.Slf4j;

import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 打印机状态检查工具类，用于查询当前门店打印机状态并更新到数据库
 */
@Slf4j
public class PrinterStatusChecker {
    private PrintDeviceDAO printDeviceDAO;
    private Timer statusCheckTimer;
    
    /**
     * 构造方法
     */
    public PrinterStatusChecker() {
        printDeviceDAO = new PrintDeviceDAO();
    }
    
    /**
     * 检查单个打印机是否在线
     * @param device 打印机设备对象
     * @return true表示在线，false表示离线
     */
    public boolean isPrinterOnline(TradePrintDevice device) {
        boolean isOnline = false;
        Socket socket = null;
        
        try {
            // 尝试连接打印机的端口（默认9100）
            int port = device.getPort() != null ? device.getPort() : 9100;
            socket = new Socket(device.getAddress(), port);
            socket.setSoTimeout(2000); // 设置2秒超时
            isOnline = true;
        } catch (Exception e) {
            // 连接失败，打印机离线
            log.info("打印机 {} 离线: {}", device.getName(), e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    // 忽略关闭异常
                }
            }
        }
        
        return isOnline;
    }
    
    /**
     * 更新单个打印机的在线状态到数据库
     * @param device 打印机设备对象
     * @return 更新是否成功
     */
    public boolean updatePrinterStatus(TradePrintDevice device) {
        try {
            boolean isOnline = isPrinterOnline(device);
            String oldStatus = device.getStatus();
            String newStatus = isOnline ? "IDLE" : "OFFLINE";
            
            // 如果状态发生变化，更新数据库
            if (oldStatus == null || !oldStatus.equals(newStatus)) {
                log.info("更新打印机 {} 状态: {} -> {}", device.getName(), oldStatus, newStatus);
                device.setStatus(newStatus);
                return printDeviceDAO.updatePrintDevice(device);
            }
        } catch (Exception e) {
            log.error("更新打印机状态失败: {}", e.getMessage(), e);
        }
        return false;
    }
    
    /**
     * 查询当前门店的所有打印机
     * @return 打印机列表
     */
    public List<TradePrintDevice> getCurrentStorePrinters() {
        // 从会话中获取当前选择的门店ID
        TradeDeliveryPickUpStore currentStore = UserSession.getCurrentStore();
        Long storeId = currentStore != null ? currentStore.getId() : null;
        
        // 查询所有打印机
        return printDeviceDAO.getAllPrintDevicesByNotLocal(storeId);
    }
    
    /**
     * 检查当前门店所有打印机的在线状态
     * @return 更新了状态的打印机数量
     */
    public int checkCurrentStorePrintersStatus() {
        int updatedCount = 0;
        try {
            // 获取当前门店的所有打印机
            List<TradePrintDevice> devices = getCurrentStorePrinters();
            
            log.info("开始检查当前门店打印机状态，共 {} 台打印机", devices.size());
            
            // 检查每个打印机的在线状态
            for (TradePrintDevice device : devices) {
                log.info("检查打印机 {} 状态，IP: {}", device.getName(), device.getAddress());
                if (updatePrinterStatus(device)) {
                    updatedCount++;
                }
            }
            
            log.info("完成检查，共更新了 {} 台打印机状态", updatedCount);
        } catch (Exception e) {
            log.error("检查当前门店打印机状态失败: {}", e.getMessage(), e);
        }
        return updatedCount;
    }
    
    /**
     * 启动打印机状态检查定时器
     * 每隔30秒检查一次当前门店打印机的在线状态
     */
    public void startStatusCheck() {
        startStatusCheck(30);
    }
    
    /**
     * 启动打印机状态检查定时器
     * @param intervalSeconds 检查间隔（秒）
     */
    public void startStatusCheck(int intervalSeconds) {
        // 如果定时器已经存在，先停止
        if (statusCheckTimer != null) {
            stopStatusCheck();
        }
        
        // 创建新的定时器
        statusCheckTimer = new Timer("PrinterStatusCheckTimer", true);
        
        // 设置定时任务
        statusCheckTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkCurrentStorePrintersStatus();
            }
        }, 0, intervalSeconds * 1000); // 立即执行，然后每隔指定秒数执行一次
        
        log.info("打印机状态检查定时器已启动，每隔 {} 秒执行一次", intervalSeconds);
    }
    
    /**
     * 停止打印机状态检查定时器
     */
    public void stopStatusCheck() {
        if (statusCheckTimer != null) {
            statusCheckTimer.cancel();
            statusCheckTimer = null;
            log.info("打印机状态检查定时器已停止");
        }
    }
    
    /**
     * 立即检查一次当前门店打印机的在线状态
     * @return 更新了状态的打印机数量
     */
    public int checkStatusNow() {
        return checkCurrentStorePrintersStatus();
    }
    
    /**
     * 检查指定门店的所有非本地打印机的在线状态
     * @param storeId 门店ID
     * @return 更新了状态的打印机数量
     */
    public int checkStorePrintersStatus(Long storeId) {
        int updatedCount = 0;
        try {
            // 查询指定门店的所有打印机
            List<TradePrintDevice> devices = printDeviceDAO.getAllPrintDevicesByNotLocal(storeId);
            
            log.info("开始检查门店 {} 的打印机状态，共 {} 台打印机", storeId != null ? storeId : "全部", devices.size());
            
            // 检查每个打印机的在线状态
            for (TradePrintDevice device : devices) {
                log.info("检查打印机 {} 状态，IP: {}", device.getName(), device.getAddress());
                if (updatePrinterStatus(device)) {
                    updatedCount++;
                }
            }
            
            log.info("完成门店 {} 打印机状态检查，共更新了 {} 台打印机状态", storeId != null ? storeId : "全部", updatedCount);
        } catch (Exception e) {
            log.error("检查指定门店打印机状态失败: {}", e.getMessage(), e);
        }
        return updatedCount;
    }
    
    /**
     * 检查所有打印机的在线状态（跨门店）
     * @return 更新了状态的打印机数量
     */
    public int checkAllPrintersStatus() {
        return checkStorePrintersStatus(null);
    }
}