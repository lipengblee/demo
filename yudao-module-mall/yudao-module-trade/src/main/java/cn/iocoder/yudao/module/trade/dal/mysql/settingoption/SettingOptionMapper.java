package cn.iocoder.yudao.module.trade.dal.mysql.settingoption;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.trade.controller.admin.settingoption.vo.SettingOptionPageReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.settingoption.SettingOptionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 打印设置选项 Mapper
 *
 * @author LIPENG
 */
@Mapper
public interface SettingOptionMapper extends BaseMapperX<SettingOptionDO> {

    default PageResult<SettingOptionDO> selectPage(SettingOptionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SettingOptionDO>()
                .likeIfPresent(SettingOptionDO::getName, reqVO.getName())
                .eqIfPresent(SettingOptionDO::getSort, reqVO.getSort())
                .eqIfPresent(SettingOptionDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(SettingOptionDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SettingOptionDO::getId));
    }

}