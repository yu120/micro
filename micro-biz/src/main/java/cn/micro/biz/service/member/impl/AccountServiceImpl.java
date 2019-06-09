package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.commons.auth.MicroTokenBody;
import cn.micro.biz.commons.enums.IEnum;
import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import cn.micro.biz.commons.exception.support.MicroErrorException;
import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.commons.utils.RsaUtils;
import cn.micro.biz.entity.member.Account;
import cn.micro.biz.entity.member.Member;
import cn.micro.biz.entity.member.MemberGroupMember;
import cn.micro.biz.entity.unified.LoginLog;
import cn.micro.biz.mapper.member.IAccountMapper;
import cn.micro.biz.mapper.member.IMemberGroupMemberMapper;
import cn.micro.biz.mapper.member.IMemberMapper;
import cn.micro.biz.model.add.RegisterAccount;
import cn.micro.biz.model.edit.ChangeEmailOrMobile;
import cn.micro.biz.model.edit.ChangePassword;
import cn.micro.biz.model.edit.ForgetPassword;
import cn.micro.biz.model.query.LoginAccount;
import cn.micro.biz.pubsrv.wx.MicroWxService;
import cn.micro.biz.pubsrv.wx.WxAuthCode2Session;
import cn.micro.biz.service.member.IAccountService;
import cn.micro.biz.service.member.IMemberRoleService;
import cn.micro.biz.service.unified.IUnionCodeService;
import cn.micro.biz.service.unified.ILoginLogService;
import cn.micro.biz.type.unified.UnionCodeCategoryEnum;
import cn.micro.biz.type.member.AccountEnum;
import cn.micro.biz.type.member.MemberGroupEnum;
import cn.micro.biz.type.member.MemberStatusEnum;
import cn.micro.biz.type.member.PlatformEnum;
import cn.micro.biz.type.unified.LoginResultEnum;
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
    private IUnionCodeService unionCodeService;
    @Resource
    private ILoginLogService loginLogService;

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
        if (AccountEnum.WX_AUTO_LOGIN == registerAccount.getCategory()) {
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
            if (AccountEnum.MOBILE == registerAccount.getCategory()) {
                member = memberMapper.selectOne(Member::getMobile, registerAccount.getAccount());
                addOrUpdateMember.setMobile(registerAccount.getAccount());
            } else if (AccountEnum.EMAIL == registerAccount.getCategory()) {
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
            addOrUpdateMember.setStatus(MemberStatusEnum.NORMAL);
            addOrUpdateMember.setSalt(RsaUtils.randomSalt());
            addOrUpdateMember.setPwd(RsaUtils.encryptPwd(registerAccount.getPassword(), addOrUpdateMember.getSalt()));
            addOrUpdateMember.setPassword(RsaUtils.encryptPassword(addOrUpdateMember.getPwd()));
            addOrUpdateMember.setIp(MicroAuthContext.getRequestIPAddress());
            addOrUpdateMember.setPlatform(registerAccount.getPlatform());
            if (memberMapper.insert(addOrUpdateMember) <= 0) {
                throw new MicroErrorException("用户注册失败");
            }

            // 4.注册账号
            Account addAccount = new Account();
            addAccount.setMemberId(addOrUpdateMember.getId());
            addAccount.setCode(registerAccount.getAccount());
            addAccount.setCategory(IEnum.parse(AccountEnum.class, registerAccount.getCategory()));
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
        // 记录登录信息
        LoginLog loginLog = new LoginLog();
        loginLog.setResult(LoginResultEnum.SUCCESS);
        loginLog.setIp(MicroAuthContext.getRequestIPAddress());
        loginLog.setAccount(loginAccount.getAccount());
        loginLog.setPlatform(loginAccount.getPlatform());

        try {
            // 1.校验账号信息
            Account account = baseMapper.selectOne(Account::getCode, loginAccount.getAccount());
            if (account == null) {
                throw new MicroBadRequestException("账号不存在");
            }
            loginLog.setCategory(account.getCategory());

            // 2.查询用户信息
            Member member = memberMapper.selectById(account.getMemberId());
            if (member == null) {
                throw new MicroBadRequestException("未找到用户");
            }
            loginLog.setMemberId(member.getId());

            // 3.校验密码
            if (RsaUtils.checkNotEquals(loginAccount.getPassword(), member.getPassword(), member.getSalt())) {
                throw new MicroBadRequestException("密码错误");
            }

            // 4.查询拥有角色
            List<String> roleCodes = memberRoleService.queryMemberRoles(account.getMemberId());
            if (CollectionUtils.isEmpty(roleCodes)) {
                throw new MicroBadRequestException("用户未分配角色或用户组");
            }

            // 5.组装响应模型
            return MicroAuthContext.build(member.getTenantId(), member.getId(),
                    member.getName(), loginAccount.getPlatform().getValue(), roleCodes, null);
        } catch (MicroBadRequestException e) {
            loginLog.setResult(LoginResultEnum.FAILURE);
            loginLog.setRemark(e.getStack());
            throw e;
        } catch (Exception e) {
            loginLog.setResult(LoginResultEnum.UNKNOWN_FAILURE);
            loginLog.setRemark(e.getMessage());
            throw e;
        } finally {
            loginLogService.save(loginLog);
        }
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
                    registerAccount.setCategory(AccountEnum.WX_AUTO_LOGIN);
                    registerAccount.setPlatform(PlatformEnum.WX);
                    registerAccount.setPassword(RsaUtils.encryptByPublicKeyHex(wxAuthCode2Session.getUnionId()));
                    if (this.doRegister(registerAccount)) {
                        wxAuthCode2Session.setHasAccount(true);
                    }
                }
            }
        }

        return wxAuthCode2Session;
    }

    @Override
    public MicroToken doRefreshToken() {
        // parse token
        MicroTokenBody microTokenBody = MicroAuthContext.getContextRefreshToken();

        // refresh data
        Member member = memberMapper.selectById(microTokenBody.getMemberId());
        if (member == null) {
            throw new MicroBadRequestException("用户不存在");
        }

        // 查询拥有角色
        List<String> roleCodes = memberRoleService.queryMemberRoles(member.getId());
        if (CollectionUtils.isEmpty(roleCodes)) {
            throw new MicroBadRequestException("用户未分配角色或用户组");
        }
        microTokenBody.setMemberName(member.getName());
        microTokenBody.setAuthorities(roleCodes);

        // build new token
        return MicroAuthContext.build(
                microTokenBody.getTenantId(),
                microTokenBody.getMemberId(),
                microTokenBody.getMemberName(),
                microTokenBody.getDeviceType(),
                microTokenBody.getAuthorities(),
                microTokenBody.getOthers());
    }

    @Override
    public Boolean doForgetPassword(ForgetPassword forgetPassword) {
        // 校验验证码
        unionCodeService.checkCode(UnionCodeCategoryEnum.FORGET_PASSWORD,
                forgetPassword.getAccount(), forgetPassword.getCode());

        // 查询账号信息
        Member member = memberMapper.selectOne(Member::getEmail, forgetPassword.getAccount());
        if (member == null) {
            throw new MicroBadRequestException("账号不存在");
        }

        // 重置密码
        Member updateMember = new Member();
        updateMember.setId(member.getId());
        updateMember.setPwd(RsaUtils.encryptPwd(forgetPassword.getPassword(), member.getSalt()));
        updateMember.setPassword(RsaUtils.encryptPassword(updateMember.getPwd()));
        if (memberMapper.updateById(updateMember) <= 0) {
            throw new MicroErrorException("重置密码失败");
        }

        return true;
    }

    @Override
    public Boolean doChangePassword(ChangePassword changePassword) {
        // 查询当前用户信息
        Long memberId = MicroAuthContext.getMemberId();
        Member member = memberMapper.selectById(memberId);
        if (member == null) {
            throw new MicroBadRequestException("当前用户不可用");
        }

        // 校验密码
        if (RsaUtils.checkNotEquals(changePassword.getOldPassword(), member.getPassword(), member.getSalt())) {
            throw new MicroBadRequestException("旧密码错误");
        }

        // 修改密码
        Member updateMember = new Member();
        updateMember.setId(memberId);
        updateMember.setPwd(RsaUtils.encryptPwd(changePassword.getNewPassword(), member.getSalt()));
        updateMember.setPassword(RsaUtils.encryptPassword(updateMember.getPwd()));
        if (memberMapper.updateById(updateMember) <= 0) {
            throw new MicroErrorException("修改失败");
        }

        return true;
    }

    @GlobalTransactional
    @Override
    public Boolean doChangeAccount(ChangeEmailOrMobile changeEmailOrMobile) {
        // 1.验证码校验
        UnionCodeCategoryEnum unionCodeCategoryEnum;
        if (AccountEnum.EMAIL == changeEmailOrMobile.getCategory()) {
            unionCodeCategoryEnum = UnionCodeCategoryEnum.CHANGE_EMAIL;
        } else if (AccountEnum.MOBILE == changeEmailOrMobile.getCategory()) {
            unionCodeCategoryEnum = UnionCodeCategoryEnum.CHANGE_MOBILE;
        } else {
            throw new MicroBadRequestException("暂不支持修改该类型账号");
        }
        unionCodeService.checkCode(unionCodeCategoryEnum, changeEmailOrMobile.getAccount(), changeEmailOrMobile.getCode());

        // 2.检查账号信息中的邮箱地址
        Account account = super.getOne(Account::getCategory, changeEmailOrMobile.getCategory(),
                Account::getCode, changeEmailOrMobile.getAccount());
        if (account != null) {
            throw new MicroBadRequestException("该邮箱地址已被占用");
        }

        // 3.检查用户信息中的邮箱地址或手机号是否已被占用
        if (AccountEnum.EMAIL == changeEmailOrMobile.getCategory()) {
            Member member = memberMapper.selectOne(Member::getEmail, changeEmailOrMobile.getAccount());
            if (member != null) {
                throw new MicroBadRequestException("该邮箱地址已被占用");
            }
        } else if (AccountEnum.MOBILE == changeEmailOrMobile.getCategory()) {
            Member member = memberMapper.selectOne(Member::getMobile, changeEmailOrMobile.getAccount());
            if (member != null) {
                throw new MicroBadRequestException("该手机号已被占用");
            }
        }

        // 4.查询当前用户需要修改的账号记录
        Long currentMemberId = MicroAuthContext.getMemberId();
        Account currentAccount = super.getOne(Account::getMemberId, currentMemberId,
                Account::getCategory, changeEmailOrMobile.getCategory());
        if (currentAccount == null) {
            // 5.1.新增登录账号
            Account addAccount = new Account();
            addAccount.setMemberId(currentMemberId);
            addAccount.setCategory(IEnum.parse(AccountEnum.class, changeEmailOrMobile.getCategory()));
            addAccount.setCode(changeEmailOrMobile.getAccount());
            if (!super.save(addAccount)) {
                throw new MicroBadRequestException("修改失败");
            }

            // 6.1.修改用户信息中的邮箱地址
            Member updateMember = new Member();
            updateMember.setId(currentMemberId);
            updateMember.setEmail(changeEmailOrMobile.getAccount());
            if (memberMapper.updateById(updateMember) > 0) {
                throw new MicroBadRequestException("修改失败");
            }
        } else {
            // 5.2.修改登录账号
            Account updateAccount = new Account();
            updateAccount.setId(currentAccount.getId());
            updateAccount.setCategory(IEnum.parse(AccountEnum.class, changeEmailOrMobile.getCategory()));
            updateAccount.setCode(changeEmailOrMobile.getAccount());
            if (!super.updateById(updateAccount)) {
                throw new MicroBadRequestException("修改失败");
            }

            // 6.2.修改用户信息中的手机号码
            Member updateMember = new Member();
            updateMember.setId(currentMemberId);
            updateMember.setMobile(changeEmailOrMobile.getAccount());
            if (memberMapper.updateById(updateMember) > 0) {
                throw new MicroBadRequestException("修改失败");
            }
        }

        return true;
    }

    @Override
    public Boolean doRegistered(String account) {
        return super.getOne(Account::getCode, account) != null;
    }

}
