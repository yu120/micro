package cn.micro.biz.web.member;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.commons.mybatis.PageQuery;
import cn.micro.biz.entity.member.MemberGroupMemberEntity;
import cn.micro.biz.service.member.IMemberGroupMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * Member Group Member Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("member-group-member")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberGroupMemberController {

    private final IMemberGroupMemberService remberGroupMemberService;

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addMemberGroupMember(@RequestBody @Size(min = 1, message = "至少添加1条记录") List<MemberGroupMemberEntity> remberGroupMembers) {
        return remberGroupMemberService.saveBatch(remberGroupMembers);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deleteMemberGroupMemberById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return remberGroupMemberService.removeByIds(ids);
    }

    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public boolean editMemberGroupMemberById(@RequestBody MemberGroupMemberEntity remberGroupMember) {
        return remberGroupMemberService.updateById(remberGroupMember);
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<MemberGroupMemberEntity> pageMemberGroupMember(
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestParam(value = "memberGroupId", required = false) Long memberGroupId,
            @RequestBody PageQuery query) {
        Page<MemberGroupMemberEntity> page = new Page<>(query.getCurrent(), query.getSize());
        page.setDesc(MicroEntity.EDITED_FIELD);

        QueryWrapper<MemberGroupMemberEntity> entityWrapper = new QueryWrapper<>();
        if (memberId != null || memberGroupId != null) {
            MemberGroupMemberEntity remberGroupMember = new MemberGroupMemberEntity();
            remberGroupMember.setMemberId(memberId);
            remberGroupMember.setMemberGroupId(memberGroupId);
            entityWrapper.setEntity(remberGroupMember);
        }

        page.setTotal(remberGroupMemberService.count(entityWrapper));
        if (page.getTotal() > 0) {
            IPage<MemberGroupMemberEntity> tempPage = new Page<>((query.getCurrent() - 1) * query.getSize(), query.getSize());
            page.setRecords(remberGroupMemberService.page(tempPage, entityWrapper).getRecords());
        }

        return page;
    }

}
