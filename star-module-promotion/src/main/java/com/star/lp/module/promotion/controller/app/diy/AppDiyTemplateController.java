package com.star.lp.module.promotion.controller.app.diy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.module.promotion.controller.app.diy.vo.AppDiyTemplatePropertyRespVO;
import com.star.lp.module.promotion.convert.diy.DiyTemplateConvert;
import com.star.lp.module.promotion.dal.dataobject.diy.DiyPageDO;
import com.star.lp.module.promotion.dal.dataobject.diy.DiyTemplateDO;
import com.star.lp.module.promotion.enums.diy.DiyPageEnum;
import com.star.lp.module.promotion.service.diy.DiyPageService;
import com.star.lp.module.promotion.service.diy.DiyTemplateService;
import com.star.lp.module.trade.api.store.TradeStoreApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.star.lp.framework.common.pojo.CommonResult.success;
import static com.star.lp.framework.common.util.collection.CollectionUtils.findFirst;

@Tag(name = "用户 APP - 装修模板")
@RestController
@RequestMapping("/promotion/diy-template")
@Validated
@Slf4j
public class AppDiyTemplateController {

    @Resource
    private DiyTemplateService diyTemplateService;
    @Resource
    private DiyPageService diyPageService;
    @Resource
    private TradeStoreApi tradeStoreApi;

    // TODO @疯狂：要不要把 used 和 get 接口合并哈；不传递 id，直接拿默认；
    @GetMapping("/used")
    @Operation(summary = "使用中的装修模板")
    @PermitAll
    public CommonResult<AppDiyTemplatePropertyRespVO> getUsedDiyTemplate() {
        DiyTemplateDO diyTemplate = diyTemplateService.getUsedDiyTemplate();
        return success(buildVo(diyTemplate));
    }

    @GetMapping("/get")
    @Operation(summary = "获得装修模板")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PermitAll
    public CommonResult<AppDiyTemplatePropertyRespVO> getDiyTemplate(@RequestParam("id") Long id) {
        DiyTemplateDO diyTemplate = diyTemplateService.getDiyTemplate(id);
        return success(buildVo(diyTemplate));
    }

    private AppDiyTemplatePropertyRespVO buildVo(DiyTemplateDO diyTemplate) {
        if (diyTemplate == null) {
            return null;
        }
        // 查询模板下的页面
        List<DiyPageDO> pages = diyPageService.getDiyPageByTemplateId(diyTemplate.getId());
        String home = findFirst(pages, page -> DiyPageEnum.INDEX.getName().equals(page.getName()), DiyPageDO::getProperty);
        boolean isStoreRule = tradeStoreApi.checkUserStoreRule();
        String user = findFirst(pages, page -> DiyPageEnum.MY.getName().equals(page.getName()), DiyPageDO::getProperty);
        // 如果没有门店权限，过滤掉商城组件
        if (!isStoreRule && user != null) {
            try {
                JSONObject userJson = JSON.parseObject(user);
                JSONArray components = userJson.getJSONArray("components");
                if (components != null) {
                    // 遍历所有组件
                    for (int i = 0; i < components.size(); i++) {
                        JSONObject component = components.getJSONObject(i);
                        if ("MenuGrid".equals(component.getString("id"))) {
                            // 获取property对象
                            JSONObject property = component.getJSONObject("property");
                            if (property != null) {
                                // 处理 MenuGrid 组件中的菜单项
                                JSONArray list = property.getJSONArray("list");
                                if (list != null) {
                                    JSONArray newList = new JSONArray();
                                    // 过滤掉商家入口菜单项
                                    for (int j = 0; j < list.size(); j++) {
                                        JSONObject item = list.getJSONObject(j);
                                        if (!"/pages/store/index/index".equals(item.getString("url"))) {
                                            newList.add(item);
                                        }
                                    }
                                    // 将过滤后的list重新放入property
                                    property.put("list", newList);
                                }
                            }
                        }
                    }
                    userJson.put("components", components);
                    user = userJson.toJSONString();
                }
            } catch (Exception e) {
                log.error("处理用户页面组件失败", e);
            }
        }
        // 拼接返回
        return DiyTemplateConvert.INSTANCE.convertPropertyVo2(diyTemplate, home, user);
    }

}
