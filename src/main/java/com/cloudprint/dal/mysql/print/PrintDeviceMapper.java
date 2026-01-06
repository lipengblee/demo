package com.star.lp.module.trade.dal.mysql.print;

import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.module.trade.dal.dataobject.print.PrintDeviceDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PrintDeviceMapper extends BaseMapperX<PrintDeviceDO> {

    /**
     * 根据门店ID查询设备列表
     */
    default List<PrintDeviceDO> selectListByStoreId(Long storeId) {
        return selectList(new LambdaQueryWrapperX<PrintDeviceDO>()
                .eq(PrintDeviceDO::getStoreId, storeId)
                .eq(PrintDeviceDO::getDeleted, false)
                .orderByAsc(PrintDeviceDO::getCreateTime));
    }

    /**
     * 查询指派给订单的设备
     */
    @Select("SELECT d.* FROM trade_print_device d " +
            "INNER JOIN trade_print_queue q ON d.id = q.device_id " +
            "WHERE q.order_id = #{orderId} AND q.deleted = 0 LIMIT 1")
    PrintDeviceDO selectAssignedDevice(Long orderId);

    /**
     * 统计门店设备数量
     */
    default Long countByStoreId(Long storeId) {
        return selectCount(new LambdaQueryWrapperX<PrintDeviceDO>()
                .eq(PrintDeviceDO::getStoreId, storeId)
                .eq(PrintDeviceDO::getDeleted, false));
    }
}
