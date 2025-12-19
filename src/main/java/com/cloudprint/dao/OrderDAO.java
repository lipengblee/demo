package com.cloudprint.dao;

import com.cloudprint.model.TradeOrder;
import com.cloudprint.util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    
    /**
     * 查询某个指派门店的待打印订单
     * @param storeId 门店ID
     * @return 待打印订单列表
     */
    public List<TradeOrder> getPendingPrintOrdersByStoreId(Integer storeId) {
        List<TradeOrder> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法查询订单");
                return orders; // 返回空列表，避免空指针异常
            }
            
            String sql = "SELECT * FROM trade_order WHERE appoint_store_id = ? AND print_status = 'pending' AND deleted = 0 AND order_type = 2 AND pay_status = 1 ORDER BY create_time DESC";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, storeId);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                TradeOrder order = new TradeOrder();
                order.setId(rs.getLong("id"));
                order.setNo(rs.getString("no"));
                order.setType(rs.getInt("type"));
                order.setTerminal(rs.getInt("terminal"));
                order.setUserId(rs.getLong("user_id"));
                order.setUserIp(rs.getString("user_ip"));
                order.setUserRemark(rs.getString("user_remark"));
                order.setStatus(rs.getInt("status"));
                order.setProductCount(rs.getInt("product_count"));
                order.setCancelType(rs.getInt("cancel_type"));
                order.setRemark(rs.getString("remark"));
                order.setCommentStatus(rs.getBoolean("comment_status"));
                order.setBrokerageUserId(rs.getLong("brokerage_user_id"));
                order.setPayOrderId(rs.getLong("pay_order_id"));
                order.setPayStatus(rs.getBoolean("pay_status"));
                order.setPayTime(rs.getTimestamp("pay_time"));
                order.setPayChannelCode(rs.getString("pay_channel_code"));
                order.setFinishTime(rs.getTimestamp("finish_time"));
                order.setCancelTime(rs.getTimestamp("cancel_time"));
                order.setTotalPrice(rs.getInt("total_price"));
                order.setDiscountPrice(rs.getInt("discount_price"));
                order.setDeliveryPrice(rs.getInt("delivery_price"));
                order.setAdjustPrice(rs.getInt("adjust_price"));
                order.setPayPrice(rs.getInt("pay_price"));
                order.setDeliveryType(rs.getInt("delivery_type"));
                order.setLogisticsId(rs.getLong("logistics_id"));
                order.setLogisticsNo(rs.getString("logistics_no"));
                order.setDeliveryTime(rs.getTimestamp("delivery_time"));
                order.setReceiveTime(rs.getTimestamp("receive_time"));
                order.setReceiverName(rs.getString("receiver_name"));
                order.setReceiverMobile(rs.getString("receiver_mobile"));
                order.setReceiverAreaId(rs.getInt("receiver_area_id"));
                order.setReceiverDetailAddress(rs.getString("receiver_detail_address"));
                order.setPickUpStoreId(rs.getLong("pick_up_store_id"));
                order.setPickUpVerifyCode(rs.getString("pick_up_verify_code"));
                order.setRefundStatus(rs.getInt("refund_status"));
                order.setRefundPrice(rs.getInt("refund_price"));
                order.setCouponId(rs.getLong("coupon_id"));
                order.setCouponPrice(rs.getInt("coupon_price"));
                order.setUsePoint(rs.getInt("use_point"));
                order.setPointPrice(rs.getInt("point_price"));
                order.setGivePoint(rs.getInt("give_point"));
                order.setRefundPoint(rs.getInt("refund_point"));
                order.setVipPrice(rs.getInt("vip_price"));
                order.setGiveCouponTemplateCounts(rs.getString("give_coupon_template_counts"));
                order.setGiveCouponIds(rs.getString("give_coupon_ids"));
                order.setSeckillActivityId(rs.getLong("seckill_activity_id"));
                order.setBargainActivityId(rs.getLong("bargain_activity_id"));
                order.setBargainRecordId(rs.getLong("bargain_record_id"));
                order.setCombinationActivityId(rs.getLong("combination_activity_id"));
                order.setCombinationHeadId(rs.getLong("combination_head_id"));
                order.setCombinationRecordId(rs.getLong("combination_record_id"));
                order.setPointActivityId(rs.getLong("point_activity_id"));
                order.setCreator(rs.getString("creator"));
                order.setCreateTime(rs.getTimestamp("create_time"));
                order.setUpdater(rs.getString("updater"));
                order.setUpdateTime(rs.getTimestamp("update_time"));
                order.setDeleted(rs.getBoolean("deleted"));
                order.setTenantId(rs.getLong("tenant_id"));
                order.setAppointStoreId(rs.getInt("appoint_store_id"));
                order.setPrintStatus(rs.getString("print_status"));
                order.setOrderType(rs.getInt("order_type"));
                
                orders.add(order);
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
        
        return orders;
    }
    
    /**
     * 查询订单详情
     * @param orderId 订单ID
     * @return 订单对象
     */
    public TradeOrder getOrderById(Long orderId) {
        TradeOrder order = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            conn = ConnectionPool.getConnection();
            if (conn == null) {
                System.err.println("数据库连接为null，无法查询订单详情");
                return null;
            }
            
            String sql = "SELECT * FROM trade_order WHERE id = ? AND deleted = 0";
            ps = conn.prepareStatement(sql);
            ps.setLong(1, orderId);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                order = new TradeOrder();
                order.setId(rs.getLong("id"));
                order.setNo(rs.getString("no"));
                order.setType(rs.getInt("type"));
                order.setTerminal(rs.getInt("terminal"));
                order.setUserId(rs.getLong("user_id"));
                order.setUserIp(rs.getString("user_ip"));
                order.setUserRemark(rs.getString("user_remark"));
                order.setStatus(rs.getInt("status"));
                order.setProductCount(rs.getInt("product_count"));
                order.setCancelType(rs.getInt("cancel_type"));
                order.setRemark(rs.getString("remark"));
                order.setCommentStatus(rs.getBoolean("comment_status"));
                order.setBrokerageUserId(rs.getLong("brokerage_user_id"));
                order.setPayOrderId(rs.getLong("pay_order_id"));
                order.setPayStatus(rs.getBoolean("pay_status"));
                order.setPayTime(rs.getTimestamp("pay_time"));
                order.setPayChannelCode(rs.getString("pay_channel_code"));
                order.setFinishTime(rs.getTimestamp("finish_time"));
                order.setCancelTime(rs.getTimestamp("cancel_time"));
                order.setTotalPrice(rs.getInt("total_price"));
                order.setDiscountPrice(rs.getInt("discount_price"));
                order.setDeliveryPrice(rs.getInt("delivery_price"));
                order.setAdjustPrice(rs.getInt("adjust_price"));
                order.setPayPrice(rs.getInt("pay_price"));
                order.setDeliveryType(rs.getInt("delivery_type"));
                order.setLogisticsId(rs.getLong("logistics_id"));
                order.setLogisticsNo(rs.getString("logistics_no"));
                order.setDeliveryTime(rs.getTimestamp("delivery_time"));
                order.setReceiveTime(rs.getTimestamp("receive_time"));
                order.setReceiverName(rs.getString("receiver_name"));
                order.setReceiverMobile(rs.getString("receiver_mobile"));
                order.setReceiverAreaId(rs.getInt("receiver_area_id"));
                order.setReceiverDetailAddress(rs.getString("receiver_detail_address"));
                order.setPickUpStoreId(rs.getLong("pick_up_store_id"));
                order.setPickUpVerifyCode(rs.getString("pick_up_verify_code"));
                order.setRefundStatus(rs.getInt("refund_status"));
                order.setRefundPrice(rs.getInt("refund_price"));
                order.setCouponId(rs.getLong("coupon_id"));
                order.setCouponPrice(rs.getInt("coupon_price"));
                order.setUsePoint(rs.getInt("use_point"));
                order.setPointPrice(rs.getInt("point_price"));
                order.setGivePoint(rs.getInt("give_point"));
                order.setRefundPoint(rs.getInt("refund_point"));
                order.setVipPrice(rs.getInt("vip_price"));
                order.setGiveCouponTemplateCounts(rs.getString("give_coupon_template_counts"));
                order.setGiveCouponIds(rs.getString("give_coupon_ids"));
                order.setSeckillActivityId(rs.getLong("seckill_activity_id"));
                order.setBargainActivityId(rs.getLong("bargain_activity_id"));
                order.setBargainRecordId(rs.getLong("bargain_record_id"));
                order.setCombinationActivityId(rs.getLong("combination_activity_id"));
                order.setCombinationHeadId(rs.getLong("combination_head_id"));
                order.setCombinationRecordId(rs.getLong("combination_record_id"));
                order.setPointActivityId(rs.getLong("point_activity_id"));
                order.setCreator(rs.getString("creator"));
                order.setCreateTime(rs.getTimestamp("create_time"));
                order.setUpdater(rs.getString("updater"));
                order.setUpdateTime(rs.getTimestamp("update_time"));
                order.setDeleted(rs.getBoolean("deleted"));
                order.setTenantId(rs.getLong("tenant_id"));
                order.setAppointStoreId(rs.getInt("appoint_store_id"));
                order.setPrintStatus(rs.getString("print_status"));
                order.setOrderType(rs.getInt("order_type"));
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
        
        return order;
    }
}