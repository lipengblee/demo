package com.star.lp.module.trade.dal.mysql.order;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.star.lp.module.trade.controller.admin.order.vo.TradeOrderPageReqVO;
import com.star.lp.module.trade.controller.admin.order.vo.TradeOrderAppointPageReqVO;
import com.star.lp.module.trade.controller.app.order.vo.AppTradeOrderPageReqVO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderDO;
import com.star.lp.module.trade.enums.order.TradeOrderTypeEnum;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface TradeOrderMapper extends BaseMapperX<TradeOrderDO> {

    default int updateByIdAndStatus(Long id, Integer status, TradeOrderDO update) {
        return update(update, new LambdaUpdateWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getId, id).eq(TradeOrderDO::getStatus, status));
    }

    default TradeOrderDO selectByIdAndUserId(Long id, Long userId) {
        return selectOne(TradeOrderDO::getId, id, TradeOrderDO::getUserId, userId);
    }

    default PageResult<TradeOrderDO> selectPage(TradeOrderPageReqVO reqVO, Set<Long> userIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TradeOrderDO>()
                .likeIfPresent(TradeOrderDO::getNo, reqVO.getNo())
                .eqIfPresent(TradeOrderDO::getUserId, reqVO.getUserId())
                .eqIfPresent(TradeOrderDO::getDeliveryType, reqVO.getDeliveryType())
                .inIfPresent(TradeOrderDO::getUserId, userIds)
                .eqIfPresent(TradeOrderDO::getType, reqVO.getType())
                .eqIfPresent(TradeOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TradeOrderDO::getPayChannelCode, reqVO.getPayChannelCode())
                .eqIfPresent(TradeOrderDO::getTerminal, reqVO.getTerminal())
                .eqIfPresent(TradeOrderDO::getLogisticsId, reqVO.getLogisticsId())
                .inIfPresent(TradeOrderDO::getPickUpStoreId, reqVO.getPickUpStoreIds())
                .likeIfPresent(TradeOrderDO::getPickUpVerifyCode, reqVO.getPickUpVerifyCode())
                .betweenIfPresent(TradeOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TradeOrderDO::getId));
    }
    
    default PageResult<TradeOrderDO> selectAppointStorePage(TradeOrderAppointPageReqVO reqVO, Set<Long> userIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TradeOrderDO>()
                .likeIfPresent(TradeOrderDO::getNo, reqVO.getNo())
                .eqIfPresent(TradeOrderDO::getUserId, reqVO.getUserId())
                .eqIfPresent(TradeOrderDO::getDeliveryType, reqVO.getDeliveryType())
                .inIfPresent(TradeOrderDO::getUserId, userIds)
                .eq(TradeOrderDO::getOrderType, 2) // 强制添加orderType=2的条件
                .eqIfPresent(TradeOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TradeOrderDO::getPayChannelCode, reqVO.getPayChannelCode())
                .eqIfPresent(TradeOrderDO::getTerminal, reqVO.getTerminal())
                .eqIfPresent(TradeOrderDO::getLogisticsId, reqVO.getLogisticsId())
                .inIfPresent(TradeOrderDO::getAppointStoreId, reqVO.getAppointStoreIds()) // 使用appointStoreId替代pickUpStoreId
                .likeIfPresent(TradeOrderDO::getPickUpVerifyCode, reqVO.getPickUpVerifyCode())
                .betweenIfPresent(TradeOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(TradeOrderDO::getId));
    }

    // TODO @疯狂：如果用 map 返回，要不这里直接用 TradeOrderSummaryRespVO 返回？也算合理，就当  sql 查询出这么个玩意~~
    default List<Map<String, Object>> selectOrderSummaryGroupByRefundStatus(TradeOrderPageReqVO reqVO, Set<Long> userIds) {
        return selectMaps(new MPJLambdaWrapperX<TradeOrderDO>()
                .selectAs(TradeOrderDO::getRefundStatus, TradeOrderDO::getRefundStatus)  // 售后状态
                .selectCount(TradeOrderDO::getId, "count") // 售后状态对应的数量
                .selectSum(TradeOrderDO::getPayPrice, "price")  // 售后状态对应的支付金额
                .likeIfPresent(TradeOrderDO::getNo, reqVO.getNo())
                .eqIfPresent(TradeOrderDO::getUserId, reqVO.getUserId())
                .eqIfPresent(TradeOrderDO::getDeliveryType, reqVO.getDeliveryType())
                .inIfPresent(TradeOrderDO::getUserId, userIds)
                .eqIfPresent(TradeOrderDO::getType, reqVO.getType())
                .eqIfPresent(TradeOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TradeOrderDO::getPayChannelCode, reqVO.getPayChannelCode())
                .eqIfPresent(TradeOrderDO::getTerminal, reqVO.getTerminal())
                .eqIfPresent(TradeOrderDO::getLogisticsId, reqVO.getLogisticsId())
                .inIfPresent(TradeOrderDO::getPickUpStoreId, reqVO.getPickUpStoreIds())
                .likeIfPresent(TradeOrderDO::getPickUpVerifyCode, reqVO.getPickUpVerifyCode())
                .betweenIfPresent(TradeOrderDO::getCreateTime, reqVO.getCreateTime())
                .groupBy(TradeOrderDO::getRefundStatus)); // 按售后状态分组
    }
    
    default List<Map<String, Object>> selectAppointStoreOrderSummaryGroupByRefundStatus(TradeOrderAppointPageReqVO reqVO, Set<Long> userIds) {
        return selectMaps(new MPJLambdaWrapperX<TradeOrderDO>()
                .selectAs(TradeOrderDO::getRefundStatus, TradeOrderDO::getRefundStatus)  // 售后状态
                .selectCount(TradeOrderDO::getId, "count") // 售后状态对应的数量
                .selectSum(TradeOrderDO::getPayPrice, "price")  // 售后状态对应的支付金额
                .likeIfPresent(TradeOrderDO::getNo, reqVO.getNo())
                .eqIfPresent(TradeOrderDO::getUserId, reqVO.getUserId())
                .eqIfPresent(TradeOrderDO::getDeliveryType, reqVO.getDeliveryType())
                .inIfPresent(TradeOrderDO::getUserId, userIds)
                .eq(TradeOrderDO::getOrderType, 2) // 强制添加orderType=2的条件
                .eqIfPresent(TradeOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TradeOrderDO::getPayChannelCode, reqVO.getPayChannelCode())
                .eqIfPresent(TradeOrderDO::getTerminal, reqVO.getTerminal())
                .eqIfPresent(TradeOrderDO::getLogisticsId, reqVO.getLogisticsId())
                .inIfPresent(TradeOrderDO::getAppointStoreId, reqVO.getAppointStoreIds()) // 使用appointStoreId替代pickUpStoreId
                .likeIfPresent(TradeOrderDO::getPickUpVerifyCode, reqVO.getPickUpVerifyCode())
                .betweenIfPresent(TradeOrderDO::getCreateTime, reqVO.getCreateTime())
                .groupBy(TradeOrderDO::getRefundStatus)); // 按售后状态分组
    }
    
    /**
     * 通过物流单号查询订单
     *
     * @param logisticsNo 物流单号
     * @return 订单列表
     */
    default List<TradeOrderDO> selectByLogisticsNo(String logisticsNo) {
        return selectList(new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getLogisticsNo, logisticsNo)
                .orderByDesc(TradeOrderDO::getCreateTime));
    }
    
    /**
     * 通过物流单号查询最新的一条订单
     *
     * @param logisticsNo 物流单号
     * @return 最新的一条订单
     */
    default TradeOrderDO selectLatestByLogisticsNo(String logisticsNo) {
        return selectOne(new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getLogisticsNo, logisticsNo)
                .orderByDesc(TradeOrderDO::getCreateTime)
                .last("LIMIT 1"));
    }

    default PageResult<TradeOrderDO> selectPage(AppTradeOrderPageReqVO reqVO, Long userId) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getUserId, userId)
                .eqIfPresent(TradeOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(TradeOrderDO::getCommentStatus, reqVO.getCommentStatus())
                .orderByDesc(TradeOrderDO::getId)); // TODO 芋艿：未来不同的 status，不同的排序
    }

    default Long selectCountByUserIdAndStatus(Long userId, Integer status, Boolean commentStatus) {
        return selectCount(new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getUserId, userId)
                .eqIfPresent(TradeOrderDO::getStatus, status)
                .eqIfPresent(TradeOrderDO::getCommentStatus, commentStatus));
    }

    default TradeOrderDO selectOrderByIdAndUserId(Long orderId, Long loginUserId) {
        return selectOne(new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getId, orderId)
                .eq(TradeOrderDO::getUserId, loginUserId));
    }

    default List<TradeOrderDO> selectListByStatusAndCreateTimeLt(Integer status, LocalDateTime createTime) {
        return selectList(new LambdaUpdateWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getStatus, status)
                .lt(TradeOrderDO::getCreateTime, createTime));
    }

    default List<TradeOrderDO> selectListByStatusAndDeliveryTimeLt(Integer status, LocalDateTime deliveryTime) {
        return selectList(new LambdaUpdateWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getStatus, status)
                .lt(TradeOrderDO::getDeliveryTime, deliveryTime));
    }

    default List<TradeOrderDO> selectListByStatusAndReceiveTimeLt(Integer status, LocalDateTime receive,
                                                                  Boolean commentStatus) {
        return selectList(new LambdaUpdateWrapper<TradeOrderDO>()
                .eq(TradeOrderDO::getStatus, status)
                .lt(TradeOrderDO::getReceiveTime, receive)
                .eq(TradeOrderDO::getCommentStatus, commentStatus));
    }

    default List<TradeOrderDO> selectListByUserIdAndActivityId(Long userId, Long activityId, TradeOrderTypeEnum type) {
        LambdaQueryWrapperX<TradeOrderDO> queryWrapperX = new LambdaQueryWrapperX<>();
        queryWrapperX.eq(TradeOrderDO::getUserId, userId);
        if (TradeOrderTypeEnum.isSeckill(type.getType())) {
            queryWrapperX.eq(TradeOrderDO::getSeckillActivityId, activityId);
        }
        if (TradeOrderTypeEnum.isBargain(type.getType())) {
            queryWrapperX.eq(TradeOrderDO::getBargainActivityId, activityId);
        }
        if (TradeOrderTypeEnum.isCombination(type.getType())) {
            queryWrapperX.eq(TradeOrderDO::getCombinationActivityId, activityId);
        }
        if (TradeOrderTypeEnum.isPoint(type.getType())) {
            queryWrapperX.eq(TradeOrderDO::getPointActivityId, activityId);
        }
        return selectList(queryWrapperX);
    }

    default TradeOrderDO selectOneByPickUpVerifyCode(String pickUpVerifyCode) {
        return selectOne(TradeOrderDO::getPickUpVerifyCode, pickUpVerifyCode);
    }

    /**
     * 通过订单编号查询订单
     *
     * @param orderNo 订单编号
     * @return 订单信息
     */
    default TradeOrderDO selectByNo(String orderNo) {
        return selectOne(TradeOrderDO::getNo, orderNo);
    }

    default TradeOrderDO selectByUserIdAndCombinationActivityIdAndStatus(Long userId, Long combinationActivityId, Integer status) {
        return selectOne(new LambdaQueryWrapperX<TradeOrderDO>()
                .eq(TradeOrderDO::getUserId, userId)
                .eq(TradeOrderDO::getStatus, status)
                .eq(TradeOrderDO::getCombinationActivityId, combinationActivityId)
        );
    }

    /**
     * 统计门店指定打印状态的订单数量
     */
    @Select("SELECT COUNT(*) FROM trade_order WHERE appoint_store_id = #{storeId} AND print_status = #{printStatus}")
    Integer countByStoreIdAndPrintStatus(Long storeId, String printStatus);

    /**
     * 统计门店今日完成的订单数量
     */
    @Select("SELECT COUNT(*) FROM trade_order WHERE appoint_store_id = #{storeId} " +
            "AND status = 40 AND DATE(complete_time) = CURDATE()")
    Integer countCompletedOrdersToday(Long storeId, java.time.LocalDateTime startOfDay);

    /**
     * 统计门店今日订单总数
     */
    @Select("SELECT COUNT(*) FROM trade_order WHERE appoint_store_id = #{storeId} " +
            "AND DATE(create_time) = CURDATE()")
    Integer countTotalOrdersToday(Long storeId, java.time.LocalDateTime startOfDay);

    /**
     * 统计门店今日营业额
     */
    @Select("SELECT COALESCE(SUM(pay_price), 0) FROM trade_order WHERE appoint_store_id = #{storeId} " +
            "AND pay_status = 1 AND DATE(create_time) = CURDATE()")
    Integer getTodayAmount(Long storeId, java.time.LocalDateTime startOfDay);

}
