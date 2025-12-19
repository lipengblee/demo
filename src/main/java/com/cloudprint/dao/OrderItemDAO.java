package com.cloudprint.dao;

import com.cloudprint.model.TradeOrderItem;
import com.cloudprint.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {
    
    /**
     * 根据订单ID查询订单商品项
     * @param orderId 订单ID
     * @return 订单商品项列表
     */
    public List<TradeOrderItem> getOrderItemsByOrderId(Long orderId) {
        List<TradeOrderItem> orderItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法查询订单商品项");
                return orderItems;
            }
            
            String sql = "SELECT * FROM trade_order_item WHERE order_id = ? AND deleted = 0 ORDER BY id ASC";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, orderId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                TradeOrderItem item = new TradeOrderItem();
                item.setId(rs.getLong("id"));
                item.setUserId(rs.getLong("user_id"));
                item.setOrderId(rs.getLong("order_id"));
                item.setCartId(rs.getLong("cart_id"));
                item.setSpuId(rs.getLong("spu_id"));
                item.setSpuName(rs.getString("spu_name"));
                item.setSkuId(rs.getLong("sku_id"));
                item.setTenantId(rs.getLong("tenant_id"));
                item.setProperties(rs.getString("properties"));
                item.setPicUrl(rs.getString("pic_url"));
                item.setCount(rs.getInt("count"));
                item.setPrice(rs.getInt("price"));
                item.setDiscountPrice(rs.getInt("discount_price"));
                item.setDeliveryPrice(rs.getInt("delivery_price"));
                item.setAdjustPrice(rs.getInt("adjust_price"));
                item.setPayPrice(rs.getInt("pay_price"));
                item.setCouponPrice(rs.getInt("coupon_price"));
                item.setPointPrice(rs.getInt("point_price"));
                item.setUsePoint(rs.getInt("use_point"));
                item.setGivePoint(rs.getInt("give_point"));
                item.setVipPrice(rs.getInt("vip_price"));
                item.setAfterSaleId(rs.getLong("after_sale_id"));
                item.setAfterSaleStatus(rs.getInt("after_sale_status"));
                item.setCommentStatus(rs.getBoolean("comment_status"));
                item.setCreateTime(rs.getTimestamp("create_time"));
                item.setUpdateTime(rs.getTimestamp("update_time"));
                item.setCreator(rs.getString("creator"));
                item.setUpdater(rs.getString("updater"));
                item.setDeleted(rs.getBoolean("deleted"));
                item.setTenantId(rs.getLong("tenant_id"));
                item.setPageStart(rs.getInt("page_start"));
                item.setPageEnd(rs.getInt("page_end"));
                orderItems.add(item);
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
        
        return orderItems;
    }
}