package com.star.lp.module.product.service.printconfig;

import cn.hutool.core.collection.CollUtil;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.star.lp.module.product.controller.admin.printconfig.vo.*;
import com.star.lp.module.product.dal.dataobject.printconfig.PrintConfigDO;
import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.common.pojo.PageParam;
import com.star.lp.framework.common.util.object.BeanUtils;

import com.star.lp.module.product.dal.mysql.printconfig.PrintConfigMapper;

import static com.star.lp.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.star.lp.framework.common.util.collection.CollectionUtils.convertList;
import static com.star.lp.framework.common.util.collection.CollectionUtils.diffList;
import static com.star.lp.module.product.enums.ErrorCodeConstants.*;

/**
 * 打印设置配置 Service 实现类
 *
 * @author LIPENG
 */
@Service
@Validated
public class PrintConfigServiceImpl implements PrintConfigService {

    @Resource
    private PrintConfigMapper printConfigMapper;

    @Override
    public Long createPrintConfig(PrintConfigSaveReqVO createReqVO) {
        // 插入
        PrintConfigDO printConfig = BeanUtils.toBean(createReqVO, PrintConfigDO.class);
        printConfigMapper.insert(printConfig);

        // 返回
        return printConfig.getId();
    }

    @Override
    public void updatePrintConfig(PrintConfigSaveReqVO updateReqVO) {
        // 校验存在
        validatePrintConfigExists(updateReqVO.getId());
        // 更新
        PrintConfigDO updateObj = BeanUtils.toBean(updateReqVO, PrintConfigDO.class);
        printConfigMapper.updateById(updateObj);
    }

    @Override
    public void deletePrintConfig(Long id) {
        // 校验存在
        validatePrintConfigExists(id);
        // 删除
        printConfigMapper.deleteById(id);
    }

    @Override
        public void deletePrintConfigListByIds(List<Long> ids) {
        // 删除
        printConfigMapper.deleteByIds(ids);
        }


    private void validatePrintConfigExists(Long id) {
        if (printConfigMapper.selectById(id) == null) {
            throw exception(PRINT_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public PrintConfigDO getPrintConfig(Long id) {
        return printConfigMapper.selectById(id);
    }

    @Override
    public PageResult<PrintConfigDO> getPrintConfigPage(PrintConfigPageReqVO pageReqVO) {
        return printConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public String getPrintConfigByConfigKey(String configKey) {
        return printConfigMapper.selectByConfigKey(configKey);
    }

}