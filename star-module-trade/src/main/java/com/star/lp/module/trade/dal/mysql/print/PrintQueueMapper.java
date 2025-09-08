package com.star.lp.module.trade.dal.mysql.print;

import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.module.trade.dal.dataobject.print.PrintQueueDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface PrintQueueMapper extends BaseMapperX<PrintQueueDO> {

    /**
     * 根据订单ID查询队列项
     */
    default PrintQueueDO selectByOrderId(Long orderId) {
        return selectOne(new LambdaQueryWrapperX<PrintQueueDO>()
                .eq(PrintQueueDO::getOrderId, orderId)
                .eq(PrintQueueDO::getDeleted, false)
                .orderByDesc(PrintQueueDO::getCreateTime)
                .last("LIMIT 1"));
    }

    /**
     * 根据设备ID查询队列列表
     */
    default List<PrintQueueDO> selectListByDeviceId(Long deviceId) {
        return selectList(new LambdaQueryWrapperX<PrintQueueDO>()
                .eq(PrintQueueDO::getDeviceId, deviceId)
                .eq(PrintQueueDO::getDeleted, false)
                .orderByAsc(PrintQueueDO::getPriority)
                .orderByAsc(PrintQueueDO::getCreateTime));
    }

    /**
     * 更新订单的打印状态
     */
    default int updateStatusByOrderId(Long orderId, String oldStatus, String newStatus) {
        return update(new PrintQueueDO().setStatus(newStatus),
                new LambdaUpdateWrapper<PrintQueueDO>()
                        .eq(PrintQueueDO::getOrderId, orderId)
                        .eq(PrintQueueDO::getStatus, oldStatus)
                        .eq(PrintQueueDO::getDeleted, false));
    }

    /**
     * 删除指定设备的特定状态任务
     */
    default int deleteByDeviceIdAndStatus(Long deviceId, List<String> statusList) {
        return delete(new LambdaQueryWrapperX<PrintQueueDO>()
                .eq(PrintQueueDO::getDeviceId, deviceId)
                .in(PrintQueueDO::getStatus, statusList)
                .eq(PrintQueueDO::getDeleted, false));
    }

    /**
     * 检查设备是否有激活的队列任务
     */
    default boolean existsByDeviceIdAndStatus(Long deviceId, List<String> statusList) {
        return selectCount(new LambdaQueryWrapperX<PrintQueueDO>()
                .eq(PrintQueueDO::getDeviceId, deviceId)
                .in(PrintQueueDO::getStatus, statusList)
                .eq(PrintQueueDO::getDeleted, false)) > 0;
    }

    /**
     * 统计设备队列数量
     */
    default Long countByDeviceIdAndStatus(Long deviceId, String status) {
        return selectCount(new LambdaQueryWrapperX<PrintQueueDO>()
                .eq(PrintQueueDO::getDeviceId, deviceId)
                .eq(PrintQueueDO::getStatus, status)
                .eq(PrintQueueDO::getDeleted, false));
    }

    /**
     * 更新设备的打印状态
     */
    default int updateStatusByDeviceId(Long deviceId, String oldStatus, String newStatus) {
        return update(new PrintQueueDO().setStatus(newStatus),
                new LambdaUpdateWrapper<PrintQueueDO>()
                        .eq(PrintQueueDO::getOrderId, deviceId)
                        .eq(PrintQueueDO::getStatus, oldStatus)
                        .eq(PrintQueueDO::getDeleted, false));
    }
}
