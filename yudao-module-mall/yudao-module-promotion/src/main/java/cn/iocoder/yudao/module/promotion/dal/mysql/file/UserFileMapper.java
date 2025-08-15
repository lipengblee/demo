package cn.iocoder.yudao.module.promotion.dal.mysql.file;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.promotion.controller.app.file.vo.AppUserFileReqVO;
import cn.iocoder.yudao.module.promotion.dal.dataobject.file.UserFileDO;
import org.apache.ibatis.annotations.Mapper;


/**
 * 文件 Mapper
 *
 * @author LIPENG
 */
@Mapper
public interface UserFileMapper extends BaseMapperX<UserFileDO> {

    default List<UserFileDO> fileList(Long userId) {
        return selectList(UserFileDO::getCreator,userId);
    }

    default PageResult<UserFileDO> filePage(AppUserFileReqVO appUserFileReqVO, Long userId){
        return selectPage(appUserFileReqVO,new LambdaQueryWrapperX<UserFileDO>()
                .eqIfPresent(UserFileDO::getCreator,userId)
                .orderByDesc(UserFileDO::getCreateTime)
        );
    };
}