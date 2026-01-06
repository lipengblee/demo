package com.star.lp.module.trade.controller.admin.order;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.lop.open.api.sdk.domain.jdcloudprint.PullDataService.pullData.PullDataReqDTO;
import com.lop.open.api.sdk.domain.jdcloudprint.PullDataService.pullData.PullDataRespDTO;
import com.lop.open.api.sdk.domain.jdcloudprint.PullDataService.pullData.WayBillInfo;
import com.lop.open.api.sdk.response.ECAP.EcapV1OrdersCreateLopResponse;
import com.lop.open.api.sdk.response.ECAP.EcapV1OrdersPrecheckLopResponse;
import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.ip.core.utils.AreaUtils;
import com.star.lp.module.member.api.user.MemberUserApi;
import com.star.lp.module.member.api.user.dto.MemberUserRespDTO;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentDO;
import com.star.lp.module.product.dal.dataobject.print.ProductPrintDocumentSpuDO;
import com.star.lp.module.product.dal.mysql.print.ProductPrintDocumentMapper;
import com.star.lp.module.trade.controller.admin.order.vo.*;
import com.star.lp.module.trade.dal.redis.RedisKeyConstants;
import com.star.lp.module.trade.service.logistics.jd.JDLogisticsService;
import com.lop.open.api.sdk.domain.jdcloudprint.TemplateService.getTemplates.QueryTemplatesDto;
import com.lop.open.api.sdk.domain.jdcloudprint.TemplateService.getTemplates.TemplateListResultDto;
import com.lop.open.api.sdk.LopException;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
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
    @Resource
    private JDLogisticsService jdLogisticsService;

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

    @GetMapping("/appoint-store-page")
    @Operation(summary = "获得指派门店的交易订单分页")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<PageResult<TradeOrderPageItemRespVO>> getAppointStoreOrderPage(TradeOrderAppointPageReqVO reqVO) {
        // 查询订单
        PageResult<TradeOrderDO> pageResult = tradeOrderQueryService.getAppointStoreOrderPage(reqVO);
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

    @GetMapping("/appoint-store-summary")
    @Operation(summary = "获得指派门店的交易订单统计")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<TradeOrderSummaryRespVO> getAppointStoreOrderSummary(TradeOrderAppointPageReqVO reqVO) {
        return success(tradeOrderQueryService.getAppointStoreOrderSummary(reqVO));
    }

    @GetMapping("/get-detail")
    @Operation(summary = "获得交易订单详情")
    @Parameter(name = "id", description = "订单编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<TradeOrderDetailRespVO> getOrderDetail(@RequestParam("id") Long id) {
        // 查询订单
        TradeOrderDO order = tradeOrderQueryService.getOrder(id);
        System.out.print("指派门店ID:" + order);
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
        return success(TradeOrderConvert.INSTANCE.convert(order, orderItems, orderLogs, user, brokerageUser, orderItemIdToDocumentMap));
    }

    @GetMapping("/get-express-track-list")
    @Operation(summary = "获得交易订单的物流轨迹")
    @Parameter(name = "id", description = "交易订单编号")
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<List<?>> getOrderExpressTrackList(@RequestParam("id") Long id) {
        return success(TradeOrderConvert.INSTANCE.convertList02(
                tradeOrderQueryService.getExpressTrackList(id)));
    }

    @GetMapping("/get-by-logistics-no")
    @Operation(summary = "通过物流单号查询订单")
    @Parameter(name = "logisticsNo", description = "物流单号", required = true)
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<List<TradeOrderPageItemRespVO>> getOrdersByLogisticsNo(@RequestParam("logisticsNo") String logisticsNo) {
        // 查询订单
        List<TradeOrderDO> orders = tradeOrderQueryService.getOrdersByLogisticsNo(logisticsNo);
        if (CollUtil.isEmpty(orders)) {
            return success(Collections.emptyList());
        }

        // 查询用户信息
        Set<Long> userIds = CollUtil.unionDistinct(convertList(orders, TradeOrderDO::getUserId),
                convertList(orders, TradeOrderDO::getBrokerageUserId, Objects::nonNull));
        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(userIds);
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                convertSet(orders, TradeOrderDO::getId));
        // 最终组合
        return success(TradeOrderConvert.INSTANCE.convertList(orders, orderItems, userMap));
    }

    @GetMapping("/get-latest-by-logistics-no")
    @Operation(summary = "通过物流单号查询最新的一条订单")
    @Parameter(name = "logisticsNo", description = "物流单号", required = true)
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    public CommonResult<TradeOrderPageItemRespVO> getLatestOrderByLogisticsNo(@RequestParam("logisticsNo") String logisticsNo) {
        // 查询订单
        TradeOrderDO order = tradeOrderQueryService.getLatestOrderByLogisticsNo(logisticsNo);
        if (order == null) {
            return success(null);
        }

        // 查询用户信息
        Set<Long> userIds = CollUtil.unionDistinct(Arrays.asList(order.getUserId()),
                order.getBrokerageUserId() != null ? Arrays.asList(order.getBrokerageUserId()) : Collections.emptyList());
        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(userIds);
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                Collections.singleton(order.getId()));
        // 最终组合
        return success(TradeOrderConvert.INSTANCE.convertList(Arrays.asList(order), orderItems, userMap).get(0));
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

    @GetMapping("/get-print-templates")
    @Operation(summary = "获取京东物流打印模板列表")
    @Parameter(name = "storeId", description = "门店ID", required = true)
    @Parameter(name = "templateType", description = "模板类型：1.自定义区；2.自定义模板；不传则获取全部类型模板", required = false)
    @Parameter(name = "cpCode", description = "承运商编码，默认为JD", required = false)
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    @Cacheable(cacheNames = RedisKeyConstants.PRINT_TEMPLATES, 
              key = "#storeId + ':' + #templateType + ':' + #cpCode",
              unless = "#result == null")
    public CommonResult<TemplateListResultDto> getPrintTemplates(
            @RequestParam("storeId") Long storeId,
            @RequestParam(value = "templateType", required = false, defaultValue = "1") Integer templateType,
            @RequestParam(value = "cpCode", required = false, defaultValue = "JD") String cpCode) {
        try {
            log.info("获取京东物流打印模板列表，门店ID: {}, 模板类型: {}, 承运商编码: {}", storeId, templateType, cpCode);

            // 1. 参数验证
            if (storeId == null || storeId <= 0) {
                return CommonResult.error(400, "门店ID不能为空且必须大于0");
            }

            // 2. 构建请求参数
            QueryTemplatesDto requestDto = new QueryTemplatesDto();
            requestDto.setTemplateType(templateType);
            requestDto.setCpCode(cpCode);

            // 3. 调用服务获取模板列表
            TemplateListResultDto result = jdLogisticsService.getPrintTemplates(requestDto, storeId);

            log.info("获取京东物流打印模板列表成功，门店ID: {}, 返回模板数量: {}",
                    storeId, result != null && result.getCode() != null && result.getData() != null ?
                            (result.getData().getStandardTemplateList() != null ?
                                    result.getData().getStandardTemplateList().size() : 0) : 0);

            return success(result);
        } catch (LopException e) {
            log.error("获取京东物流打印模板列表失败，门店ID: {}", storeId, e);
            return CommonResult.error(500, "获取京东物流打印模板列表失败: " + e.getMessage());
        } catch (Exception e) {
            log.error("获取京东物流打印模板列表异常，门店ID: {}", storeId, e);
            return CommonResult.error(500, "获取京东物流打印模板列表异常: " + e.getMessage());
        }
    }

    @GetMapping("/get-order-by-no-print")
    @Operation(summary = "通过订单号查询订单并打印物流面单")
    @Parameter(name = "orderNo", description = "订单号", required = true)
    @PreAuthorize("@ss.hasPermission('trade:order:query')")
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<TradeOrderPageItemRespVO> getOrderByNoPrint(@RequestParam("orderNo") String orderNo) {
        try {
            log.info("开始处理订单打印请求，订单号: {}", orderNo);
            
            // 参数验证
            validateOrderNo(orderNo);
            
            // 查询并验证订单
            TradeOrderDO order = validateAndGetOrder(orderNo);
            if (order == null) {
                log.info("订单不存在，订单号: {}", orderNo);
                return CommonResult.error(400, "订单不存在");
            }
            
            // 检查物流单号
            checkLogisticsNo(order, orderNo);
            
            // 创建物流订单
            String logisticsNo = createLogisticsOrder(order, orderNo);
            
            // 获取打印数据
            PullDataRespDTO printData = getPrintData(orderNo, logisticsNo, order.getAppointStoreId());
            
            // 组装并返回结果
            CommonResult<TradeOrderPageItemRespVO> result = assembleResponse(order,printData);
            log.info("订单打印处理完成，订单号: {}", orderNo);
            return result;
        } catch (LopException e) {
            log.error("京东物流API调用失败，订单号: {}", orderNo, e);
            return CommonResult.error(400, "京东物流API调用失败: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("参数验证失败，订单号: {}", orderNo, e);
            return CommonResult.error(400, e.getMessage());
        } catch (Exception e) {
            log.error("处理订单打印失败，订单号: {}", orderNo, e);
            return CommonResult.error(500, "处理订单打印失败: " + e.getMessage());
        }
    }
    
    /**
     * 验证订单号
     */
    private void validateOrderNo(String orderNo) {
        if (orderNo == null || orderNo.trim().isEmpty()) {
            throw new IllegalArgumentException("订单号不能为空");
        }
    }
    
    /**
     * 查询并验证订单
     */
    private TradeOrderDO validateAndGetOrder(String orderNo) {
        TradeOrderDO order = tradeOrderQueryService.getOrderByNo(orderNo);
        if (order == null) {
            return null;
        }
        
        // 验证门店ID
        if (order.getAppointStoreId() == null || order.getAppointStoreId() <= 0) {
            throw new IllegalArgumentException("订单未指定门店，无法打印物流面单");
        }
        
        return order;
    }
    
    /**
     * 检查物流单号是否已存在
     */
    private void checkLogisticsNo(TradeOrderDO order, String orderNo) {
        String logisticsNo = order.getLogisticsNo();
        if (logisticsNo != null && !logisticsNo.trim().isEmpty()) {
            log.info("物流单号已存在，无法打印，订单号: {}", orderNo);
            throw new IllegalArgumentException("物流单号已存在，无法打印物流面单");
        }
    }
    
    /**
     * 创建物流订单
     */
    private String createLogisticsOrder(TradeOrderDO order, String orderNo) throws LopException {
        // 创建验证地址参数,京东快递下单前置校验
        JDLogisticsOrderParamReqVo param = new JDLogisticsOrderParamReqVo();
        param.setStoreId(order.getAppointStoreId());
        param.setReceiverName(order.getReceiverName());
        param.setReceiverPhone(order.getReceiverMobile());
        String fullAddress = AreaUtils.format(order.getReceiverAreaId()) + order.getReceiverDetailAddress();
        param.setReceiverAddress(fullAddress);
        
        log.debug("京东物流测试请求参数 - 订单号: {}", orderNo);
        log.debug("门店ID: {}, 收件人: {}, 电话: {}, 地址: {}", 
                param.getStoreId(), param.getReceiverName(), param.getReceiverPhone(), fullAddress);
        
        // 调用前置校验方法
        EcapV1OrdersPrecheckLopResponse response = jdLogisticsService.preCheckOrder(param);
        log.info("京东物流地址验证响应 - 订单号: {}, code: {}, result: {}", 
                orderNo, response.getCode(), JSONUtil.toJsonStr(response.getResult()));
        
        // 校验地址是否验证通过
        if (response.getResult() == null || !"0".equals(response.getCode()) || !response.getResult().getSuccess()) {
            String errorMsg = "京东物流地址验证失败: " + (response.getResult() != null ? response.getResult().getMsg() : "未知错误");
            tradeOrderUpdateService.updateOrderStatusToFailed(order.getId(), errorMsg);
            log.error("京东物流地址验证失败，订单号: {}", orderNo);
            throw new IllegalArgumentException(errorMsg);
        }
        
        //设置货品信息（使用默认值）
        param.setOrderId(order.getNo());
        param.setCargoName("打印文件");
        param.setCargoQuantity(1); //一个包裹
        param.setCargoWeight("0.5");
        param.setCargoLength("0.1");
        param.setCargoWidth("29.7");
        param.setCargoHeight("21");
        
        log.info("京东物流下单请求 - 订单号: {}, 参数: {}", orderNo, JSONUtil.toJsonStr(param));
        
        // 调用京东物流下单API
        EcapV1OrdersCreateLopResponse createResponse = jdLogisticsService.createOrder(param);
        String logisticsDetail = JSONUtil.toJsonStr(createResponse);
        log.info("京东物流下单响应 - 订单号: {}, 结果: {}", orderNo, logisticsDetail);
        
        // 处理下单结果
        if (createResponse == null || !"0".equals(createResponse.getCode()) || createResponse.getResult() == null) {
            String errorMsg = "京东物流下单失败: " + (createResponse != null ? createResponse.getMsg() : "未知错误");
            log.error("京东物流下单失败，订单号: {}", orderNo);
            tradeOrderUpdateService.updateOrderStatusToFailedWithDetail(order.getId(), errorMsg, logisticsDetail);
            throw new IllegalArgumentException(errorMsg);
        }
        
        String waybillCode = createResponse.getResult().getData().getWaybillCode();
        log.info("京东物流下单成功，订单号: {}, 物流单号: {}", orderNo, waybillCode);
        
        // 更新订单物流信息
        boolean updateResult = tradeOrderUpdateService.updateOrderLogisticsInfo(order.getId(), waybillCode, logisticsDetail, order.getAppointStoreId());
        if (!updateResult) {
            String errorMsg = "京东物流下单数据回写错误";
            log.error("{}, 订单号: {}", errorMsg, orderNo);
            throw new IllegalArgumentException(errorMsg);
        }
        
        return waybillCode;
    }
    
    /**
     * 获取打印数据
     */
    private PullDataRespDTO getPrintData(String orderNo, String logisticsNo, Long storeId) throws LopException {
        PullDataReqDTO requestDto = new PullDataReqDTO();
        requestDto.setCpCode("JD");
        requestDto.setObjectId(orderNo);
        
        // 设置打印数据：创建WayBillInfo对象列表
        List<WayBillInfo> wayBillInfos = new ArrayList<>();
        WayBillInfo wayBillInfo = new WayBillInfo();
        wayBillInfo.setJdWayBillCode(logisticsNo); // 设置京东物流运单号
        wayBillInfo.setPopFlag(0); // 设置非京东商城订单
        wayBillInfo.setOrderNo(orderNo); // 设置销售平台订单号
        wayBillInfos.add(wayBillInfo);
        requestDto.setWayBillInfos(wayBillInfos);
        
        PullDataRespDTO printDataResult = jdLogisticsService.getPrintData(requestDto, storeId);
        log.info("获取到京东物流打印数据 - 订单号: {}, 结果: {}", orderNo, JSONUtil.toJsonStr(printDataResult));
        return printDataResult;
    }
    
    /**
     * 组装响应结果
     */
    private CommonResult<TradeOrderPageItemRespVO> assembleResponse(TradeOrderDO order,PullDataRespDTO printData) {
        // 查询用户信息
        Set<Long> userIds = CollUtil.unionDistinct(Arrays.asList(order.getUserId()),
                order.getBrokerageUserId() != null ? Arrays.asList(order.getBrokerageUserId()) : Collections.emptyList());
        Map<Long, MemberUserRespDTO> userMap = memberUserApi.getUserMap(userIds);
        // 查询订单项
        List<TradeOrderItemDO> orderItems = tradeOrderQueryService.getOrderItemListByOrderId(
                Collections.singleton(order.getId()));
        // 最终组合
        return success(TradeOrderConvert.INSTANCE.convertList(Arrays.asList(order), orderItems, userMap).get(0));
    }


}