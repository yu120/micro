package cn.micro.biz.web.member;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.commons.mybatis.PageQuery;
import cn.micro.biz.entity.member.RolePermissionEntity;
import cn.micro.biz.service.member.IRolePermissionService;
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
 * Role Permission Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("role-permission")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RolePermissionController {

    private final IRolePermissionService rolePermissionService;

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addRolePermission(@RequestBody @Size(min = 1, message = "至少添加1条记录") List<RolePermissionEntity> rolePermissions) {
        return rolePermissionService.saveBatch(rolePermissions);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deleteRolePermissionById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return rolePermissionService.removeByIds(ids);
    }

    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public boolean editRolePermissionById(@RequestBody RolePermissionEntity rolePermission) {
        return rolePermissionService.updateById(rolePermission);
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<RolePermissionEntity> pageRolePermission(
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "permissionId", required = false) Long permissionId,
            @RequestBody PageQuery query) {
        Page<RolePermissionEntity> page = new Page<>(query.getPageNo(), query.getPageSize());
        page.setDesc(MicroEntity.EDITED_FIELD);

        QueryWrapper<RolePermissionEntity> entityWrapper = new QueryWrapper<>();
        if (roleId != null || permissionId != null) {
            RolePermissionEntity rolePermission = new RolePermissionEntity();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            entityWrapper.setEntity(rolePermission);
        }

        page.setTotal(rolePermissionService.count(entityWrapper));
        if (page.getTotal() > 0) {
            IPage<RolePermissionEntity> tempPage = new Page<>((query.getPageNo() - 1) * query.getPageSize(), query.getPageSize());
            page.setRecords(rolePermissionService.page(tempPage, entityWrapper).getRecords());
        }

        return page;
    }

}
