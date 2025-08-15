package cn.iocoder.yudao.module.trade.service.settingoption;

import java.util.*;

import cn.iocoder.yudao.module.trade.controller.admin.settingoption.vo.SettingOptionPageReqVO;
import cn.iocoder.yudao.module.trade.controller.admin.settingoption.vo.SettingOptionSaveReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.settingoption.SettingOptionDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.settingoption.SettingOptionValueDO;
import jakarta.validation.*;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 打印设置选项 Service 接口
 *
 * @author LIPENG
 */
public interface SettingOptionService {

    /**
     * 创建打印设置选项
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createSettingOption(@Valid SettingOptionSaveReqVO createReqVO);

    /**
     * 更新打印设置选项
     *
     * @param updateReqVO 更新信息
     */
    void updateSettingOption(@Valid SettingOptionSaveReqVO updateReqVO);

    /**
     * 删除打印设置选项
     *
     * @param id 编号
     */
    void deleteSettingOption(Long id);

    /**
    * 批量删除打印设置选项
    *
    * @param ids 编号
    */
    void deleteSettingOptionListByIds(List<Long> ids);

    /**
     * 获得打印设置选项
     *
     * @param id 编号
     * @return 打印设置选项
     */
    SettingOptionDO getSettingOption(Long id);

    /**
     * 获得打印设置选项分页
     *
     * @param pageReqVO 分页查询
     * @return 打印设置选项分页
     */
    PageResult<SettingOptionDO> getSettingOptionPage(SettingOptionPageReqVO pageReqVO);

    // ==================== 子表（打印设置选项值） ====================

    /**
     * 获得打印设置选项值列表
     *
     * @param optionId 关联选项ID（print_setting_option.id）
     * @return 打印设置选项值列表
     */
    List<SettingOptionValueDO> getSettingOptionValueListByOptionId(Long optionId);

    /**
     * 获得所有打印设置选项
     *
     * @return 打印设置选项列表
     */
    List<SettingOptionDO> getAllSettingOptions();

    // SettingOptionService 接口
    List<SettingOptionValueDO> getSettingOptionValueListByOptionIds(List<Long> optionIds);

}