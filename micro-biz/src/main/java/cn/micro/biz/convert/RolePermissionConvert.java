package cn.micro.biz.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RolePermissionConvert {

    RolePermissionConvert INSTANCE = Mappers.getMapper(RolePermissionConvert.class);

}
