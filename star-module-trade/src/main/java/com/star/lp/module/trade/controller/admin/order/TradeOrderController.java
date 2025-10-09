package com.star.lp.module.trade.controller.admin.order;

import cn.hutool.core.collection.CollUtil;
import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.member.api.user.MemberUserApi;
import com.star.lp.module.member.api.user.dto.MemberUserRespDTO;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentSpuDO;
import com.star.lp.module.product.dal.mysql.print.ProductPrintDocumentMapper;
import com.star.lp.module.trade.controller.admin.order.vo.*;
import com.star.lp.module.trade.convert.order.TradeOrderConvert;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderItemDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderLogDO;
import com.star.lp.module.trade.service.order.TradeOrderLogService;
import com.star.lp.module.trade.service.order.TradeOrderQueryService;
import com.star.lp.module.trade.service.order.TradeOrderUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.star.lp.framework.common.pojo.CommonResult.success;
import static com.star.lp.framework.common.util.collection.CollectionUtils.convertList;
import static com.star.lp.framework.common.util.collection.CollectionUtils.convertSet;
import static com.star.lp.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 交易订单")
@RestController
@RequestMapping("/trade/order")
@Validated
@Slf4j
public class TradeOrderController {

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;
    @Resource
    private TradeOrderQueryService tradeOrderQueryService;
    @Resource
    private TradeOrderLogService tradeOrderLogService;

    @Resource
    private MemberUserApi memberUserApi;

    @Resource
    private ProductPrintDocumentMapper productPrintDocumentMapper;

    @GetMapping("/page")
    @Operation(summary = "获得交易订单分页")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<PageResult<TradeOrderPageItemRespVO>> getOrderPage(TradeOrderPageReqVO reqVO) {
        // 查询订单
        PageResult<TradeOrderDO> pageResult = tradeOrderQueryService.getOrderPage(reqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

        // 查询用户信息
        Set<Long> userIds = CollUtil.unionDistinct(convertList(pageResult.getList(), TradeOrderDO::getUserId),
                convertList(pageResult.getList(), TradeOrderDO::getBrokerageUserId, Objects::nonNull));
        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(userIds);
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                convertSet(pageResult.getList(), TradeOrderDO::getId));
        // 最终组合
        return success(TradeOrderConvert.INSTANCE.convertPage(pageResult, orderItems, userMap));
    }

    @GetMapping("/summary")
    @Operation(summary = "获得交易订单统计")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<TradeOrderSummaryRespVO> getOrderSummary(TradeOrderPageReqVO reqVO) {
        return success(tradeOrderQueryService.getOrderSummary(reqVO));
    }

    @GetMapping("/get-detail")
    @Operation(summary = "获得交易订单详情")
    @Parameter(name = "id", description = "订单编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<TradeOrderDetailRespVO> getOrderDetail(@RequestParam("id") Long id) {
        // 查询订单
        TradeOrderDO order = tradeOrderQueryService.getOrder(id);
        System.out.print("指派门店ID:"+order);
        if (order == null) {
            return success(null);
        }
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(id);


        // 2.1.1 提取所有spuId
        Set<Long> spuIds = orderItems.stream()
                .map(TradeOrderItemDO::getSpuId)
                .collect(Collectors.toSet());

        // 2.1.2 批量查询spu与document的关联关系
        List<ProductPrintDocumentSpuDO> spuDocumentRelations = tradeOrderQueryService.selectBySpuIds(spuIds);

        // 2.1.3 构建spuId到documentId的映射
        Map<Long, Long> spuToDocumentIdMap = spuDocumentRelations.stream()
                .collect(Collectors.toMap(
                        ProductPrintDocumentSpuDO::getSpuId,
                        ProductPrintDocumentSpuDO::getDocumentId,
                        (existing, replacement) -> existing // 处理重复spuId，保留第一个
                ));

        // 2.1.4 提取所有documentId
        Set<Long> documentIds = new HashSet<>(spuToDocumentIdMap.values());

        // 2.1.5 批量查询打印文档信息
        Map<Long, ProductPrintDocumentDO> documentMap = documentIds.isEmpty() ? Collections.emptyMap() :
                productPrintDocumentMapper.selectByUserIdAndIds(order.getUserId(), new ArrayList<>(documentIds))
                        .stream()
                        .collect(Collectors.toMap(ProductPrintDocumentDO::getId, Function.identity()));

        // 2.1.6 构建订单项ID与打印文档的映射Map
        Map<Long, ProductPrintDocumentDO> orderItemIdToDocumentMap = new HashMap<>(orderItems.size());
        for (TradeOrderItemDO item : orderItems) {
            Long documentId = spuToDocumentIdMap.get(item.getSpuId());
            ProductPrintDocumentDO printDocument = documentMap.get(documentId);
            if (printDocument != null) {
                orderItemIdToDocumentMap.put(item.getId(), printDocument); // 用订单项ID作为key关联
            }
        }

        // 拼接数据
        MemberUserRespDTO user = memberUserApi.getUser(order.getUserId());
        MemberUserRespDTO brokerageUser = order.getBrokerageUserId() != null ?
                memberUserApi.getUser(order.getBrokerageUserId()) : null;
        List<TradeOrderLogDO> orderLogs = tradeOrderLogService.getOrderLogListByOrderId(id);
        return success(TradeOrderConvert.INSTANCE.convert(order, orderItems, orderLogs, user, brokerageUser,orderItemIdToDocumentMap));
    }

    @GetMapping("/get-express-track-list")
    @Operation(summary = "获得交易订单的物流轨迹")
    @Parameter(name = "id", description = "交易订单编号")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<List<?>> getOrderExpressTrackList(@RequestParam("id") Long id) {
        return success(TradeOrderConvert.INSTANCE.convertList02(
                tradeOrderQueryService.getExpressTrackList(id)));
    }

    @PutMapping("/delivery")
    @Operation(summary = "订单发货")
    @PreAuthorize("@ss.hasPermission('trade:order:update')")
    public CommonResult<Boolean> deliveryOrder(@RequestBody TradeOrderDeliveryReqVO deliveryReqVO) {
        tradeOrderUpdateService.deliveryOrder(deliveryReqVO);
        return success(true);
    }

    @PutMapping("/update-remark")
    @Operation(summary = "订单备注")
    @PreAuthorize("@ss.hasPermission('trade:order:update')")
    public CommonResult<Boolean> updateOrderRemark(@RequestBody TradeOrderRemarkReqVO reqVO) {
        tradeOrderUpdateService.updateOrderRemark(reqVO);
        return success(true);
    }

    @PutMapping("/update-appoint-store-id")
    @Operation(summary = "订单指定门店")
    @PreAuthorize("@ss.hasPermission('trade:order:update')")
    public CommonResult<Boolean> updateOrderAppointStoreId(@RequestBody TradeOrderAppointStoreIdReqVO reqVO) {
        tradeOrderUpdateService.updateOrderAppointStoreId(reqVO);
        return success(true);
    }

    @PutMapping("/update-price")
    @Operation(summary = "订单调价")
    @PreAuthorize("@ss.hasPermission('trade:order:update')")
    public CommonResult<Boolean> updateOrderPrice(@RequestBody TradeOrderUpdatePriceReqVO reqVO) {
        tradeOrderUpdateService.updateOrderPrice(reqVO);
        return success(true);
    }

    @PutMapping("/update-address")
    @Operation(summary = "修改订单收货地址")
    @PreAuthorize("@ss.hasPermission('trade:order:update')")
    public CommonResult<Boolean> updateOrderAddress(@RequestBody TradeOrderUpdateAddressReqVO reqVO) {
        tradeOrderUpdateService.updateOrderAddress(reqVO);
        return success(true);
    }

    @PutMapping("/pick-up-by-id")
    @Operation(summary = "订单核销")
    @Parameter(name = "id", description = "交易订单编号")
    @PreAuthorize("@ss.hasPermission('trade:order:pick-up')")
    public CommonResult<Boolean> pickUpOrderById(@RequestParam("id") Long id) {
        tradeOrderUpdateService.pickUpOrderByAdmin(getLoginUserId(), id);
        return success(true);
    }

    @PutMapping("/pick-up-by-verify-code")
    @Operation(summary = "订单核销")
    @Parameter(name = "pickUpVerifyCode", description = "自提核销码")
    @PreAuthorize("@ss.hasPermission('trade:order:pick-up')")
    public CommonResult<Boolean> pickUpOrderByVerifyCode(@RequestParam("pickUpVerifyCode") String pickUpVerifyCode) {
        tradeOrderUpdateService.pickUpOrderByAdmin(getLoginUserId(), pickUpVerifyCode);
        return success(true);
    }

    @GetMapping("/get-by-pick-up-verify-code")
    @Operation(summary = "查询核销码对应的订单")
    @Parameter(name = "pickUpVerifyCode", description = "自提核销码")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<TradeOrderDetailRespVO> getByPickUpVerifyCode(@RequestParam("pickUpVerifyCode") String pickUpVerifyCode) {
        TradeOrderDO tradeOrder = tradeOrderUpdateService.getByPickUpVerifyCode(pickUpVerifyCode);
        return success(TradeOrderConvert.INSTANCE.convert2(tradeOrder, null));
    }

}
