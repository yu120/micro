package cn.micro.biz.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberRoleConvert {

    MemberRoleConvert INSTANCE = Mappers.getMapper(MemberRoleConvert.class);

}
