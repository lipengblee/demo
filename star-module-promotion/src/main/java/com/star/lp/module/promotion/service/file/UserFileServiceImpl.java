package com.star.lp.module.promotion.service.file;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.promotion.controller.app.file.vo.AppUserFileReqVO;
import com.star.lp.module.promotion.dal.dataobject.file.UserFileDO;
import com.star.lp.module.promotion.dal.mysql.file.UserFileMapper;
import org.springframework.stereotype.Service;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import java.util.List;


/**
 * 文件 Service 实现类
 *
 * @author LIPENG
 */
@Service
@Validated
public class UserFileServiceImpl implements UserFileService {

    @Resource
    private UserFileMapper userFileMapper;

    @Override
    public List<UserFileDO> getFileList(Long userId) {
        return userFileMapper.fileList(userId);
    }

    @Override
    public PageResult<UserFileDO> getFilePage(AppUserFileReqVO appUserFileReqVO, Long userId){
        return userFileMapper.filePage(appUserFileReqVO,userId);
    }


}