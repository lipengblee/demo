package com.star.lp.module.trade.controller.admin.delivery;

import cn.hutool.core.collection.CollUtil;
import com.star.lp.framework.common.enums.CommonStatusEnum;
import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.common.util.object.BeanUtils;
import com.star.lp.module.member.api.user.MemberUserApi;
import com.star.lp.module.member.api.user.dto.MemberUserRespDTO;
import com.star.lp.module.system.api.user.AdminUserApi;
import com.star.lp.module.system.api.user.dto.AdminUserRespDTO;
import com.star.lp.module.trade.controller.admin.base.system.user.UserSimpleBaseVO;
import com.star.lp.module.trade.controller.admin.delivery.vo.pickup.*;
import com.star.lp.module.trade.convert.delivery.DeliveryPickUpStoreConvert;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreDO;
import com.star.lp.module.trade.service.delivery.DeliveryPickUpStoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.star.lp.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 自提门店")
@RestController
@RequestMapping("/trade/delivery/pick-up-store")
@Validated
public class DeliveryPickUpStoreController {

    @Resource
    private DeliveryPickUpStoreService deliveryPickUpStoreService;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private MemberUserApi memberUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建自提门店")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:create')")
    public CommonResult<Long> createDeliveryPickUpStore(@Valid @RequestBody DeliveryPickUpStoreCreateReqVO createReqVO) {
        return success(deliveryPickUpStoreService.createDeliveryPickUpStore(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新自提门店")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:update')")
    public CommonResult<Boolean> updateDeliveryPickUpStore(@Valid @RequestBody DeliveryPickUpStoreUpdateReqVO updateReqVO) {
        deliveryPickUpStoreService.updateDeliveryPickUpStore(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除自提门店")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:delete')")
    public CommonResult<Boolean> deleteDeliveryPickUpStore(@RequestParam("id") Long id) {
        deliveryPickUpStoreService.deleteDeliveryPickUpStore(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得自提门店")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:query')")
    public CommonResult<DeliveryPickUpStoreRespVO> getDeliveryPickUpStore(@RequestParam("id") Long id) {
        DeliveryPickUpStoreDO deliveryPickUpStore = deliveryPickUpStoreService.getDeliveryPickUpStore(id);
        if (deliveryPickUpStore == null) {
            return success(null);
        }
        List<AdminUserRespDTO> verifyUsers = CollUtil.isNotEmpty(deliveryPickUpStore.getVerifyUserIds()) ?
                adminUserApi.getUserList(deliveryPickUpStore.getVerifyUserIds()) : null;
        //添加查询会员用户
        List<MemberUserRespDTO> storeOperationUsers = CollUtil.isNotEmpty(deliveryPickUpStore.getStoreOperationUserIds()) ?
                memberUserApi.getUserList(deliveryPickUpStore.getStoreOperationUserIds()) : null;
        return success(BeanUtils.toBean(deliveryPickUpStore, DeliveryPickUpStoreRespVO.class)
                .setVerifyUsers(BeanUtils.toBean(verifyUsers, UserSimpleBaseVO.class))
                .setStoreOperationUsers(BeanUtils.toBean(storeOperationUsers, UserSimpleBaseVO.class))
                );
    }

    @GetMapping("/simple-list")
    @Operation(summary = "获得自提门店精简信息列表")
    public CommonResult<List<DeliveryPickUpStoreSimpleRespVO>> getSimpleDeliveryPickUpStoreList() {
        List<DeliveryPickUpStoreDO> list = deliveryPickUpStoreService.getDeliveryPickUpStoreListByStatus(
                CommonStatusEnum.ENABLE.getStatus());
        return success(DeliveryPickUpStoreConvert.INSTANCE.convertList1(list));
    }

    @GetMapping("/list")
    @Operation(summary = "获得自提门店列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:query')")
    public CommonResult<List<DeliveryPickUpStoreRespVO>> getDeliveryPickUpStoreList(@RequestParam("ids") Collection<Long> ids) {
        List<DeliveryPickUpStoreDO> list = deliveryPickUpStoreService.getDeliveryPickUpStoreList(ids);
        return success(DeliveryPickUpStoreConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得自提门店分页")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:query')")
    public CommonResult<PageResult<DeliveryPickUpStoreRespVO>> getDeliveryPickUpStorePage(@Valid DeliveryPickUpStorePageReqVO pageVO) {
        PageResult<DeliveryPickUpStoreDO> pageResult = deliveryPickUpStoreService.getDeliveryPickUpStorePage(pageVO);
        return success(DeliveryPickUpStoreConvert.INSTANCE.convertPage(pageResult));
    }

    @PostMapping("/bind")
    @Operation(summary = "绑定自提店员")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:create')")
    public CommonResult<Boolean> bindDeliveryPickUpStore(@Valid @RequestBody DeliveryPickUpBindReqVO bindReqVO) {
        deliveryPickUpStoreService.bindDeliveryPickUpStore(bindReqVO);
        return success(true);
    }

    @PostMapping("/bind-operation")
    @Operation(summary = "绑定订单操作员")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store:create')")
    public CommonResult<Boolean> bindOperationDeliveryPickUpStore(@Valid @RequestBody DeliveryPickUpBindOperationReqVO bindReqVO) {
        deliveryPickUpStoreService.bindOperationDeliveryPickUpStore(bindReqVO);
        return success(true);
    }

}
