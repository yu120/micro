package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.auth.MicroToken;
import cn.micro.biz.commons.auth.MicroTokenBody;
import cn.micro.biz.commons.enums.IEnum;
import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import cn.micro.biz.commons.exception.support.MicroErrorException;
import cn.micro.biz.commons.utils.RsaUtils;
import cn.micro.biz.entity.member.AccountEntity;
import cn.micro.biz.entity.member.MemberEntity;
import cn.micro.biz.entity.member.MemberGroupMemberEntity;
import cn.micro.biz.entity.unified.LoginLogEntity;
import cn.micro.biz.mapper.member.IAccountMapper;
import cn.micro.biz.mapper.member.IMemberGroupMemberMapper;
import cn.micro.biz.mapper.member.IMemberMapper;
import cn.micro.biz.mapper.unified.ILoginLogMapper;
import cn.micro.biz.model.add.RegisterAccount;
import cn.micro.biz.model.edit.ChangeEmailOrMobile;
import cn.micro.biz.model.edit.ChangePassword;
import cn.micro.biz.model.edit.ForgetPassword;
import cn.micro.biz.model.query.LoginAccount;
import cn.micro.biz.pubsrv.wechat.WeChatMiniProgram;
import cn.micro.biz.pubsrv.wechat.response.WeChatCode2SessionResponse;
import cn.micro.biz.pubsrv.wechat.MicroWxProperties;
import cn.micro.biz.service.member.IAccountService;
import cn.micro.biz.service.member.IMemberRoleService;
import cn.micro.biz.service.unified.IUnionCodeService;
import cn.micro.biz.type.unified.UnionCodeCategoryEnum;
import cn.micro.biz.type.member.AccountEnum;
import cn.micro.biz.type.member.MemberGroupEnum;
import cn.micro.biz.type.member.MemberStatusEnum;
import cn.micro.biz.type.member.PlatformEnum;
import cn.micro.biz.type.unified.LoginResultEnum;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(MicroWxProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountServiceImpl extends ServiceImpl<IAccountMapper, AccountEntity> implements IAccountService {

    private final IMemberMapper memberMapper;
    private final ILoginLogMapper loginLogMapper;
    private final IMemberGroupMemberMapper memberGroupMemberMapper;
    private final MicroWxProperties properties;

    @Resource
    private IMemberRoleService memberRoleService;
    @Resource
    private IUnionCodeService unionCodeService;

    @GlobalTransactional
    @Override
    public Boolean doRegister(RegisterAccount registerAccount) {
        // 1.账号唯一性校验
        AccountEntity queryAccountEntity = new AccountEntity();
        queryAccountEntity.setCategory(registerAccount.getCategory());
        queryAccountEntity.setCode(registerAccount.getAccount());
        AccountEntity account = super.getOne(Wrappers.query(queryAccountEntity));

        // 2.检查账号是否存在用户信息
        MemberEntity addOrUpdateMember = new MemberEntity();
        if (AccountEnum.WE_CHAT == registerAccount.getCategory()) {
            // 2.1.微信自动登录
            if (account != null) {
                addOrUpdateMember = memberMapper.selectById(account.getMemberId());
                if (addOrUpdateMember == null) {
                    addOrUpdateMember = new MemberEntity();
                }
            }
        } else {
            // 2.2.非微信自动登录
            if (account != null) {
                throw new MicroBadRequestException("账号已存在");
            }
            MemberEntity member;
            if (AccountEnum.MOBILE == registerAccount.getCategory()) {
                MemberEntity queryMemberEntity = new MemberEntity();
                queryMemberEntity.setMobile(registerAccount.getAccount());
                member = memberMapper.selectOne(Wrappers.query(queryMemberEntity));
                addOrUpdateMember.setMobile(registerAccount.getAccount());
            } else if (AccountEnum.EMAIL == registerAccount.getCategory()) {
                MemberEntity queryMemberEntity = new MemberEntity();
                queryMemberEntity.setEmail(registerAccount.getAccount());
                member = memberMapper.selectOne(Wrappers.query(queryMemberEntity));
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
            AccountEntity addAccount = new AccountEntity();
            addAccount.setMemberId(addOrUpdateMember.getId());
            addAccount.setCode(registerAccount.getAccount());
            addAccount.setCategory(IEnum.parse(AccountEnum.class, registerAccount.getCategory()));
            if (baseMapper.insert(addAccount) <= 0) {
                throw new MicroErrorException("账号注册失败");
            }

            // 5.分配用户组
            MemberGroupMemberEntity addMemberGroupMember = new MemberGroupMemberEntity();
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
        LoginLogEntity loginLog = new LoginLogEntity();
        loginLog.setResult(LoginResultEnum.SUCCESS);
        loginLog.setIp(MicroAuthContext.getRequestIPAddress());
        loginLog.setAccount(loginAccount.getAccount());
        loginLog.setPlatform(loginAccount.getPlatform());

        try {
            // 1.校验账号信息
            AccountEntity queryAccountEntity = new AccountEntity();
            queryAccountEntity.setCode(loginAccount.getAccount());
            AccountEntity account = baseMapper.selectOne(Wrappers.query(queryAccountEntity));
            if (account == null) {
                throw new MicroBadRequestException("账号不存在");
            }
            loginLog.setCategory(account.getCategory());

            // 2.查询用户信息
            MemberEntity member = memberMapper.selectById(account.getMemberId());
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
            return MicroAuthContext.build(member.getId(),
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
            loginLogMapper.insert(loginLog);
        }
    }

    @Override
    public WeChatCode2SessionResponse weChatLogin(String code) {
        WeChatMiniProgram weChatMiniProgram = new WeChatMiniProgram(properties.getAppId(), properties.getSecret());
        WeChatCode2SessionResponse response = weChatMiniProgram.authCode2Session(code);

        // 查询账号是否已创建
        AccountEntity queryAccountEntity = new AccountEntity();
        queryAccountEntity.setCategory(AccountEnum.WE_CHAT);
        queryAccountEntity.setCode(response.getUnionId());
        AccountEntity account = baseMapper.selectOne(new QueryWrapper<>(queryAccountEntity));

        RegisterAccount registerAccount = new RegisterAccount();
        registerAccount.setAccount(response.getUnionId());
        registerAccount.setCategory(AccountEnum.WE_CHAT);
        registerAccount.setPlatform(PlatformEnum.WX);
        registerAccount.setPassword(RsaUtils.encryptByPublicKeyHex(response.getUnionId()));
        if (this.doRegister(registerAccount)) {

        }

        return response;
    }

    @Override
    public MicroToken doRefreshToken() {
        // parse token
        MicroTokenBody microTokenBody = MicroAuthContext.getContextRefreshToken();

        // refresh data
        MemberEntity member = memberMapper.selectById(microTokenBody.getMembersId());
        if (member == null) {
            throw new MicroBadRequestException("用户不存在");
        }

        // 查询拥有角色
        List<String> roleCodes = memberRoleService.queryMemberRoles(member.getId());
        if (CollectionUtils.isEmpty(roleCodes)) {
            throw new MicroBadRequestException("用户未分配角色或用户组");
        }
        microTokenBody.setMembersName(member.getName());
        microTokenBody.setAuthorities(roleCodes);

        // build new token
        return MicroAuthContext.build(
                microTokenBody.getMembersId(),
                microTokenBody.getMembersName(),
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
        MemberEntity queryMemberEntity = new MemberEntity();
        queryMemberEntity.setEmail(forgetPassword.getAccount());
        MemberEntity member = memberMapper.selectOne(Wrappers.query(queryMemberEntity));
        if (member == null) {
            throw new MicroBadRequestException("账号不存在");
        }

        // 重置密码
        MemberEntity updateMember = new MemberEntity();
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
        Long memberId = MicroAuthContext.getMembersId();
        MemberEntity member = memberMapper.selectById(memberId);
        if (member == null) {
            throw new MicroBadRequestException("当前用户不可用");
        }

        // 校验密码
        if (RsaUtils.checkNotEquals(changePassword.getOldPassword(), member.getPassword(), member.getSalt())) {
            throw new MicroBadRequestException("旧密码错误");
        }

        // 修改密码
        MemberEntity updateMember = new MemberEntity();
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
        AccountEntity queryAccountEntity = new AccountEntity();
        queryAccountEntity.setCategory(changeEmailOrMobile.getCategory());
        queryAccountEntity.setCode(changeEmailOrMobile.getAccount());
        AccountEntity account = super.getOne(Wrappers.query(queryAccountEntity));
        if (account != null) {
            throw new MicroBadRequestException("该邮箱地址已被占用");
        }

        // 3.检查用户信息中的邮箱地址或手机号是否已被占用
        if (AccountEnum.EMAIL == changeEmailOrMobile.getCategory()) {
            MemberEntity queryMemberEntity = new MemberEntity();
            queryMemberEntity.setEmail(changeEmailOrMobile.getAccount());
            MemberEntity member = memberMapper.selectOne(Wrappers.query(queryMemberEntity));
            if (member != null) {
                throw new MicroBadRequestException("该邮箱地址已被占用");
            }
        } else if (AccountEnum.MOBILE == changeEmailOrMobile.getCategory()) {
            MemberEntity queryMemberEntity = new MemberEntity();
            queryMemberEntity.setMobile(changeEmailOrMobile.getAccount());
            MemberEntity member = memberMapper.selectOne(Wrappers.query(queryMemberEntity));
            if (member != null) {
                throw new MicroBadRequestException("该手机号已被占用");
            }
        }

        // 4.查询当前用户需要修改的账号记录
        Long currentMemberId = MicroAuthContext.getMembersId();
        AccountEntity queryCurrentAccountEntity = new AccountEntity();
        queryCurrentAccountEntity.setMemberId(currentMemberId);
        queryCurrentAccountEntity.setCategory(changeEmailOrMobile.getCategory());
        AccountEntity currentAccount = super.getOne(Wrappers.query(queryCurrentAccountEntity));
        if (currentAccount == null) {
            // 5.1.新增登录账号
            AccountEntity addAccount = new AccountEntity();
            addAccount.setMemberId(currentMemberId);
            addAccount.setCategory(IEnum.parse(AccountEnum.class, changeEmailOrMobile.getCategory()));
            addAccount.setCode(changeEmailOrMobile.getAccount());
            if (!super.save(addAccount)) {
                throw new MicroBadRequestException("修改失败");
            }

            // 6.1.修改用户信息中的邮箱地址
            MemberEntity updateMember = new MemberEntity();
            updateMember.setId(currentMemberId);
            updateMember.setEmail(changeEmailOrMobile.getAccount());
            if (memberMapper.updateById(updateMember) > 0) {
                throw new MicroBadRequestException("修改失败");
            }
        } else {
            // 5.2.修改登录账号
            AccountEntity updateAccount = new AccountEntity();
            updateAccount.setId(currentAccount.getId());
            updateAccount.setCategory(IEnum.parse(AccountEnum.class, changeEmailOrMobile.getCategory()));
            updateAccount.setCode(changeEmailOrMobile.getAccount());
            if (!super.updateById(updateAccount)) {
                throw new MicroBadRequestException("修改失败");
            }

            // 6.2.修改用户信息中的手机号码
            MemberEntity updateMember = new MemberEntity();
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
        AccountEntity queryAccountEntity = new AccountEntity();
        queryAccountEntity.setCode(account);
        return super.getOne(Wrappers.query(queryAccountEntity)) != null;
    }

}
