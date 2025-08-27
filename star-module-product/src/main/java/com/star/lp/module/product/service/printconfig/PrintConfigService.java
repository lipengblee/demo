package com.star.lp.module.product.service.printconfig;

import java.util.*;
import jakarta.validation.*;
import com.star.lp.module.product.controller.admin.printconfig.vo.*;
import com.star.lp.module.product.dal.dataobject.printconfig.PrintConfigDO;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.common.pojo.PageParam;

/**
 * 打印设置配置 Service 接口
 *
 * @author LIPENG
 */
public interface PrintConfigService {

    /**
     * 创建打印设置配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPrintConfig(@Valid PrintConfigSaveReqVO createReqVO);

    /**
     * 更新打印设置配置
     *
     * @param updateReqVO 更新信息
     */
    void updatePrintConfig(@Valid PrintConfigSaveReqVO updateReqVO);

    /**
     * 删除打印设置配置
     *
     * @param id 编号
     */
    void deletePrintConfig(Long id);

    /**
    * 批量删除打印设置配置
    *
    * @param ids 编号
    */
    void deletePrintConfigListByIds(List<Long> ids);

    /**
     * 获得打印设置配置
     *
     * @param id 编号
     * @return 打印设置配置
     */
    PrintConfigDO getPrintConfig(Long id);

    /**
     * 获得打印设置配置分页
     *
     * @param pageReqVO 分页查询
     * @return 打印设置配置分页
     */
    PageResult<PrintConfigDO> getPrintConfigPage(PrintConfigPageReqVO pageReqVO);

    String getPrintConfigByConfigKey(String configKey);

}