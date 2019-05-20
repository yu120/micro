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
import cn.micro.biz.model.add.RegisterMemberAdd;
import cn.micro.biz.model.query.LoginAccountQuery;
import cn.micro.biz.service.member.IAccountService;
import cn.micro.biz.type.AccountCategoryEnum;
import cn.micro.biz.type.RoleEnum;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 账号 服务实现类
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
    public Boolean addRegister(RegisterMemberAdd registerMemberAdd) throws Exception {
        // 账号唯一性校验
        Account account = super.getOneEqs(Account::getCode, registerMemberAdd.getAccount());
        if (account != null) {
            throw new MicroBadRequestException("账号已存在");
        }

        // 检查账号是否存在用户信息
        Long memberId;
        Member queryMember = this.buildMember(registerMemberAdd.getAccount(), registerMemberAdd.getType());
        Member member = memberMapper.selectOne(Wrappers.query(queryMember));
        if (member != null) {
            memberId = member.getId();
        } else {
            // 没有用户信息,则立即注册一条用户信息
            Member addMember = this.buildMember(registerMemberAdd.getAccount(), registerMemberAdd.getType());
            addMember.setSalt(MD5Utils.randomSalt());
            addMember.setPassword(MD5Utils.encode(registerMemberAdd.getPassword(), addMember.getSalt()));
            if (memberMapper.insert(addMember) <= 0) {
                throw new MicroErrorException("用户注册失败");
            }
            memberId = addMember.getId();
        }

        // 注册账号
        Account insertAccount = new Account();
        insertAccount.setMemberId(memberId);
        insertAccount.setCode(registerMemberAdd.getAccount());
        insertAccount.setIp(IPUtils.getRequestIPAddress());
        insertAccount.setCategory(registerMemberAdd.getType());
        if (baseMapper.insert(insertAccount) <= 0) {
            throw new MicroErrorException("账号注册失败");
        }

        // 分配角色
        MemberRole memberRole = new MemberRole();
        memberRole.setMemberId(memberId);
        memberRole.setRoleId(RoleEnum.ROLE_USER.getValue());
        if (memberRoleMapper.insert(memberRole) <= 0) {
            throw new MicroErrorException("角色分配失败");
        }

        return true;
    }

    @Override
    public MicroToken queryLogin(LoginAccountQuery loginAccountQuery) throws Exception {
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

    private Member buildMember(String account, Integer type) {
        Member member = new Member();
        if (AccountCategoryEnum.EMAIL.getValue() == type) {
            member.setEmail(account);
        } else if (AccountCategoryEnum.MOBILE.getValue() == type) {
            member.setMobile(account);
        } else if (AccountCategoryEnum.ID_CARD.getValue() == type) {
            member.setIdCard(account);
        }

        return member;
    }

}
