package cn.micro.biz.web;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.commons.mybatis.PageQuery;
import cn.micro.biz.entity.member.MemberGroupRole;
import cn.micro.biz.service.member.IMemberGroupRoleService;
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
 * Member Group Role Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("member-group-role")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberGroupRoleController {

    private final IMemberGroupRoleService memberGroupRoleService;

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addMemberGroupRole(@RequestBody @Size(min = 1, message = "至少添加1条记录") List<MemberGroupRole> memberGroupRoles) {
        return memberGroupRoleService.saveBatch(memberGroupRoles);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deleteMemberGroupRoleById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return memberGroupRoleService.removeByIds(ids);
    }

    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public boolean editMemberGroupRoleById(@RequestBody MemberGroupRole memberGroupRole) {
        return memberGroupRoleService.updateById(memberGroupRole);
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<MemberGroupRole> pageMemberGroupRole(
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "memberGroupId", required = false) Long memberGroupId,
            @RequestBody PageQuery query) {
        Page<MemberGroupRole> page = new Page<>(query.getCurrent(), query.getSize());
        page.setDesc(MicroEntity.EDITED_FIELD);

        QueryWrapper<MemberGroupRole> entityWrapper = new QueryWrapper<>();
        if (roleId != null || memberGroupId != null) {
            MemberGroupRole memberGroupRole = new MemberGroupRole();
            memberGroupRole.setRoleId(roleId);
            memberGroupRole.setMemberGroupId(memberGroupId);
            entityWrapper.setEntity(memberGroupRole);
        }

        page.setTotal(memberGroupRoleService.count(entityWrapper));
        if (page.getTotal() > 0) {
            IPage<MemberGroupRole> tempPage = new Page<>((query.getCurrent() - 1) * query.getSize(), query.getSize());
            page.setRecords(memberGroupRoleService.page(tempPage, entityWrapper).getRecords());
        }

        return page;
    }

}
