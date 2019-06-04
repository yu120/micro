package cn.micro.biz.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionConvert {

    PermissionConvert INSTANCE = Mappers.getMapper(PermissionConvert.class);

}
