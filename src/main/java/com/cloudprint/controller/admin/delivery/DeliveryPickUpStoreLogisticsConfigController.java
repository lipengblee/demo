package com.star.lp.module.trade.controller.admin.delivery;

import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.common.util.object.BeanUtils;
import com.star.lp.module.trade.controller.admin.delivery.vo.logistics.*;
import com.star.lp.module.trade.convert.delivery.DeliveryPickUpStoreLogisticsConfigConvert;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreLogisticsConfigDO;
import com.star.lp.module.trade.service.delivery.DeliveryPickUpStoreLogisticsConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.star.lp.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 自提门店物流配置
 *
 * @author star
 */
@Tag(name = "管理后台 - 自提门店物流配置")
@RestController
@RequestMapping("/trade/delivery/pick-up-store/logistics-config")
@Validated
public class DeliveryPickUpStoreLogisticsConfigController {

    @Resource
    private DeliveryPickUpStoreLogisticsConfigService logisticsConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建自提门店物流配置")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store-logistics-config:create')")
    public CommonResult<Long> createDeliveryPickUpStoreLogisticsConfig(@Valid @RequestBody DeliveryPickUpStoreLogisticsConfigCreateReqVO createReqVO) {
        return success(logisticsConfigService.createDeliveryPickUpStoreLogisticsConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新自提门店物流配置")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store-logistics-config:update')")
    public CommonResult<Boolean> updateDeliveryPickUpStoreLogisticsConfig(@Valid @RequestBody DeliveryPickUpStoreLogisticsConfigUpdateReqVO updateReqVO) {
        logisticsConfigService.updateDeliveryPickUpStoreLogisticsConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除自提门店物流配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store-logistics-config:delete')")
    public CommonResult<Boolean> deleteDeliveryPickUpStoreLogisticsConfig(@RequestParam("id") Long id) {
        logisticsConfigService.deleteDeliveryPickUpStoreLogisticsConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得自提门店物流配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store-logistics-config:query')")
    public CommonResult<DeliveryPickUpStoreLogisticsConfigRespVO> getDeliveryPickUpStoreLogisticsConfig(@RequestParam("id") Long id) {
        DeliveryPickUpStoreLogisticsConfigDO logisticsConfig = logisticsConfigService.getDeliveryPickUpStoreLogisticsConfig(id);
        return success(BeanUtils.toBean(logisticsConfig, DeliveryPickUpStoreLogisticsConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得自提门店物流配置分页")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store-logistics-config:query')")
    public CommonResult<PageResult<DeliveryPickUpStoreLogisticsConfigRespVO>> getDeliveryPickUpStoreLogisticsConfigPage(@Valid DeliveryPickUpStoreLogisticsConfigPageReqVO pageReqVO) {
        PageResult<DeliveryPickUpStoreLogisticsConfigDO> pageResult = logisticsConfigService.getDeliveryPickUpStoreLogisticsConfigPage(pageReqVO);
        return success(DeliveryPickUpStoreLogisticsConfigConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/list-by-store-id")
    @Operation(summary = "根据门店ID获得物流配置列表")
    @Parameter(name = "storeId", description = "门店ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store-logistics-config:query')")
    public CommonResult<List<DeliveryPickUpStoreLogisticsConfigRespVO>> getLogisticsConfigListByStoreId(@RequestParam("storeId") Long storeId) {
        List<DeliveryPickUpStoreLogisticsConfigDO> list = logisticsConfigService.getLogisticsConfigListByStoreId(storeId);
        return success(DeliveryPickUpStoreLogisticsConfigConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/get-by-store-id-and-logistics-name")
    @Operation(summary = "根据门店ID和物流名称获得物流配置")
    @Parameter(name = "storeId", description = "门店ID", required = true, example = "1024")
    @Parameter(name = "logisticsName", description = "物流名称", required = true, example = "顺丰速运")
    @PreAuthorize("@ss.hasPermission('trade:delivery:pick-up-store-logistics-config:query')")
    public CommonResult<DeliveryPickUpStoreLogisticsConfigRespVO> getLogisticsConfigByStoreIdAndLogisticsName(
            @RequestParam("storeId") Long storeId,
            @RequestParam("logisticsName") String logisticsName) {
        DeliveryPickUpStoreLogisticsConfigDO config = logisticsConfigService.getLogisticsConfigByStoreIdAndLogisticsName(storeId, logisticsName);
        return success(BeanUtils.toBean(config, DeliveryPickUpStoreLogisticsConfigRespVO.class));
    }

}
