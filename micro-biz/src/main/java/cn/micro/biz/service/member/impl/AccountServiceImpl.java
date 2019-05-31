package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.commons.exception.MicroBadRequestException;
import cn.micro.biz.commons.exception.MicroErrorException;
import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.commons.utils.IPUtils;
import cn.micro.biz.commons.utils.MD5Utils;
import cn.micro.biz.entity.member.*;
import cn.micro.biz.mapper.member.IAccountMapper;
import cn.micro.biz.mapper.member.IMemberGroupMemberMapper;
import cn.micro.biz.mapper.member.IMemberMapper;
import cn.micro.biz.model.add.RegisterAccount;
import cn.micro.biz.model.query.LoginAccount;
import cn.micro.biz.service.member.IAccountService;
import cn.micro.biz.service.member.IMemberRoleService;
import cn.micro.biz.type.member.AccountEnum;
import cn.micro.biz.type.member.MemberGroupEnum;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
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
    private IMemberGroupMemberMapper memberGroupMemberMapper;

    @Resource
    private IMemberRoleService memberRoleService;

    @GlobalTransactional
    @Override
    public Boolean doRegister(RegisterAccount registerAccount) {
        // 1.账号唯一性校验
        Account account = super.getOne(Account::getCode, registerAccount.getAccount());
        if (account != null) {
            throw new MicroBadRequestException("账号已存在");
        }

        // 2.检查账号是否存在用户信息
        Member member, addMember = new Member();
        if (AccountEnum.EMAIL.getValue() == registerAccount.getCategory()) {
            member = memberMapper.selectOne(Member::getEmail, registerAccount.getAccount());
            addMember.setEmail(registerAccount.getAccount());
        } else if (AccountEnum.MOBILE.getValue() == registerAccount.getCategory()) {
            member = memberMapper.selectOne(Member::getMobile, registerAccount.getAccount());
            addMember.setMobile(registerAccount.getAccount());
        } else {
            throw new MicroBadRequestException("非法账号类型");
        }

        if (member == null) {
            // 3.没有用户信息,则立即注册一条用户信息
            member = addMember;
            member.setSalt(MD5Utils.randomSalt());
            member.setPassword(MD5Utils.encode(registerAccount.getPassword(), member.getSalt()));
            if (memberMapper.insert(member) <= 0) {
                throw new MicroErrorException("用户注册失败");
            }
        }

        // 4.注册账号
        Account addAccount = new Account();
        addAccount.setMemberId(member.getId());
        addAccount.setCode(registerAccount.getAccount());
        addAccount.setCategory(registerAccount.getCategory());
        addAccount.setIp(IPUtils.getRequestIPAddress());
        addAccount.setPlatform(registerAccount.getPlatform());
        if (baseMapper.insert(addAccount) <= 0) {
            throw new MicroErrorException("账号注册失败");
        }

        // 5.分配用户组
        MemberGroupMember addMemberGroupMember = new MemberGroupMember();
        addMemberGroupMember.setMemberId(member.getId());
        addMemberGroupMember.setMemberGroupId(MemberGroupEnum.MEMBER.getValue());
        if (memberGroupMemberMapper.insert(addMemberGroupMember) <= 0) {
            throw new MicroErrorException("用户组分配失败");
        }

        return true;
    }

    @Override
    public MicroToken doLogin(LoginAccount loginAccount) {
        // 1.校验账号信息
        Account account = baseMapper.selectOne(Account::getCode, loginAccount.getAccount());
        if (account == null) {
            throw new MicroBadRequestException("账号不存在");
        }

        // 2.查询用户信息
        Member member = memberMapper.selectById(account.getMemberId());
        if (member == null) {
            throw new MicroBadRequestException("未找到用户");
        }

        // 3.校验密码
        if (MD5Utils.checkNotEquals(loginAccount.getPassword(), member.getPassword(), member.getSalt())) {
            throw new MicroBadRequestException("密码错误");
        }

        // 4.查询拥有角色
        List<String> roleCodes = memberRoleService.queryMemberRoles(account.getMemberId());
        if (CollectionUtils.isEmpty(roleCodes)) {
            throw new MicroBadRequestException("用户未分配角色或用户组");
        }

        // 5.组装响应模型
        return MicroAuthContext.build(member.getTenantId(), member.getId(),
                member.getName(), loginAccount.getPlatform(), roleCodes);
    }

}
