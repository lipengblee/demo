package com.star.lp.module.promotion.convert.userfile;

import com.star.lp.framework.common.pojo.PageResult;
import com.star.lp.module.promotion.controller.app.file.vo.AppUserFileReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserFileConvert {
    UserFileConvert INSTANCE = Mappers.getMapper(UserFileConvert.class);
}
