package cn.micro.biz.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberConvert {

    MemberConvert INSTANCE = Mappers.getMapper(MemberConvert.class);

}
