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
import cn.micro.biz.pubsrv.wx.MicroWxService;
import cn.micro.biz.pubsrv.wx.WxAuthCode2Session;
import cn.micro.biz.service.member.IAccountService;
import cn.micro.biz.service.member.IMemberRoleService;
import cn.micro.biz.type.member.AccountEnum;
import cn.micro.biz.type.member.MemberGroupEnum;
import cn.micro.biz.type.member.PlatformEnum;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Account Service Implements
 *
 * @author lry
 */
@Slf4j
@Service
public class AccountServiceImpl extends MicroServiceImpl<IAccountMapper, Account> implements IAccountService {

    @Resource
    private IMemberMapper memberMapper;
    @Resource
    private IMemberGroupMemberMapper memberGroupMemberMapper;

    @Resource
    private IMemberRoleService memberRoleService;
    @Resource
    private MicroWxService microWxService;

    @GlobalTransactional
    @Override
    public Boolean doRegister(RegisterAccount registerAccount) {
        // 1.账号唯一性校验
        Account account = super.getOne(Account::getCategory, registerAccount.getCategory(),
                Account::getCode, registerAccount.getAccount());

        // 2.检查账号是否存在用户信息
        Member addOrUpdateMember = new Member();
        if (AccountEnum.WX_AUTO_LOGIN.getValue() == registerAccount.getCategory()) {
            // 2.1.微信自动登录
            if (account != null) {
                addOrUpdateMember = memberMapper.selectById(account.getMemberId());
                if (addOrUpdateMember == null) {
                    addOrUpdateMember = new Member();
                }
            }
        } else {
            // 2.2.非微信自动登录
            if (account != null) {
                throw new MicroBadRequestException("账号已存在");
            }
            Member member;
            if (AccountEnum.MOBILE.getValue() == registerAccount.getCategory()) {
                member = memberMapper.selectOne(Member::getMobile, registerAccount.getAccount());
                addOrUpdateMember.setMobile(registerAccount.getAccount());
            } else if (AccountEnum.EMAIL.getValue() == registerAccount.getCategory()) {
                member = memberMapper.selectOne(Member::getEmail, registerAccount.getAccount());
                addOrUpdateMember.setEmail(registerAccount.getAccount());
            } else {
                throw new MicroBadRequestException("非法账号类型");
            }
            if (member != null) {
                throw new MicroBadRequestException("用户已存在");
            }
        }

        if (addOrUpdateMember.getId() == null) {
            // 3.1.注册用户
            addOrUpdateMember.setName(registerAccount.getName());
            addOrUpdateMember.setIcon(registerAccount.getIcon());
            addOrUpdateMember.setSalt(MD5Utils.randomSalt());
            addOrUpdateMember.setPassword(MD5Utils.encode(registerAccount.getPassword(), addOrUpdateMember.getSalt()));
            if (memberMapper.insert(addOrUpdateMember) <= 0) {
                throw new MicroErrorException("用户注册失败");
            }

            // 4.注册账号
            Account addAccount = new Account();
            addAccount.setMemberId(addOrUpdateMember.getId());
            addAccount.setCode(registerAccount.getAccount());
            addAccount.setCategory(registerAccount.getCategory());
            addAccount.setIp(IPUtils.getRequestIPAddress());
            addAccount.setPlatform(registerAccount.getPlatform());
            if (baseMapper.insert(addAccount) <= 0) {
                throw new MicroErrorException("账号注册失败");
            }

            // 5.分配用户组
            MemberGroupMember addMemberGroupMember = new MemberGroupMember();
            addMemberGroupMember.setMemberId(addOrUpdateMember.getId());
            addMemberGroupMember.setMemberGroupId(MemberGroupEnum.MEMBER.getValue());
            if (memberGroupMemberMapper.insert(addMemberGroupMember) <= 0) {
                throw new MicroErrorException("用户组分配失败");
            }
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

    @Override
    public WxAuthCode2Session wxLogin(String code, boolean register) {
        WxAuthCode2Session wxAuthCode2Session = microWxService.wxLogin(code);
        if (wxAuthCode2Session != null) {
            if (StringUtils.isNotBlank(wxAuthCode2Session.getUnionId())) {
                // 判断账号是否已被注册
                Account account = super.getOne(Account::getCategory, AccountEnum.WX_AUTO_LOGIN.getValue(),
                        Account::getCode, wxAuthCode2Session.getUnionId());
                if (account != null) {
                    wxAuthCode2Session.setHasAccount(true);
                } else if (register) {
                    RegisterAccount registerAccount = new RegisterAccount();
                    registerAccount.setAccount(wxAuthCode2Session.getUnionId());
                    registerAccount.setCategory(AccountEnum.WX_AUTO_LOGIN.getValue());
                    registerAccount.setPlatform(PlatformEnum.WX.getValue());
                    registerAccount.setPassword(MD5Utils.encode(wxAuthCode2Session.getUnionId()));
                    this.doRegister(registerAccount);
                }
            }
        }

        return wxAuthCode2Session;
    }

}
