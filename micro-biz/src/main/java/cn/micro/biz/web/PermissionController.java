package cn.micro.biz.web;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.exception.MicroBadRequestException;
import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.commons.mybatis.entity.PageQuery;
import cn.micro.biz.entity.member.Permission;
import cn.micro.biz.service.member.IPermissionService;
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
 * Permission Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("permission")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionController {

    private final IPermissionService permissionService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addPermission(@RequestBody Permission permission) {
        Permission queryPermission = permissionService.getOne(Permission::getCode, permission.getCode());
        if (queryPermission != null) {
            throw new MicroBadRequestException("权限CODE已存在");
        }

        return permissionService.save(permission);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deletePermissionById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return permissionService.removeByIds(ids);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public boolean updatePermissionById(@RequestBody Permission permission) {
        return permissionService.updateById(permission);
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<Permission> pagePermission(@RequestBody PageQuery query) {
        Page<Permission> page = new Page<>(query.getCurrent(), query.getSize());
        page.setDesc(MicroEntity.EDITED_FIELD);
        page.setTotal(permissionService.count(new QueryWrapper<>()));
        if (page.getTotal() > 0) {
            IPage<Permission> tempPermission = new Page<>((query.getCurrent() - 1) * query.getSize(), query.getSize());
            page.setRecords(permissionService.page(tempPermission, new QueryWrapper<>()).getRecords());
        }

        return page;
    }

}
