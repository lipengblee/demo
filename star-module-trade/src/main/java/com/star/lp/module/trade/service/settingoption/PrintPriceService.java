package com.star.lp.module.trade.service.settingoption;

import cn.hutool.core.collection.CollUtil;
import com.star.lp.module.trade.dal.dataobject.settingoption.SettingOptionValueDO;
import com.star.lp.module.trade.dal.mysql.settingoption.SettingOptionValueMapper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 打印价格服务类
 * 用于计算打印服务的单价
 */
@Service          // 标识为Spring服务组件
@RequiredArgsConstructor // Lombok生成构造器，自动注入依赖
@Slf4j            // Lombok生成日志记录器
public class PrintPriceService {

    // 注入设置选项值数据访问层
    private final SettingOptionValueMapper settingOptionValueMapper;

    /**
     * 计算单价方法
     * @param selectedOptions 选项的JSON字符串，格式为Map<选项ID, 选项值ID>
     * @return 计算出的单价，如果计算失败或无匹配项则返回0
     */
    public Integer calculateTotalPrice(String selectedOptions, int totalPages, int copyCount) {
        // 将JSON字符串解析为Map<Integer, Integer>
        Map<Integer, Integer> options = JSON.parseObject(selectedOptions, new TypeReference<Map<Integer, Integer>>() {
        });
        // 检查选项是否为空
        if (CollUtil.isEmpty(options)) {
            return 0;
        }
        try {
            List<SettingOptionValueDO> optionValues = settingOptionValueMapper.selectList(new QueryWrapper<SettingOptionValueDO>().in("option_id", options.keySet()).in("id", options.values()));

            if (CollUtil.isEmpty(optionValues)) {
                log.warn("计算单价：未查询到匹配的选项值，options={}", options);
                return 0;
            }

            // 3. 计算总价格：单页总价 × 总页数 × 份数
            int pageTotalPrice = optionValues.stream().mapToInt(SettingOptionValueDO::getPrice).sum();
            return pageTotalPrice * totalPages * copyCount;

        } catch (Exception e) {
            log.error("计算单价失败，selectedOptions: {}", selectedOptions, e);
            return 0;
        }
    }
}
