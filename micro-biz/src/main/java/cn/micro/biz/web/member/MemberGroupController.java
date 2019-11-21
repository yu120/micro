package cn.micro.biz.web.member;

import cn.micro.biz.commons.auth.PreAuth;
import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.commons.mybatis.PageQuery;
import cn.micro.biz.entity.member.MemberGroupEntity;
import cn.micro.biz.service.member.IMemberGroupService;
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
 * Member Group Controller
 *
 * @author lry
 */
@PreAuth
@Validated
@RestController
@RequestMapping("member-group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MemberGroupController {

    private final IMemberGroupService memberGroupService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public boolean addMemberGroup(@RequestBody MemberGroupEntity memberGroup) {
        MemberGroupEntity queryMemberGroupEntity = new MemberGroupEntity();
        queryMemberGroupEntity.setCode(memberGroup.getCode());
        MemberGroupEntity queryMemberGroup = memberGroupService.getOne(Wrappers.query(queryMemberGroupEntity));
        if (queryMemberGroup != null) {
            throw new MicroBadRequestException("用户组CODE已存在");
        }

        return memberGroupService.save(memberGroup);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public boolean deleteMemberGroupById(@RequestBody @Size(min = 1, message = "至少删除1条记录") List<Long> ids) {
        return memberGroupService.removeByIds(ids);
    }

    @RequestMapping(value = "edit", method = RequestMethod.PUT)
    public boolean editMemberGroupById(@RequestBody MemberGroupEntity memberGroup) {
        MemberGroupEntity queryMemberGroupEntity = new MemberGroupEntity();
        queryMemberGroupEntity.setCode(memberGroup.getCode());
        MemberGroupEntity queryMemberGroup = memberGroupService.getOne(Wrappers.query(queryMemberGroupEntity));
        if (queryMemberGroup != null) {
            if (!queryMemberGroup.getId().equals(memberGroup.getId())) {
                throw new MicroBadRequestException("用户组CODE已存在");
            }
        }

        return memberGroupService.updateById(memberGroup);
    }

    @RequestMapping(value = "page", method = RequestMethod.POST)
    public Page<MemberGroupEntity> pageMemberGroup(@RequestBody PageQuery query) {
        Page<MemberGroupEntity> page = new Page<>(query.getPageNo(), query.getPageSize());
        page.setDesc(MicroEntity.EDITED_FIELD);
        page.setTotal(memberGroupService.count(new QueryWrapper<>()));
        if (page.getTotal() > 0) {
            IPage<MemberGroupEntity> tempPage = new Page<>((query.getPageNo() - 1) * query.getPageSize(), query.getPageSize());
            page.setRecords(memberGroupService.page(tempPage, new QueryWrapper<>()).getRecords());
        }

        return page;
    }

}
