package cn.iocoder.yudao.module.promotion.service.file;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.app.file.vo.AppUserFileReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.file.UserFileDO;
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