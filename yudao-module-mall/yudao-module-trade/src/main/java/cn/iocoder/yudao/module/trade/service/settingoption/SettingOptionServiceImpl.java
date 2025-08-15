package cn.iocoder.yudao.module.trade.service.settingoption;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.trade.controller.admin.settingoption.vo.SettingOptionPageReqVO;
import cn.iocoder.yudao.module.trade.controller.admin.settingoption.vo.SettingOptionSaveReqVO;
import cn.iocoder.yudao.module.trade.dal.dataobject.settingoption.SettingOptionDO;
import cn.iocoder.yudao.module.trade.dal.dataobject.settingoption.SettingOptionValueDO;
import cn.iocoder.yudao.module.trade.dal.mysql.settingoption.SettingOptionMapper;
import cn.iocoder.yudao.module.trade.dal.mysql.settingoption.SettingOptionValueMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import static cn.iocoder.yudao.module.trade.enums.ErrorCodeConstants.SETTING_OPTION_NOT_EXISTS;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;


import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.diffList;

/**
 * 打印设置选项 Service 实现类
 *
 * @author LIPENG
 */
@Service
@Validated
public class SettingOptionServiceImpl implements SettingOptionService {

    @Resource
    private SettingOptionMapper settingOptionMapper;
    @Resource
    private SettingOptionValueMapper settingOptionValueMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSettingOption(SettingOptionSaveReqVO createReqVO) {
        // 插入
        SettingOptionDO settingOption = BeanUtils.toBean(createReqVO, SettingOptionDO.class);
        settingOptionMapper.insert(settingOption);


        // 插入子表
        createSettingOptionValueList(settingOption.getId(), createReqVO.getSettingOptionValues());
        // 返回
        return settingOption.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSettingOption(SettingOptionSaveReqVO updateReqVO) {
        // 校验存在
        validateSettingOptionExists(updateReqVO.getId());
        // 更新
        SettingOptionDO updateObj = BeanUtils.toBean(updateReqVO, SettingOptionDO.class);
        settingOptionMapper.updateById(updateObj);

        // 更新子表
        updateSettingOptionValueList(updateReqVO.getId(), updateReqVO.getSettingOptionValues());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSettingOption(Long id) {
        // 校验存在
        validateSettingOptionExists(id);
        // 删除
        settingOptionMapper.deleteById(id);

        // 删除子表
        deleteSettingOptionValueByOptionId(id);
    }

    @Override
        @Transactional(rollbackFor = Exception.class)
    public void deleteSettingOptionListByIds(List<Long> ids) {
        // 删除
        settingOptionMapper.deleteByIds(ids);
    
    // 删除子表
            deleteSettingOptionValueByOptionIds(ids);
    }


    private void validateSettingOptionExists(Long id) {
        if (settingOptionMapper.selectById(id) == null) {
            throw exception(SETTING_OPTION_NOT_EXISTS);
        }
    }

    @Override
    public SettingOptionDO getSettingOption(Long id) {
        return settingOptionMapper.selectById(id);
    }

    @Override
    public PageResult<SettingOptionDO> getSettingOptionPage(SettingOptionPageReqVO pageReqVO) {
        return settingOptionMapper.selectPage(pageReqVO);
    }

    // ==================== 子表（打印设置选项值） ====================

    @Override
    public List<SettingOptionValueDO> getSettingOptionValueListByOptionId(Long optionId) {
        return settingOptionValueMapper.selectListByOptionId(optionId);
    }

    private void createSettingOptionValueList(Long optionId, List<SettingOptionValueDO> list) {
        list.forEach(o -> o.setOptionId(optionId).clean());
        settingOptionValueMapper.insertBatch(list);
    }

    private void updateSettingOptionValueList(Long optionId, List<SettingOptionValueDO> list) {
	    list.forEach(o -> o.setOptionId(optionId).clean());
	    List<SettingOptionValueDO> oldList = settingOptionValueMapper.selectListByOptionId(optionId);
	    List<List<SettingOptionValueDO>> diffList = diffList(oldList, list, (oldVal, newVal) -> {
            boolean same = ObjectUtil.equal(oldVal.getId(), newVal.getId());
            if (same) {
                newVal.setId(oldVal.getId()).clean(); // 解决更新情况下：updateTime 不更新
            }
            return same;
	    });

	    // 第二步，批量添加、修改、删除
	    if (CollUtil.isNotEmpty(diffList.get(0))) {
	        settingOptionValueMapper.insertBatch(diffList.get(0));
	    }
	    if (CollUtil.isNotEmpty(diffList.get(1))) {
	        settingOptionValueMapper.updateBatch(diffList.get(1));
	    }
	    if (CollUtil.isNotEmpty(diffList.get(2))) {
	        settingOptionValueMapper.deleteByIds(convertList(diffList.get(2), SettingOptionValueDO::getId));
	    }
    }

    private void deleteSettingOptionValueByOptionId(Long optionId) {
        settingOptionValueMapper.deleteByOptionId(optionId);
    }

	private void deleteSettingOptionValueByOptionIds(List<Long> optionIds) {
        settingOptionValueMapper.deleteByOptionIds(optionIds);
	}

    @Override
    public List<SettingOptionDO> getAllSettingOptions() {
        // 查询所有打印设置选项，并按排序字段升序排列
        return settingOptionMapper.selectList(new LambdaQueryWrapperX<SettingOptionDO>()
                .orderByAsc(SettingOptionDO::getSort));
    }

    // SettingOptionServiceImpl 实现
    @Override
    public List<SettingOptionValueDO> getSettingOptionValueListByOptionIds(List<Long> optionIds) {
        return settingOptionValueMapper.selectListByOptionIds(optionIds);
    }

}