package cn.iocoder.yudao.module.promotion.service.file;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.app.file.vo.AppUserFileReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.file.UserFileDO;
import cn.iocoder.yudao.module.promotion.dal.mysql.file.UserFileMapper;
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