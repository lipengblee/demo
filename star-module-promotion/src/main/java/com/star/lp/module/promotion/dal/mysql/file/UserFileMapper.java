package com.star.lp.module.promotion.dal.mysql.file;

import java.util.*;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.framework.mybatis.core.mapper.BaseMapperX;
import com.star.lp.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.star.lp.module.promotion.controller.app.file.vo.AppUserFileReqVO;
import com.star.lp.module.promotion.dal.dataobject.file.UserFileDO;
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