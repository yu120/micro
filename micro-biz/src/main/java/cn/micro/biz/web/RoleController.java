package cn.micro.biz.web;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.exception.MicroBadRequestException;
import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import cn.micro.biz.commons.mybatis.entity.PageQuery;
import cn.micro.biz.entity.member.Role;
import cn.micro.biz.mapper.member.IRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
public class RoleController {

    @Resource
    private IRoleMapper roleMapper;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addRole(@RequestBody Role role) {
        Role queryRole = roleMapper.selectOne(Role::getCode, role.getCode());
        if (queryRole != null) {
            throw new MicroBadRequestException("角色CODE已存在");
        }

        return roleMapper.insert(role) > 0;
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deleteRoleById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return roleMapper.deleteBatchIds(ids) > 0;
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public boolean updateRoleById(@RequestBody Role role) {
        Role queryRole = roleMapper.selectOne(Role::getCode, role.getCode());
        if (queryRole != null) {
            if (!queryRole.getId().equals(role.getId())) {
                throw new MicroBadRequestException("角色CODE已存在");
            }
        }

        return roleMapper.updateById(role) > 0;
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
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

}
