package cn.micro.biz.web.member;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.commons.mybatis.PageQuery;
import cn.micro.biz.entity.member.MemberRoleEntity;
import cn.micro.biz.service.member.IMemberRoleService;
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
 * Member Role Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("member-role")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberRoleController {

    private final IMemberRoleService memberRoleService;

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addMemberRole(@RequestBody @Size(min = 1, message = "至少添加1条记录") List<MemberRoleEntity> memberRoles) {
        return memberRoleService.saveBatch(memberRoles);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deleteMemberRoleById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return memberRoleService.removeByIds(ids);
    }

    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public boolean editMemberRoleById(@RequestBody MemberRoleEntity memberRole) {
        return memberRoleService.updateById(memberRole);
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<MemberRoleEntity> pageMemberRole(
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "memberId", required = false) Long memberId,
            @RequestBody PageQuery query) {
        Page<MemberRoleEntity> page = new Page<>(query.getCurrent(), query.getSize());
        page.setDesc(MicroEntity.EDITED_FIELD);

        QueryWrapper<MemberRoleEntity> entityWrapper = new QueryWrapper<>();
        if (roleId != null || memberId != null) {
            MemberRoleEntity memberRole = new MemberRoleEntity();
            memberRole.setRoleId(roleId);
            memberRole.setMemberId(memberId);
            entityWrapper.setEntity(memberRole);
        }

        page.setTotal(memberRoleService.count(entityWrapper));
        if (page.getTotal() > 0) {
            IPage<MemberRoleEntity> tempPage = new Page<>((query.getCurrent() - 1) * query.getSize(), query.getSize());
            page.setRecords(memberRoleService.page(tempPage, entityWrapper).getRecords());
        }

        return page;
    }

}
