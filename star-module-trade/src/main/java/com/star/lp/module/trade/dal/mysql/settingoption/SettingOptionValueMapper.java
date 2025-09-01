package com.star.lp.module.trade.dal.mysql.settingoption;

import java.util.*;

import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.module.trade.dal.dataobject.settingoption.SettingOptionValueDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 打印设置选项值 Mapper
 *
 * @author LIPENG
 */
@Mapper
public interface SettingOptionValueMapper extends BaseMapperX<SettingOptionValueDO> {

    default List<SettingOptionValueDO> selectListByOptionId(Long optionId) {
        return selectList(SettingOptionValueDO::getOptionId, optionId);
    }

    default int deleteByOptionId(Long optionId) {
        return delete(SettingOptionValueDO::getOptionId, optionId);
    }

	default int deleteByOptionIds(List<Long> optionIds) {
	    return deleteBatch(SettingOptionValueDO::getOptionId, optionIds);
	}

    // 批量查询选项值
    default List<SettingOptionValueDO> selectListByOptionIds(Collection<Long> optionIds) {
        return selectList(SettingOptionValueDO::getOptionId, optionIds);
    }

}