package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.commons.exception.MicroBadRequestException;
import cn.micro.biz.commons.exception.MicroErrorException;
import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.commons.utils.IPUtils;
import cn.micro.biz.commons.utils.MD5Utils;
import cn.micro.biz.entity.member.Account;
import cn.micro.biz.entity.member.Member;
import cn.micro.biz.entity.member.MemberRole;
import cn.micro.biz.mapper.member.IAccountMapper;
import cn.micro.biz.mapper.member.IMemberMapper;
import cn.micro.biz.mapper.member.IMemberRoleMapper;
import cn.micro.biz.model.add.RegisterMember;
import cn.micro.biz.model.query.LoginAccountQuery;
import cn.micro.biz.service.member.IAccountService;
import cn.micro.biz.type.member.AccountEnum;
import cn.micro.biz.type.member.RoleEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Account Service Implements
 *
 * @author lry
 */
@Service
public class AccountServiceImpl extends MicroServiceImpl<IAccountMapper, Account> implements IAccountService {

    @Resource
    private IMemberMapper memberMapper;
    @Resource
    private IMemberRoleMapper memberRoleMapper;

    @GlobalTransactional
    @Override
    public Boolean doRegister(RegisterMember registerMember) {
        // 账号唯一性校验
        Account account = super.getOne(Account::getCode, registerMember.getAccount());
        if (account != null) {
            throw new MicroBadRequestException("账号已存在");
        }

        // 检查账号是否存在用户信息
        Member member, addMember = new Member();
        if (AccountEnum.EMAIL.getValue() == registerMember.getCategory()) {
            member = memberMapper.selectOne(Member::getEmail, registerMember.getAccount());
            addMember.setEmail(registerMember.getAccount());
        } else if (AccountEnum.MOBILE.getValue() == registerMember.getCategory()) {
            member = memberMapper.selectOne(Member::getMobile, registerMember.getAccount());
            addMember.setMobile(registerMember.getAccount());
        } else {
            throw new MicroBadRequestException("非法账号类型");
        }

        if (member == null) {
            // 没有用户信息,则立即注册一条用户信息
            member = addMember;
            member.setSalt(MD5Utils.randomSalt());
            member.setPassword(MD5Utils.encode(registerMember.getPassword(), member.getSalt()));
            if (memberMapper.insert(member) <= 0) {
                throw new MicroErrorException("用户注册失败");
            }
        }

        // 注册账号
        Account addAccount = new Account();
        addAccount.setMemberId(member.getId());
        addAccount.setCode(registerMember.getAccount());
        addAccount.setCategory(registerMember.getCategory());
        addAccount.setIp(IPUtils.getRequestIPAddress());
        addAccount.setPlatform(registerMember.getPlatform());
        if (baseMapper.insert(addAccount) <= 0) {
            throw new MicroErrorException("账号注册失败");
        }

        // 分配角色
        MemberRole addMemberRole = new MemberRole();
        addMemberRole.setMemberId(member.getId());
        addMemberRole.setRoleId(RoleEnum.ROLE_MEMBER.getValue());
        if (memberRoleMapper.insert(addMemberRole) <= 0) {
            throw new MicroErrorException("角色分配失败");
        }

        return true;
    }

    @Override
    public MicroToken queryLogin(LoginAccountQuery loginAccountQuery) {
        // 校验账号信息
        Account queryAccount = new Account();
        queryAccount.setCode(loginAccountQuery.getAccount());
        Account account = baseMapper.selectOne(Wrappers.query(queryAccount));
        if (account == null) {
            throw new MicroBadRequestException("账号不存在");
        }
        if (account.getMemberId() == null) {
            throw new MicroBadRequestException("未绑定用户");
        }

        // 查询用户信息
        Member member = memberMapper.selectById(account.getMemberId());
        if (member == null) {
            throw new MicroBadRequestException("未找到用户");
        }
        if (StringUtils.isBlank(member.getPassword())) {
            throw new MicroBadRequestException("未设置密码");
        }
        if (!MD5Utils.check(loginAccountQuery.getPassword(), member.getPassword(), member.getSalt())) {
            throw new MicroBadRequestException("密码错误");
        }

        // 查询当前用户的所有角色
        List<String> roleCodes = memberRoleMapper.selectRoleCodesByMemberId(account.getMemberId());
        if (CollectionUtils.isEmpty(roleCodes)) {
            throw new MicroBadRequestException("用户未分配角色");
        }

        return MicroAuthContext.build(member.getTenantId(), member.getId(), member.getName(),
                loginAccountQuery.getDevice(), roleCodes, null);
    }

}
