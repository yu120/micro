package cn.micro.biz.web.member;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.commons.mybatis.PageQuery;
import cn.micro.biz.entity.member.PermissionEntity;
import cn.micro.biz.service.member.IPermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
    public boolean addPermission(@RequestBody PermissionEntity permission) {
        PermissionEntity queryPermissionEntity = new PermissionEntity();
        queryPermissionEntity.setCode(permission.getCode());
        PermissionEntity queryPermission = permissionService.getOne(Wrappers.query(queryPermissionEntity));
        if (queryPermission != null) {
            throw new MicroBadRequestException("权限CODE已存在");
        }

        return permissionService.save(permission);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deletePermissionById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return permissionService.removeByIds(ids);
    }

    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public boolean editPermissionById(@RequestBody PermissionEntity permission) {
        return permissionService.updateById(permission);
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<PermissionEntity> pagePermission(@RequestBody PageQuery query) {
        Page<PermissionEntity> page = new Page<>(query.getPageNo(), query.getPageSize());
        page.setDesc(MicroEntity.EDITED_FIELD);
        page.setTotal(permissionService.count(new QueryWrapper<>()));
        if (page.getTotal() > 0) {
            IPage<PermissionEntity> tempPermission = new Page<>((query.getPageNo() - 1) * query.getPageSize(), query.getPageSize());
            page.setRecords(permissionService.page(tempPermission, new QueryWrapper<>()).getRecords());
        }

        return page;
    }

}
