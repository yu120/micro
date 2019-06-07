package cn.micro.biz.convert;

import cn.micro.biz.entity.member.Member;
import cn.micro.biz.model.edit.EditMemberInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MemberConvert {

    MemberConvert INSTANCE = Mappers.getMapper(MemberConvert.class);

    Member copy(EditMemberInfo editMemberInfo);

}
