package com.cloudprint.dao;

import com.cloudprint.model.TradeDeliveryPickUpStore;
import com.cloudprint.util.ConnectionPool;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自提门店数据访问对象
 */
@Slf4j
public class TradeDeliveryPickUpStoreDAO {

    /**
     * 根据ID查询门店信息
     * @param storeId 门店ID
     * @return 门店对象，如果不存在则返回null
     */
    public TradeDeliveryPickUpStore getStoreById(Long storeId) {
        TradeDeliveryPickUpStore store = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法查询门店信息");
                return null;
            }

            String sql = "SELECT * FROM trade_delivery_pick_up_store WHERE id = ? AND deleted = 0";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, storeId);
            rs = ps.executeQuery();

            if (rs.next()) {
                store = new TradeDeliveryPickUpStore();
                store.setId(rs.getLong("id"));
                store.setName(rs.getString("name"));
                store.setIntroduction(rs.getString("introduction"));
                store.setPhone(rs.getString("phone"));
                store.setAreaId(rs.getInt("area_id"));
                store.setDetailAddress(rs.getString("detail_address"));
                store.setLogo(rs.getString("logo"));
                store.setOpeningTime(rs.getString("opening_time"));
                store.setClosingTime(rs.getString("closing_time"));
                store.setLatitude(rs.getDouble("latitude"));
                store.setLongitude(rs.getDouble("longitude"));
                store.setVerifyUserIds(rs.getString("verify_user_ids"));
                store.setStatus(rs.getInt("status"));
                store.setCreator(rs.getString("creator"));
                store.setCreateTime(rs.getTimestamp("create_time"));
                store.setUpdater(rs.getString("updater"));
                store.setUpdateTime(rs.getTimestamp("update_time"));
                store.setDeleted(rs.getBoolean("deleted"));
                store.setTenantId(rs.getLong("tenant_id"));
                store.setStoreOperationUserIds(rs.getString("store_operation_user_ids"));
                store.setContact(rs.getString("contact"));
            }
        } catch (SQLException e) {
            System.err.println("查询门店信息失败: " + e.getMessage());
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

        return store;
    }

    /**
     * 根据操作员ID查询关联的门店列表
     * @param operatorUserId 操作员ID
     * @return 门店列表
     */
    public java.util.List<TradeDeliveryPickUpStore> getStoresByOperatorUserId(Long operatorUserId) {
        java.util.List<TradeDeliveryPickUpStore> stores = new java.util.ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        log.info("查询用户ID为 {} 的关联门店", operatorUserId);
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法查询门店信息");
                return stores;
            }

            // 查询所有未删除的门店（暂时移除状态条件以调试）
            String sql = "SELECT * FROM trade_delivery_pick_up_store WHERE deleted = 0";
            log.info("执行SQL: {}", sql);

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            
            int recordCount = 0;
            log.info("开始处理查询结果...");

            while (rs.next()) {
                recordCount++;
                // 获取门店ID和名称用于日志
                Long storeId = rs.getLong("id");
                String storeName = rs.getString("name");
                
                // 获取门店的验证用户ID列表
                String verifyUserIds = rs.getString("verify_user_ids");
                log.info("门店ID: {}, 名称: {}, 验证用户ID列表: {}", storeId, storeName, verifyUserIds);

                // 检查用户是否有权限访问该门店
                if (hasUserPermission(operatorUserId, verifyUserIds)) {
                    TradeDeliveryPickUpStore store = new TradeDeliveryPickUpStore();
                    store.setId(rs.getLong("id"));
                    store.setName(rs.getString("name"));
                    store.setIntroduction(rs.getString("introduction"));
                    store.setPhone(rs.getString("phone"));
                    store.setAreaId(rs.getInt("area_id"));
                    store.setDetailAddress(rs.getString("detail_address"));
                    store.setLogo(rs.getString("logo"));
                    store.setOpeningTime(rs.getString("opening_time"));
                    store.setClosingTime(rs.getString("closing_time"));
                    store.setLatitude(rs.getDouble("latitude"));
                    store.setLongitude(rs.getDouble("longitude"));
                    store.setVerifyUserIds(rs.getString("verify_user_ids"));
                    store.setStatus(rs.getInt("status"));
                    store.setCreator(rs.getString("creator"));
                    store.setCreateTime(rs.getTimestamp("create_time"));
                    store.setUpdater(rs.getString("updater"));
                    store.setUpdateTime(rs.getTimestamp("update_time"));
                    store.setDeleted(rs.getBoolean("deleted"));
                    store.setTenantId(rs.getLong("tenant_id"));
                    store.setStoreOperationUserIds(rs.getString("store_operation_user_ids"));
                    store.setContact(rs.getString("contact"));

                    stores.add(store);
                }
            }
            
            log.info("SQL查询共返回 {} 条记录，其中用户有权访问的门店数量: {}", recordCount, stores.size());
        } catch (SQLException e) {
            System.err.println("查询门店列表失败: " + e.getMessage());
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

        return stores;
    }

    /**
     * 检查用户是否有权限访问门店
     * @param userId 用户ID
     * @param verifyUserIds 门店验证用户ID字符串，以逗号分隔
     * @return 是否有权限
     */
    private boolean hasUserPermission(Long userId, String verifyUserIds) {
        log.info("检查用户ID {} 是否有权限访问门店，门店验证用户ID列表: {}", userId, verifyUserIds);
        
        if (verifyUserIds == null || verifyUserIds.trim().isEmpty()) {
            log.info("门店验证用户ID列表为空，用户无权限");
            return false;
        }

        String userIdStr = userId.toString();
        String[] userIds = verifyUserIds.split(",");
        log.info("分割后的验证用户ID数量: {}", userIds.length);

        for (String id : userIds) {
            log.info("比较验证用户ID: '{}' 与用户ID: '{}'", id.trim(), userIdStr);
            if (id.trim().equals(userIdStr)) {
                log.info("找到匹配，用户有权限访问该门店");
                return true;
            }
        }

        log.info("未找到匹配，用户无权限访问该门店");
        return false;
    }
}
