package com.cloudprint.dao;

import com.cloudprint.model.TradePrintDevice;
import com.cloudprint.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PrintDeviceDAO {
    
    /**
     * 添加打印机
     * @param device 打印机对象
     * @return 是否添加成功
     */
    public boolean addPrintDevice(TradePrintDevice device) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法添加打印机");
                return false;
            }
            
            String sql = "INSERT INTO trade_print_device (store_id, name, type, model, status, location, address, " +
                    "connection_type, port, paper_status, ink_level, queue_count, today_count, success_rate, is_local, " +
                    "last_connect_time, remark, deleted, creator, create_time, updater, update_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW())";
            
            ps = conn.prepareStatement(sql);
            ps.setLong(1, device.getStoreId());
            ps.setString(2, device.getName());
            ps.setString(3, device.getType());
            ps.setString(4, device.getModel());
            ps.setString(5, device.getStatus());
            ps.setString(6, device.getLocation());
            ps.setString(7, device.getAddress());
            ps.setString(8, device.getConnectionType());
            ps.setInt(9, device.getPort());
            ps.setString(10, device.getPaperStatus());
            ps.setInt(11, device.getInkLevel());
            ps.setInt(12, device.getQueueCount());
            ps.setInt(13, device.getTodayCount());
            ps.setInt(14, device.getSuccessRate());
            ps.setBoolean(15, device.getIsLocal() != null ? device.getIsLocal() : false);
            ps.setTimestamp(16, device.getLastConnectTime() != null ? new java.sql.Timestamp(device.getLastConnectTime().getTime()) : null);
            ps.setString(17, device.getRemark());
            ps.setBoolean(18, device.getDeleted());
            ps.setString(19, device.getCreator());
            ps.setString(20, device.getUpdater());
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                ConnectionPool.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 查询所有打印机
     * @param storeId 门店ID，用于筛选特定门店的打印机
     * @return 打印机列表
     */
    public List<TradePrintDevice> getAllPrintDevices(Long storeId) {
        List<TradePrintDevice> devices = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法查询打印机");
                return devices;
            }
            
            String sql = "SELECT * FROM trade_print_device WHERE deleted = 0";
            if (storeId != null) {
                sql += " AND store_id = ?";
            }
            sql += " ORDER BY create_time DESC";
            ps = conn.prepareStatement(sql);
            
            if (storeId != null) {
                ps.setLong(1, storeId);
            }
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                TradePrintDevice device = new TradePrintDevice();
                device.setId(rs.getLong("id"));
                device.setStoreId(rs.getLong("store_id"));
                device.setName(rs.getString("name"));
                device.setType(rs.getString("type"));
                device.setModel(rs.getString("model"));
                device.setStatus(rs.getString("status"));
                device.setLocation(rs.getString("location"));
                device.setAddress(rs.getString("address"));
                device.setConnectionType(rs.getString("connection_type"));
                device.setPort(rs.getInt("port"));
                device.setPaperStatus(rs.getString("paper_status"));
                device.setInkLevel(rs.getInt("ink_level"));
                device.setQueueCount(rs.getInt("queue_count"));
                device.setTodayCount(rs.getInt("today_count"));
                device.setSuccessRate(rs.getInt("success_rate"));
                device.setIsLocal(rs.getBoolean("is_local"));
                device.setLastConnectTime(rs.getTimestamp("last_connect_time"));
                device.setRemark(rs.getString("remark"));
                device.setDeleted(rs.getBoolean("deleted"));
                device.setCreator(rs.getString("creator"));
                device.setCreateTime(rs.getTimestamp("create_time"));
                device.setUpdater(rs.getString("updater"));
                device.setUpdateTime(rs.getTimestamp("update_time"));
                
                devices.add(device);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                ConnectionPool.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return devices;
    }
    

    /**
     * 查询所有非本地打印机
     * @param storeId 门店ID，用于筛选特定门店的打印机
     * @return 打印机列表
     */
    public List<TradePrintDevice> getAllPrintDevicesByNotLocal(Long storeId) {
        List<TradePrintDevice> devices = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法查询打印机");
                return devices;
            }
            
            String sql = "SELECT * FROM trade_print_device WHERE deleted = 0 AND is_local = 0";
            if (storeId != null) {
                sql += " AND store_id = ?";
            }
            sql += " ORDER BY create_time DESC";
            ps = conn.prepareStatement(sql);
            
            if (storeId != null) {
                ps.setLong(1, storeId);
            }
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
                TradePrintDevice device = new TradePrintDevice();
                device.setId(rs.getLong("id"));
                device.setStoreId(rs.getLong("store_id"));
                device.setName(rs.getString("name"));
                device.setType(rs.getString("type"));
                device.setModel(rs.getString("model"));
                device.setStatus(rs.getString("status"));
                device.setLocation(rs.getString("location"));
                device.setAddress(rs.getString("address"));
                device.setConnectionType(rs.getString("connection_type"));
                device.setPort(rs.getInt("port"));
                device.setPaperStatus(rs.getString("paper_status"));
                device.setInkLevel(rs.getInt("ink_level"));
                device.setQueueCount(rs.getInt("queue_count"));
                device.setTodayCount(rs.getInt("today_count"));
                device.setSuccessRate(rs.getInt("success_rate"));
                device.setIsLocal(rs.getBoolean("is_local"));
                device.setLastConnectTime(rs.getTimestamp("last_connect_time"));
                device.setRemark(rs.getString("remark"));
                device.setDeleted(rs.getBoolean("deleted"));
                device.setCreator(rs.getString("creator"));
                device.setCreateTime(rs.getTimestamp("create_time"));
                device.setUpdater(rs.getString("updater"));
                device.setUpdateTime(rs.getTimestamp("update_time"));
                
                devices.add(device);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                ConnectionPool.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return devices;
    }
    
    /**
     * 根据ID查询打印机
     * @param deviceId 打印机ID
     * @return 打印机对象
     */
    public TradePrintDevice getPrintDeviceById(Long deviceId) {
        TradePrintDevice device = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法查询打印机详情");
                return null;
            }
            
            String sql = "SELECT * FROM trade_print_device WHERE id = ? AND deleted = 0";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, deviceId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                device = new TradePrintDevice();
                device.setId(rs.getLong("id"));
                device.setStoreId(rs.getLong("store_id"));
                device.setName(rs.getString("name"));
                device.setType(rs.getString("type"));
                device.setModel(rs.getString("model"));
                device.setStatus(rs.getString("status"));
                device.setLocation(rs.getString("location"));
                device.setAddress(rs.getString("address"));
                device.setConnectionType(rs.getString("connection_type"));
                device.setPort(rs.getInt("port"));
                device.setPaperStatus(rs.getString("paper_status"));
                device.setInkLevel(rs.getInt("ink_level"));
                device.setQueueCount(rs.getInt("queue_count"));
                device.setTodayCount(rs.getInt("today_count"));
                device.setSuccessRate(rs.getInt("success_rate"));
                device.setIsLocal(rs.getBoolean("is_local"));
                device.setLastConnectTime(rs.getTimestamp("last_connect_time"));
                device.setRemark(rs.getString("remark"));
                device.setDeleted(rs.getBoolean("deleted"));
                device.setCreator(rs.getString("creator"));
                device.setCreateTime(rs.getTimestamp("create_time"));
                device.setUpdater(rs.getString("updater"));
                device.setUpdateTime(rs.getTimestamp("update_time"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                ConnectionPool.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return device;
    }
    
    /**
     * 更新打印机信息
     * @param device 打印机对象
     * @return 更新成功返回true，否则返回false
     */
    public boolean updatePrintDevice(TradePrintDevice device) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法更新打印机");
                return false;
            }
            
            String sql = "UPDATE trade_print_device SET name = ?, type = ?, model = ?, status = ?, location = ?, " +
                    "address = ?, port = ?, paper_status = ?, ink_level = ?, is_local = ?, remark = ?, updater = ?, update_time = NOW() " +
                    "WHERE id = ? AND deleted = 0";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, device.getName());
            ps.setString(2, device.getType());
            ps.setString(3, device.getModel());
            ps.setString(4, device.getStatus());
            ps.setString(5, device.getLocation());
            ps.setString(6, device.getAddress());
            ps.setInt(7, device.getPort());
            ps.setString(8, device.getPaperStatus());
            ps.setInt(9, device.getInkLevel());
            ps.setBoolean(10, device.getIsLocal() != null ? device.getIsLocal() : false);
            ps.setString(11, device.getRemark());
            ps.setString(12, "admin"); // 固定更新人为admin
            ps.setLong(13, device.getId());
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                ConnectionPool.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 删除打印机信息（软删除）
     * @param deviceId 打印机ID
     * @return 删除成功返回true，否则返回false
     */
    public boolean deletePrintDevice(Long deviceId) {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法删除打印机");
                return false;
            }
            
            String sql = "UPDATE trade_print_device SET deleted = 1, updater = ?, update_time = NOW() " +
                    "WHERE id = ? AND deleted = 0";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, "admin"); // 固定删除人为admin
            ps.setLong(2, deviceId);
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                ConnectionPool.releaseConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}