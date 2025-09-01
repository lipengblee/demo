package com.star.lp.module.trade.controller.app.order;

import cn.hutool.core.collection.CollUtil;
import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.module.pay.api.notify.dto.PayOrderNotifyReqDTO;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentSpuDO;
import com.star.lp.module.product.dal.mysql.print.ProductPrintDocumentMapper;
import com.star.lp.module.product.dal.mysql.print.ProductPrintDocumentSpuMapper;
import com.star.lp.module.trade.controller.app.order.vo.*;
import com.star.lp.module.trade.controller.app.order.vo.item.AppTradeOrderItemCommentCreateReqVO;
import com.star.lp.module.trade.controller.app.order.vo.item.AppTradeOrderItemRespVO;
import com.star.lp.module.trade.convert.order.TradeOrderConvert;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryExpressDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderDO;
import com.star.lp.module.trade.dal.dataobject.order.TradeOrderItemDO;
import com.star.lp.module.trade.enums.order.TradeOrderStatusEnum;
import com.star.lp.module.trade.framework.order.config.TradeOrderProperties;
import com.star.lp.module.trade.service.aftersale.AfterSaleService;
import com.star.lp.module.trade.service.delivery.DeliveryExpressService;
import com.star.lp.module.trade.service.order.TradeOrderQueryService;
import com.star.lp.module.trade.service.order.TradeOrderUpdateService;
import com.star.lp.module.trade.service.price.TradePriceService;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.star.lp.framework.common.pojo.CommonResult.success;
import static com.star.lp.framework.common.util.collection.CollectionUtils.convertSet;
import static com.star.lp.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.star.lp.module.trade.enums.PrintErrorCodeConstants.PRINT_PRICE_CONFIG_NOT_EXISTS;
import static com.star.lp.module.trade.enums.PrintErrorCodeConstants.TRADE_ORDER_PRINT_ITEMS_EMPTY;

@Tag(name = "用户 App - 交易订单")
@RestController
@RequestMapping("/trade/order")
@Validated
@Slf4j
public class AppTradeOrderController {

    @Resource
    private TradeOrderUpdateService tradeOrderUpdateService;
    @Resource
    private TradeOrderQueryService tradeOrderQueryService;
    @Resource
    private DeliveryExpressService deliveryExpressService;
    @Resource
    private AfterSaleService afterSaleService;
    @Resource
    private TradePriceService priceService;

    @Resource
    private TradeOrderProperties tradeOrderProperties;

    @Resource
    private ProductPrintDocumentSpuMapper productPrintDocumentSpuMapper;

    @Resource
    private ProductPrintDocumentMapper productPrintDocumentMapper;

    @GetMapping("/settlement")
    @Operation(summary = "获得订单结算信息")
    public CommonResult<AppTradeOrderSettlementRespVO> settlementOrder(@Valid AppTradeOrderSettlementReqVO settlementReqVO) {
        return success(tradeOrderUpdateService.settlementOrder(getLoginUserId(), settlementReqVO));
    }

    @GetMapping("/settlement-print")
    @Operation(summary = "获得打印订单结算信息")
    public CommonResult<AppTradeOrderSettlementRespVO> settlementOrderPrint(@Valid AppTradeOrderSettlementPrintReqVO settlementReqVO) {
        // 1. 基础参数校验
        if (settlementReqVO == null) {
            return CommonResult.error(PRINT_PRICE_CONFIG_NOT_EXISTS);
        }

        // 2. 获取打印文档信息并校验
        List<AppTradeOrderSettlementPrintReqVO.Item> printItems = settlementReqVO.getItems();
        if (CollUtil.isEmpty(printItems)) {
            return CommonResult.error(TRADE_ORDER_PRINT_ITEMS_EMPTY);
        }

        return success(tradeOrderUpdateService.settlementOrderPrint(getLoginUserId(), settlementReqVO));
    }

    @GetMapping("/settlement-product")
    @Operation(summary = "获得商品结算信息", description = "用于商品列表、商品详情，获得参与活动后的价格信息")
    @Parameter(name = "spuIds", description = "商品 SPU 编号数组")
    @PermitAll
    public CommonResult<List<AppTradeProductSettlementRespVO>> settlementProduct(@RequestParam("spuIds") List<Long> spuIds) {
        return success(priceService.calculateProductPrice(getLoginUserId(), spuIds));
    }

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public CommonResult<AppTradeOrderCreateRespVO> createOrder(@Valid @RequestBody AppTradeOrderCreateReqVO createReqVO) {
        TradeOrderDO order = tradeOrderUpdateService.createOrder(getLoginUserId(), createReqVO);
        return success(new AppTradeOrderCreateRespVO().setId(order.getId()).setPayOrderId(order.getPayOrderId()));
    }

    @PostMapping("/update-paid")
    @Operation(summary = "更新订单为已支付") // 由 pay-module 支付服务，进行回调，可见 PayNotifyJob
    @PermitAll
    public CommonResult<Boolean> updateOrderPaid(@RequestBody PayOrderNotifyReqDTO notifyReqDTO) {
        tradeOrderUpdateService.updateOrderPaid(Long.valueOf(notifyReqDTO.getMerchantOrderId()),
                notifyReqDTO.getPayOrderId());
        return success(true);
    }

    @GetMapping("/get-detail")
    @Operation(summary = "获得交易订单")
    @Parameters({
            @Parameter(name = "id", description = "交易订单编号"),
            @Parameter(name = "sync", description = "是否同步支付状态", example = "true")
    })
    public CommonResult<AppTradeOrderDetailRespVO> getOrderDetail(@RequestParam("id") Long id,
                                                                  @RequestParam(value = "sync", required = false) Boolean sync) {
        // 1.1 查询订单
        TradeOrderDO order = tradeOrderQueryService.getOrder(getLoginUserId(), id);
        if (order == null) {
            return success(null);
        }
        // 1.2 sync 仅在等待支付
        if (Boolean.TRUE.equals(sync)
                && TradeOrderStatusEnum.isUnpaid(order.getStatus()) && !order.getPayStatus()) {
            tradeOrderUpdateService.syncOrderPayStatusQuietly(order.getId(), order.getPayOrderId());
            // 重新查询，因为同步后，可能会有变化
            order = tradeOrderQueryService.getOrder(id);
        }

        // 2.1 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(order.getId());

        // 新增逻辑：查询打印文档信息
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
                productPrintDocumentMapper.selectByUserIdAndIds(getLoginUserId(), new ArrayList<>(documentIds))
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

        // 2.2 查询物流公司
        DeliveryExpressDO express = order.getLogisticsId() != null && order.getLogisticsId() > 0 ?
                deliveryExpressService.getDeliveryExpress(order.getLogisticsId()) : null;

        // 2.3 组合结果：将Map传入转换方法，设置到VO中
        AppTradeOrderDetailRespVO result = TradeOrderConvert.INSTANCE
                .convert02(order, orderItems, tradeOrderProperties, express, orderItemIdToDocumentMap);
        return success(result);
    }

    @GetMapping("/get-express-track-list")
    @Operation(summary = "获得交易订单的物流轨迹")
    @Parameter(name = "id", description = "交易订单编号")
    public CommonResult<List<AppOrderExpressTrackRespDTO>> getOrderExpressTrackList(@RequestParam("id") Long id) {
        return success(TradeOrderConvert.INSTANCE.convertList02(
                tradeOrderQueryService.getExpressTrackList(id, getLoginUserId())));
    }

    @GetMapping("/page")
    @Operation(summary = "获得交易订单分页")
    public CommonResult<PageResult<AppTradeOrderPageItemRespVO>> getOrderPage(AppTradeOrderPageReqVO reqVO) {
        // 查询订单
        PageResult<TradeOrderDO> pageResult = tradeOrderQueryService.getOrderPage(getLoginUserId(), reqVO);
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                convertSet(pageResult.getList(), TradeOrderDO::getId));
        // 最终组合
        return success(TradeOrderConvert.INSTANCE.convertPage02(pageResult, orderItems));
    }

    @GetMapping("/get-count")
    @Operation(summary = "获得交易订单数量")
    public CommonResult<Map<String, Long>> getOrderCount() {
        Map<String, Long> orderCount = Maps.newLinkedHashMapWithExpectedSize(5);
        // 全部
        orderCount.put("allCount", tradeOrderQueryService.getOrderCount(getLoginUserId(), null, null));
        // 待付款（未支付）
        orderCount.put("unpaidCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.UNPAID.getStatus(), null));
        // 待发货
        orderCount.put("undeliveredCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.UNDELIVERED.getStatus(), null));
        // 待收货
        orderCount.put("deliveredCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.DELIVERED.getStatus(), null));
        // 待评价
        orderCount.put("uncommentedCount", tradeOrderQueryService.getOrderCount(getLoginUserId(),
                TradeOrderStatusEnum.COMPLETED.getStatus(), false));
        // 售后数量
        orderCount.put("afterSaleCount", afterSaleService.getApplyingAfterSaleCount(getLoginUserId()));
        return success(orderCount);
    }

    @PutMapping("/receive")
    @Operation(summary = "确认交易订单收货")
    @Parameter(name = "id", description = "交易订单编号")
    public CommonResult<Boolean> receiveOrder(@RequestParam("id") Long id) {
        tradeOrderUpdateService.receiveOrderByMember(getLoginUserId(), id);
        return success(true);
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "取消交易订单")
    @Parameter(name = "id", description = "交易订单编号")
    public CommonResult<Boolean> cancelOrder(@RequestParam("id") Long id) {
        tradeOrderUpdateService.cancelOrderByMember(getLoginUserId(), id);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除交易订单")
    @Parameter(name = "id", description = "交易订单编号")
    public CommonResult<Boolean> deleteOrder(@RequestParam("id") Long id) {
        tradeOrderUpdateService.deleteOrder(getLoginUserId(), id);
        return success(true);
    }

    // ========== 订单项 ==========

    @GetMapping("/item/get")
    @Operation(summary = "获得交易订单项")
    @Parameter(name = "id", description = "交易订单项编号")
    public CommonResult<AppTradeOrderItemRespVO> getOrderItem(@RequestParam("id") Long id) {
        TradeOrderItemDO item = tradeOrderQueryService.getOrderItem(getLoginUserId(), id);
        return success(TradeOrderConvert.INSTANCE.convert03(item));
    }

    @PostMapping("/item/create-comment")
    @Operation(summary = "创建交易订单项的评价")
    public CommonResult<Long> createOrderItemComment(@RequestBody AppTradeOrderItemCommentCreateReqVO createReqVO) {
        return success(tradeOrderUpdateService.createOrderItemCommentByMember(getLoginUserId(), createReqVO));
    }

}
