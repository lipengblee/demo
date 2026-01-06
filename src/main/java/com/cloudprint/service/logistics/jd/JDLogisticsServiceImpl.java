package com.star.lp.module.trade.service.logistics.jd;

import com.lop.open.api.sdk.LopException;
import com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.CommonCargoInfo;
import com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.CommonProductInfo;
import com.lop.open.api.sdk.domain.jdcloudprint.TemplateService.getTemplates.QueryTemplatesDto;
import com.lop.open.api.sdk.domain.jdcloudprint.TemplateService.getTemplates.TemplateListResultDto;
import com.lop.open.api.sdk.request.ECAP.EcapV1OrdersCreateLopRequest;
import com.lop.open.api.sdk.request.ECAP.EcapV1OrdersPrecheckLopRequest;
import com.lop.open.api.sdk.request.jdcloudprint.CloudPrintGetTemplatesLopRequest;
import com.lop.open.api.sdk.response.ECAP.EcapV1OrdersCreateLopResponse;
import com.lop.open.api.sdk.response.ECAP.EcapV1OrdersPrecheckLopResponse;
import com.star.lp.module.trade.controller.admin.order.vo.JDLogisticsOrderParamReqVo;
import com.star.lp.module.trade.dal.dataobject.delivery.DeliveryPickUpStoreLogisticsConfigDO;
import com.star.lp.module.trade.service.delivery.DeliveryPickUpStoreLogisticsConfigService;
import com.star.lp.module.trade.service.logistics.jd.client.JDLogisticsClient;
import com.star.lp.module.trade.service.logistics.jd.config.JDLogisticsConfig;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.lop.open.api.sdk.domain.jdcloudprint.PullDataService.pullData.PullDataReqDTO;
import com.lop.open.api.sdk.domain.jdcloudprint.PullDataService.pullData.PullDataRespDTO;
import com.lop.open.api.sdk.request.jdcloudprint.PullDataServicePullDataLopRequest;
import com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCheckPreCreateOrderV1.CommonCreateOrderRequest;
import com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCheckPreCreateOrderV1.Contact;

import java.math.BigDecimal;
import java.util.*;


/**
 * 京东物流服务实现类
 *
 * @author star
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JDLogisticsServiceImpl implements JDLogisticsService {

    private final JDLogisticsClient jdLogisticsClient;

    @Resource
    private DeliveryPickUpStoreLogisticsConfigService deliveryPickUpStoreLogisticsConfigService;

    @Resource
    private JDLogisticsConfig config;

    @Override
    public TemplateListResultDto getPrintTemplates(QueryTemplatesDto requestDto, Long storeId) throws LopException {
        try {
            log.info("获取京东物流打印模板列表，门店ID: {}, 请求参数: {}", storeId, requestDto);

            // 1. 构建具体的API请求对象
            CloudPrintGetTemplatesLopRequest request = new CloudPrintGetTemplatesLopRequest();

            request.setRequest(requestDto);

            // 3. 调用客户端执行请求
            TemplateListResultDto result = jdLogisticsClient.execute(request, storeId).getResult();

            log.info("获取京东物流打印模板列表成功，门店ID: {}, 结果: {}", storeId, result);
            return result;
        } catch (LopException e) {
            log.error("获取京东物流打印模板列表失败，门店ID: {}", storeId, e);
            throw e;
        } catch (Exception e) {
            log.error("获取京东物流打印模板列表异常，门店ID: {}", storeId, e);
            throw new LopException("获取京东物流打印模板列表异常: " + e.getMessage(), e);
        }
    }

    @Override
    public PullDataRespDTO getPrintData(PullDataReqDTO requestDto, Long storeId) throws LopException {
        try {

            // 设置parameters：根据cpCode确定key值
            Map<String, String> parameters = new HashMap<>();
            // cpCode为JDDKYDDJY时，key的值是ewCustomerCode
            parameters.put("ewCustomerCode", config.getCustomerCode()); // 设置商家编码
            requestDto.setParameters(parameters);

            log.info("获取京东物流打印数据，门店ID: {}, 请求参数: {}", storeId, requestDto);

            // 1. 构建具体的API请求对象
            PullDataServicePullDataLopRequest request = new PullDataServicePullDataLopRequest();
            request.setPullDataReqDTO(requestDto);

            // 2. 调用客户端执行请求
            PullDataRespDTO result = jdLogisticsClient.execute(request, storeId).getResult();

            log.info("获取京东物流打印数据成功，门店ID: {}, 结果: {}", storeId, result);
            return result;
        } catch (LopException e) {
            log.error("获取京东物流打印数据失败，门店ID: {}", storeId, e);
            throw e;
        } catch (Exception e) {
            log.error("获取京东物流打印数据异常，门店ID: {}", storeId, e);
            throw new LopException("获取京东物流打印数据异常: " + e.getMessage(), e);
        }
    }

    public EcapV1OrdersPrecheckLopResponse preCheckOrder(JDLogisticsOrderParamReqVo orderParam) throws LopException {
        log.info("开始京东快递下单前置校验，门店ID: {}", orderParam != null ? orderParam.getStoreId() : null);

        // 参数非空校验
        if (orderParam == null) {
            log.error("订单参数不能为空");
            throw new IllegalArgumentException("订单参数不能为空");
        }

        // 验证收件人信息（必填）
        if (orderParam.getReceiverName() == null || orderParam.getReceiverName().trim().isEmpty()) {
            log.error("收件人姓名不能为空");
            throw new IllegalArgumentException("收件人姓名不能为空");
        }
        if (orderParam.getReceiverPhone() == null || orderParam.getReceiverPhone().trim().isEmpty()) {
            log.error("收件人电话不能为空");
            throw new IllegalArgumentException("收件人电话不能为空");
        }
        if (orderParam.getReceiverAddress() == null || orderParam.getReceiverAddress().trim().isEmpty()) {
            log.error("收件人地址不能为空");
            throw new IllegalArgumentException("收件人地址不能为空");
        }

        // 验证电话号码格式（简单验证）
        if (!isValidPhone(orderParam.getReceiverPhone())) {
            log.error("收件人电话格式不正确: {}", orderParam.getReceiverPhone());
            throw new IllegalArgumentException("收件人电话格式不正确");
        }

        // 验证寄件人信息（如果没有配置的话）
        DeliveryPickUpStoreLogisticsConfigDO logisticsConfig = deliveryPickUpStoreLogisticsConfigService.getLogisticsConfigByStoreIdAndLogisticsName(orderParam.getStoreId(), "京东快递");
        if (logisticsConfig == null || (logisticsConfig.getSenderAddress() == null || logisticsConfig.getSenderAddress().trim().isEmpty())) {
            if (orderParam.getSenderName() == null || orderParam.getSenderName().trim().isEmpty()) {
                log.error("寄件人姓名不能为空");
                throw new IllegalArgumentException("寄件人姓名不能为空");
            }
            if (orderParam.getSenderPhone() == null || orderParam.getSenderPhone().trim().isEmpty()) {
                log.error("寄件人电话不能为空");
                throw new IllegalArgumentException("寄件人电话不能为空");
            }
            if (!isValidPhone(orderParam.getSenderPhone())) {
                log.error("寄件人电话格式不正确: {}", orderParam.getSenderPhone());
                throw new IllegalArgumentException("寄件人电话格式不正确");
            }
            if (orderParam.getSenderAddress() == null || orderParam.getSenderAddress().trim().isEmpty()) {
                log.error("寄件人地址不能为空");
                throw new IllegalArgumentException("寄件人地址不能为空");
            }
        }

        try {
            // 创建请求对象
            EcapV1OrdersPrecheckLopRequest request = new EcapV1OrdersPrecheckLopRequest();
            CommonCreateOrderRequest requestDTO = new CommonCreateOrderRequest();

            // 前置校验使用与创建订单相同的参数结构
            // 设置订单来源：1-电商平台的商家
            requestDTO.setOrderOrigin(1);

            // 设置客户编码
            requestDTO.setCustomerCode(config.getCustomerCode());

            // 创建并设置寄件人信息
            Contact senderContact = new Contact();
            // 优先使用物流配置中的寄件人信息，如果没有则使用外部传入的信息
            if (logisticsConfig != null && logisticsConfig.getSenderAddress() != null && !logisticsConfig.getSenderAddress().trim().isEmpty()) {
                log.debug("使用物流配置中的寄件人信息");
                StringBuilder fullAddress = new StringBuilder();
                if (logisticsConfig.getSenderProvince() != null)
                    fullAddress.append(logisticsConfig.getSenderProvince());
                if (logisticsConfig.getSenderCity() != null)
                    fullAddress.append(logisticsConfig.getSenderCity());
                if (logisticsConfig.getSenderDistrict() != null)
                    fullAddress.append(logisticsConfig.getSenderDistrict());
                if (logisticsConfig.getSenderAddress() != null)
                    fullAddress.append(logisticsConfig.getSenderAddress());

                senderContact.setFullAddress(fullAddress.toString());
                senderContact.setName(logisticsConfig.getSenderName());
                senderContact.setMobile(logisticsConfig.getSenderPhone());
            } else {
                log.debug("使用外部传入的寄件人信息");
                senderContact.setFullAddress(orderParam.getSenderAddress());
                senderContact.setName(orderParam.getSenderName());
                senderContact.setMobile(orderParam.getSenderPhone());
            }
            requestDTO.setSenderContact(senderContact);

            // 创建并设置收件人信息
            Contact receiverContact = new Contact();
            receiverContact.setFullAddress(orderParam.getReceiverAddress());
            receiverContact.setName(orderParam.getReceiverName());
            receiverContact.setMobile(orderParam.getReceiverPhone());
            requestDTO.setReceiverContact(receiverContact);

            request.setRequest(requestDTO);
            log.debug("前置校验请求参数构造完成，准备调用API");

            // 执行请求
            EcapV1OrdersPrecheckLopResponse response = jdLogisticsClient.execute(request, orderParam.getStoreId());
            log.info("京东快递下单前置校验完成，结果: 成功");
            return response;
        } catch (LopException e) {
            log.error("京东快递下单前置校验失败，错误信息: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("京东快递下单前置校验发生未知错误: {}", e.getMessage(), e);
            throw new LopException("前置校验失败: " + e.getMessage(), e);
        }
    }

    /**
     * 简单验证电话号码格式
     *
     * @param phone 电话号码
     * @return 是否有效
     */
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        // 简单验证：11位数字，以1开头（适用于中国大陆手机号）
        return phone.matches("^1[3-9]\\d{9}$");
    }


    public EcapV1OrdersCreateLopResponse createOrder(JDLogisticsOrderParamReqVo orderParam) throws LopException {

        // 创建请求对象
        EcapV1OrdersCreateLopRequest request = new EcapV1OrdersCreateLopRequest();
        com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.CommonCreateOrderRequest requestDTO = new com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.CommonCreateOrderRequest();

        // 设置订单ID
        requestDTO.setOrderId(orderParam.getOrderId());

        // 设置订单来源：1-电商平台的商家
        requestDTO.setOrderOrigin(1);

        // 设置客户编码
        requestDTO.setCustomerCode(config.getCustomerCode());

        // 设置付款方式：3-月结
        requestDTO.setSettleType(3);

        // 获取物流配置
        DeliveryPickUpStoreLogisticsConfigDO logisticsConfig = deliveryPickUpStoreLogisticsConfigService.getLogisticsConfigByStoreIdAndLogisticsName(orderParam.getStoreId(), "京东快递");

        // 创建并设置寄件人信息
        com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.Contact senderContact = new com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.Contact();
        // 优先使用物流配置中的寄件人信息，如果没有则使用外部传入的信息
        if (logisticsConfig != null && !logisticsConfig.getSenderAddress().isEmpty()) {
            log.debug("使用物流配置中的寄件人信息");
            StringBuilder fullAddress = new StringBuilder();
            if (logisticsConfig.getSenderProvince() != null)
                fullAddress.append(logisticsConfig.getSenderProvince());
            if (logisticsConfig.getSenderCity() != null)
                fullAddress.append(logisticsConfig.getSenderCity());
            if (logisticsConfig.getSenderDistrict() != null)
                fullAddress.append(logisticsConfig.getSenderDistrict());
            if (logisticsConfig.getSenderAddress() != null)
                fullAddress.append(logisticsConfig.getSenderAddress());

            senderContact.setFullAddress(fullAddress.toString());
            senderContact.setName(logisticsConfig.getSenderName());
            senderContact.setMobile(logisticsConfig.getSenderPhone());
        } else {
            senderContact.setFullAddress(orderParam.getSenderAddress());
            senderContact.setName(orderParam.getSenderName());
            senderContact.setMobile(orderParam.getSenderPhone());
        }
        requestDTO.setSenderContact(senderContact);

        // 创建并设置收件人信息
        com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.Contact receiverContact = new com.lop.open.api.sdk.domain.ECAP.CommonCreateOrderApi.commonCreateOrderV1.Contact();
        receiverContact.setFullAddress(orderParam.getReceiverAddress());
        receiverContact.setName(orderParam.getReceiverName());
        receiverContact.setMobile(orderParam.getReceiverPhone());
        requestDTO.setReceiverContact(receiverContact);

        // 创建并设置产品信息
        CommonProductInfo productInfo = new CommonProductInfo();
        productInfo.setProductCode("ed-m-0001"); //京东标快	ed-m-0001	0-C2C；1-B2C；2-C2B
        requestDTO.setProductsReq(productInfo);

        // 创建并设置货品信息
        CommonCargoInfo cargoInfo = new CommonCargoInfo();
        cargoInfo.setName(orderParam.getCargoName());
        cargoInfo.setQuantity(orderParam.getCargoQuantity());
        cargoInfo.setWeight(new BigDecimal(orderParam.getCargoWeight()));

        // 计算体积：长 × 宽 × 高（单位：cm³）
        BigDecimal volume = new BigDecimal(orderParam.getCargoLength())
                .multiply(new BigDecimal(orderParam.getCargoWidth()))
                .multiply(new BigDecimal(orderParam.getCargoHeight()))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        cargoInfo.setVolume(volume);

        List<CommonCargoInfo> cargoInfos = Arrays.asList(cargoInfo);
        requestDTO.setCargoes(cargoInfos);

        // 设置请求参数
        request.setRequest(requestDTO);

        // 执行请求
        return jdLogisticsClient.execute(request, orderParam.getStoreId());

    }

}