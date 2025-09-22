package com.star.lp.module.promotion.controller.app.file;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.promotion.controller.app.file.vo.AppUserFileReqVO;
import com.star.lp.module.promotion.controller.app.file.vo.AppUserFileRespVO;
import com.star.lp.module.promotion.dal.dataobject.file.UserFileDO;
import com.star.lp.module.promotion.service.file.UserFileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.star.lp.framework.common.pojo.CommonResult;
import com.star.lp.framework.common.util.object.BeanUtils;
import java.util.List;
import static com.star.lp.framework.common.pojo.CommonResult.success;
import static com.star.lp.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 上传文件列表")
@RestController
@RequestMapping("/promotion/file")
@Validated
public class AppUserFileController {

    @Resource
    private UserFileService userFileService;

    @GetMapping("/list")
    @Operation(summary = "获得用户上传文件列表,不带分页")
    public CommonResult<List<AppUserFileRespVO>> getFileList() {
        //通过用户ID,查询用户名下的所有文档
        Long userId = getLoginUserId();
        List<UserFileDO> list = userFileService.getFileList(userId);
        return success(BeanUtils.toBean(list, AppUserFileRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "带分页的文件列表")
    public CommonResult<PageResult<AppUserFileRespVO>> getFilePage(@Valid AppUserFileReqVO appUserFileReqVO){
        Long userId = getLoginUserId();
        PageResult<UserFileDO> pageResult = userFileService.getFilePage(appUserFileReqVO,userId);
        return success(BeanUtils.toBean(pageResult, AppUserFileRespVO.class));
    }
}