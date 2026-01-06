package com.star.lp.module.trade.service.logistics.jd;

import com.lop.open.api.sdk.LopException;
import com.lop.open.api.sdk.domain.jdcloudprint.TemplateService.getTemplates.QueryTemplatesDto;
import com.lop.open.api.sdk.domain.jdcloudprint.TemplateService.getTemplates.TemplateListResultDto;

import com.lop.open.api.sdk.domain.jdcloudprint.PullDataService.pullData.PullDataReqDTO;
import com.lop.open.api.sdk.domain.jdcloudprint.PullDataService.pullData.PullDataRespDTO;
import com.lop.open.api.sdk.response.ECAP.EcapV1OrdersCreateLopResponse;
import com.lop.open.api.sdk.response.ECAP.EcapV1OrdersPrecheckLopResponse;
import com.star.lp.module.trade.controller.admin.order.vo.JDLogisticsOrderParamReqVo;


/**
 * 京东物流服务接口
 *
 * @author star
 */
public interface JDLogisticsService {

    /**
     * 获取打印模板列表
     *
     * @param requestDto 请求参数对象
     * @param storeId    门店ID
     * @return 打印模板列表
     */
    TemplateListResultDto getPrintTemplates(QueryTemplatesDto requestDto, Long storeId) throws LopException;

    /**
     * 获取打印数据
     *
     * @param requestDto 请求参数对象
     * @param storeId    门店ID
     * @return 打印数据响应
     */
    PullDataRespDTO getPrintData(PullDataReqDTO requestDto, Long storeId) throws LopException;

    /**
     * 京东快递下单前置校验
     *
     * @param orderParam 订单参数
     * @return 前置校验结果
     * @throws LopException             京东物流API异常
     * @throws IllegalArgumentException 参数校验异常
     */
    EcapV1OrdersPrecheckLopResponse preCheckOrder(JDLogisticsOrderParamReqVo orderParam) throws LopException;

    /**
     * 创建京东物流订单
     *
     * @param orderParam 订单参数
     * @return 订单创建结果
     * @throws LopException 京东物流API异常
     */
    EcapV1OrdersCreateLopResponse createOrder(JDLogisticsOrderParamReqVo orderParam) throws LopException;

}
