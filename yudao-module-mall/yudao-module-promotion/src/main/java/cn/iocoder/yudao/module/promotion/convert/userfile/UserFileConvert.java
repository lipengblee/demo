package cn.iocoder.yudao.module.promotion.convert.userfile;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.promotion.controller.app.file.vo.AppUserFileReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserFileConvert {
    UserFileConvert INSTANCE = Mappers.getMapper(UserFileConvert.class);
}
