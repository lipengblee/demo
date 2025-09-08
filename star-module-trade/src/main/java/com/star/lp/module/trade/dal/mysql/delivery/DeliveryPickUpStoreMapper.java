package com.star.lp.module.trade.dal.mysql.delivery;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.module.trade.controller.admin.delivery.vo.pickup.DeliveryPickUpStorePageReqVO;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DeliveryPickUpStoreMapper extends BaseMapperX<DeliveryPickUpStoreDO> {

    default PageResult<DeliveryPickUpStoreDO> selectPage(DeliveryPickUpStorePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeliveryPickUpStoreDO>()
                .likeIfPresent(DeliveryPickUpStoreDO::getName, reqVO.getName())
                .eqIfPresent(DeliveryPickUpStoreDO::getPhone, reqVO.getPhone())
                .eqIfPresent(DeliveryPickUpStoreDO::getAreaId, reqVO.getAreaId())
                .eqIfPresent(DeliveryPickUpStoreDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DeliveryPickUpStoreDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeliveryPickUpStoreDO::getId));
    }

    default List<DeliveryPickUpStoreDO> selectListByStatus(Integer status) {
        return selectList(DeliveryPickUpStoreDO::getStatus, status);
    }

    /**
     * 根据验证用户ID查询门店
     */
    default DeliveryPickUpStoreDO selectByVerifyUserId(Long userId) {
        return selectOne(new LambdaQueryWrapperX<DeliveryPickUpStoreDO>()
                .like(DeliveryPickUpStoreDO::getVerifyUserIds, userId.toString())
                .eq(DeliveryPickUpStoreDO::getDeleted, false));
    }

    /**
     * 根据店铺运营用户ID查询取货店信息
     *
     * @param userId 店铺运营用户ID
     * @return 返回匹配的取货店信息，如果没有找到则返回null
     */
    default DeliveryPickUpStoreDO selectByStoreOperationUserId(Long userId) {
    // 使用LambdaQueryWrapperX创建查询条件
        return selectOne(new LambdaQueryWrapperX<DeliveryPickUpStoreDO>()
            // 查询storeOperationUserIds字段中包含userId字符串的记录
                .like(DeliveryPickUpStoreDO::getStoreOperationUserIds, userId.toString())
            // 只查询未被删除的记录（deleted字段为false）
                .eq(DeliveryPickUpStoreDO::getDeleted, false)
            // 按照ID降序排序
                .orderByDesc(DeliveryPickUpStoreDO::getId));
    }

}




