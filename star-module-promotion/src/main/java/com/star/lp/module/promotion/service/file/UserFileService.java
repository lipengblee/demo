package com.star.lp.module.promotion.service.file;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.promotion.controller.app.file.vo.AppUserFileReqVO;
import com.star.lp.module.promotion.dal.dataobject.file.UserFileDO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;


/**
 * 文件 Service 接口
 *
 * @author LIPENG
 */
public interface UserFileService {

    List<UserFileDO> getFileList(Long userId);

    PageResult<UserFileDO> getFilePage(AppUserFileReqVO appUserFileReqVO, Long userId);

}