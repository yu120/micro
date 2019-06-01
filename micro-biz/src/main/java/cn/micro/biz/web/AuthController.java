package cn.micro.biz.web;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.commons.mybatis.entity.PageQuery;
import cn.micro.biz.entity.member.Permission;
import cn.micro.biz.entity.member.Role;
import cn.micro.biz.entity.member.RolePermission;
import cn.micro.biz.mapper.member.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Auth Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("auth")
public class AuthController {

    @Resource
    private IMemberMapper memberMapper;
    @Resource
    private IMemberRoleMapper memberRoleMapper;
    @Resource
    private IRoleMapper roleMapper;
    @Resource
    private IPermissionMapper permissionMapper;
    @Resource
    private IRolePermissionMapper rolePermissionMapper;

    // ========= Role

    @RequestMapping(value = "role", method = RequestMethod.POST)
    public boolean addRole(@RequestBody Role role) {
        return roleMapper.insert(role) > 0;
    }

    @RequestMapping(value = "role", method = RequestMethod.DELETE)
    public boolean deleteRoleById(@RequestBody List<Long> ids) {
        return roleMapper.deleteBatchIds(ids) > 0;
    }

    @RequestMapping(value = "role", method = RequestMethod.PUT)
    public boolean updateRoleById(@RequestBody Role role) {
        return roleMapper.updateById(role) > 0;
    }

    @RequestMapping(value = "roles", method = RequestMethod.POST)
    public Page<Role> pageRole(@RequestBody PageQuery query) {
        Page<Role> page = new Page<>(query.getCurrent(), query.getSize());
        page.setDesc(MicroEntity.EDITED_FIELD);

        page.setTotal(Long.valueOf(roleMapper.selectCount(new QueryWrapper<>())));
        if (page.getTotal() > 0) {
            IPage<Role> tempPage = new Page<>((query.getCurrent() - 1) * query.getSize(), query.getSize());
            page.setRecords(roleMapper.selectPage(tempPage, new QueryWrapper<>()).getRecords());
        }

        return page;
    }

    // ========= Permission

    @RequestMapping(value = "permission", method = RequestMethod.POST)
    public boolean addPermission(@RequestBody Permission permission) {
        return permissionMapper.insert(permission) > 0;
    }

    @RequestMapping(value = "permission", method = RequestMethod.DELETE)
    public boolean deletePermissionById(@RequestBody List<Long> ids) {
        return permissionMapper.deleteBatchIds(ids) > 0;
    }

    @RequestMapping(value = "permission", method = RequestMethod.PUT)
    public boolean updatePermissionById(@RequestBody Permission permission) {
        return permissionMapper.updateById(permission) > 0;
    }

    @RequestMapping(value = "permissions", method = RequestMethod.POST)
    public Page<Permission> pagePermission(@RequestBody PageQuery query) {
        Page<Permission> page = new Page<>(query.getCurrent(), query.getSize());
        page.setDesc(MicroEntity.EDITED_FIELD);

        page.setTotal(Long.valueOf(permissionMapper.selectCount(new QueryWrapper<>())));
        if (page.getTotal() > 0) {
            IPage<Permission> tempPermission = new Page<>((query.getCurrent() - 1) * query.getSize(), query.getSize());
            page.setRecords(permissionMapper.selectPage(tempPermission, new QueryWrapper<>()).getRecords());
        }

        return page;
    }

    // ========= Role Permission

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "role-permission", method = RequestMethod.POST)
    public boolean addRolePermission(@RequestBody List<RolePermission> rolePermissions) {
        if (CollectionUtils.isEmpty(rolePermissions)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }

        SqlSession batchSqlSession = SqlHelper.sqlSessionBatch(RolePermission.class);
        try {
            int size = rolePermissions.size();
            String sqlStatement = SqlHelper.table(RolePermission.class).getSqlStatement(SqlMethod.INSERT_ONE.getMethod());
            for (int i = 0; i < size; i++) {
                batchSqlSession.insert(sqlStatement, rolePermissions.get(i));
                if (i >= 1 && i % 30 == 0) {
                    batchSqlSession.flushStatements();
                }
            }

            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new MybatisPlusException("Error: Cannot execute insertBatch Method. Cause", e);
        }

        return true;
    }

    @RequestMapping(value = "role-permission", method = RequestMethod.DELETE)
    public boolean deleteRolePermissionById(@RequestBody List<Long> ids) {
        return rolePermissionMapper.deleteBatchIds(ids) > 0;
    }

    @RequestMapping(value = "role-permission", method = RequestMethod.PUT)
    public boolean updateRolePermissionById(@RequestBody RolePermission rolePermission) {
        return rolePermissionMapper.updateById(rolePermission) > 0;
    }

    @RequestMapping(value = "role-permissions", method = RequestMethod.POST)
    public Page<RolePermission> pageRolePermission(
            @RequestParam(value = "roleId", required = false) Long roleId,
            @RequestParam(value = "permissionId", required = false) Long permissionId,
            @RequestBody PageQuery query) {
        Page<RolePermission> page = new Page<>(query.getCurrent(), query.getSize());
        page.setDesc(MicroEntity.EDITED_FIELD);

        QueryWrapper<RolePermission> entityWrapper = new QueryWrapper<>();
        if (roleId != null || permissionId != null) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            entityWrapper.setEntity(rolePermission);
        }

        page.setTotal(Long.valueOf(rolePermissionMapper.selectCount(entityWrapper)));
        if (page.getTotal() > 0) {
            IPage<RolePermission> tempPage = new Page<>((query.getCurrent() - 1) * query.getSize(), query.getSize());
            page.setRecords(rolePermissionMapper.selectPage(tempPage, entityWrapper).getRecords());
        }

        return page;
    }

}
