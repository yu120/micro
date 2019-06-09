package cn.micro.biz.web.member;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.commons.mybatis.PageQuery;
import cn.micro.biz.entity.member.MemberGroup;
import cn.micro.biz.service.member.IMemberGroupService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Member Group Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("member-group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberGroupController {

    private final IMemberGroupService memberGroupService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addMemberGroup(@RequestBody MemberGroup memberGroup) {
        MemberGroup queryMemberGroup = memberGroupService.getOne(MemberGroup::getCode, memberGroup.getCode());
        if (queryMemberGroup != null) {
            throw new MicroBadRequestException("用户组CODE已存在");
        }

        return memberGroupService.save(memberGroup);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deleteMemberGroupById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return memberGroupService.removeByIds(ids);
    }

    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public boolean editMemberGroupById(@RequestBody MemberGroup memberGroup) {
        MemberGroup queryMemberGroup = memberGroupService.getOne(MemberGroup::getCode, memberGroup.getCode());
        if (queryMemberGroup != null) {
            if (!queryMemberGroup.getId().equals(memberGroup.getId())) {
                throw new MicroBadRequestException("用户组CODE已存在");
            }
        }

        return memberGroupService.updateById(memberGroup);
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<MemberGroup> pageMemberGroup(@RequestBody PageQuery query) {
        Page<MemberGroup> page = new Page<>(query.getCurrent(), query.getSize());
        page.setDesc(MicroEntity.EDITED_FIELD);
        page.setTotal(memberGroupService.count(new QueryWrapper<>()));
        if (page.getTotal() > 0) {
            IPage<MemberGroup> tempPage = new Page<>((query.getCurrent() - 1) * query.getSize(), query.getSize());
            page.setRecords(memberGroupService.page(tempPage, new QueryWrapper<>()).getRecords());
        }

        return page;
    }

}
