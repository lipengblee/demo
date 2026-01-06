package com.star.lp.module.trade.convert.print;

import com.star.lp.framework.common.util.collection.CollectionUtils;
import com.star.lp.module.trade.controller.app.print.vo.AppPrintSettingOptionRespVO;
import com.star.lp.module.trade.controller.app.print.vo.AppPrintSettingOptionValueRespVO;
import com.star.lp.module.trade.dal.dataobject.settingoption.SettingOptionDO;
import com.star.lp.module.trade.dal.dataobject.settingoption.SettingOptionValueDO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface SettingOptionConvert {
    SettingOptionConvert INSTANCE = Mappers.getMapper(SettingOptionConvert.class);

    /**
     * 将单个 SettingOptionDO 与对应的选项值列表转换为 AppPrintSettingOptionRespVO
     */
    @Mapping(target = "values", source = "valueList")
    AppPrintSettingOptionRespVO convert(SettingOptionDO bean, List<SettingOptionValueDO> valueList);

    /**
     * 批量转换：通过 optionId 映射选项值，避免循环查询
     */
    default List<AppPrintSettingOptionRespVO> convertList(List<SettingOptionDO> optionList, List<SettingOptionValueDO> allValueList) {
        // 构建 valueList 映射：key=optionId，value=该选项对应的所有值
        Map<Long, List<SettingOptionValueDO>> valueMap = allValueList.stream()
                .collect(Collectors.groupingBy(SettingOptionValueDO::getOptionId));

        // 调用单参数的 convertList，仅传入选项列表和转换函数
        return CollectionUtils.convertList(optionList, option ->
                convert(option, valueMap.getOrDefault(option.getId(), Collections.emptyList())));
    }

    /**
     * 将 SettingOptionValueDO 转换为 AppPrintSettingOptionValueRespVO
     */
    AppPrintSettingOptionValueRespVO convertValue(SettingOptionValueDO bean);
}