package com.star.lp.module.product.dal.mysql.printconfig;

import java.util.*;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.module.product.dal.dataobject.printconfig.PrintConfigDO;
import org.apache.ibatis.annotations.Mapper;
import com.star.lp.module.product.controller.admin.printconfig.vo.*;

/**
 * 打印设置配置 Mapper
 *
 * @author LIPENG
 */
@Mapper
public interface PrintConfigMapper extends BaseMapperX<PrintConfigDO> {

    default PageResult<PrintConfigDO> selectPage(PrintConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PrintConfigDO>()
                .eqIfPresent(PrintConfigDO::getConfigKey, reqVO.getConfigKey())
                .eqIfPresent(PrintConfigDO::getConfigValue, reqVO.getConfigValue())
                .eqIfPresent(PrintConfigDO::getConfigDesc, reqVO.getConfigDesc())
                .eqIfPresent(PrintConfigDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(PrintConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PrintConfigDO::getId));
    }

    default String selectByConfigKey(String configKey) {
        PrintConfigDO config = selectOne(new LambdaQueryWrapperX<PrintConfigDO>().eq(PrintConfigDO::getConfigKey, configKey));
        return config == null ? null : config.getConfigValue();
    }

}