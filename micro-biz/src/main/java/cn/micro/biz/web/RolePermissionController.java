package cn.micro.biz.web;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.commons.mybatis.entity.PageQuery;
import cn.micro.biz.entity.member.RolePermission;
import cn.micro.biz.mapper.member.IRolePermissionMapper;
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
public class RolePermissionController {

    @Resource
    private IRolePermissionMapper rolePermissionMapper;

    @Transactional(rollbackFor = Exception.class)
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addRolePermission(@RequestBody @Size(min = 1, message = "至少添加1条记录") List<RolePermission> rolePermissions) {
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

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deleteRolePermissionById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return rolePermissionMapper.deleteBatchIds(ids) > 0;
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public boolean updateRolePermissionById(@RequestBody RolePermission rolePermission) {
        return rolePermissionMapper.updateById(rolePermission) > 0;
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
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
