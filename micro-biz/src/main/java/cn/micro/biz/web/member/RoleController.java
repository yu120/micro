package cn.micro.biz.web.member;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.commons.mybatis.PageQuery;
import cn.micro.biz.entity.member.Role;
import cn.micro.biz.service.member.IRoleService;
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
 * Role Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("role")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController {

    private final IRoleService roleService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addRole(@RequestBody Role role) {
        Role queryRole = roleService.getOne(Role::getCode, role.getCode());
        if (queryRole != null) {
            throw new MicroBadRequestException("角色CODE已存在");
        }

        return roleService.save(role);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deleteRoleById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return roleService.removeByIds(ids);
    }

    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public boolean editRoleById(@RequestBody Role role) {
        Role queryRole = roleService.getOne(Role::getCode, role.getCode());
        if (queryRole != null) {
            if (!queryRole.getId().equals(role.getId())) {
                throw new MicroBadRequestException("角色CODE已存在");
            }
        }

        return roleService.updateById(role);
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<Role> pageRole(@RequestBody PageQuery query) {
        Page<Role> page = new Page<>(query.getCurrent(), query.getSize());
        page.setDesc(MicroEntity.EDITED_FIELD);
        page.setTotal(roleService.count(new QueryWrapper<>()));
        if (page.getTotal() > 0) {
            IPage<Role> tempPage = new Page<>((query.getCurrent() - 1) * query.getSize(), query.getSize());
            page.setRecords(roleService.page(tempPage, new QueryWrapper<>()).getRecords());
        }

        return page;
    }

}
