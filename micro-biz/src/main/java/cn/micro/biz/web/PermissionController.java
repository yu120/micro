package cn.micro.biz.web;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.exception.MicroBadRequestException;
import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.commons.mybatis.entity.PageQuery;
import cn.micro.biz.entity.member.Permission;
import cn.micro.biz.mapper.member.IPermissionMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Permission Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("permission")
public class PermissionController {

    @Resource
    private IPermissionMapper permissionMapper;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addPermission(@RequestBody Permission permission) {
        Permission queryPermission = permissionMapper.selectOne(Permission::getCode, permission.getCode());
        if (queryPermission != null) {
            throw new MicroBadRequestException("权限CODE已存在");
        }

        return permissionMapper.insert(permission) > 0;
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deletePermissionById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return permissionMapper.deleteBatchIds(ids) > 0;
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public boolean updatePermissionById(@RequestBody Permission permission) {
        return permissionMapper.updateById(permission) > 0;
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
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

}
