package cn.micro.biz.service.unified.impl;

import cn.micro.biz.commons.exception.support.MicroBadRequestException;
import cn.micro.biz.entity.unified.UnionCodeEntity;
import cn.micro.biz.mapper.unified.IUnionCodeMapper;
import cn.micro.biz.pubsrv.email.EmailMessage;
import cn.micro.biz.pubsrv.email.EmailService;
import cn.micro.biz.service.unified.IUnionCodeService;
import cn.micro.biz.type.unified.EmailCategoryEnum;
import cn.micro.biz.type.unified.UnionCodeCategoryEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Union Code Service Implements
 *
 * @author lry
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UnionCodeServiceImpl extends ServiceImpl<IUnionCodeMapper, UnionCodeEntity> implements IUnionCodeService {

    private final EmailService emailService;

    @Override
    public void checkCode(UnionCodeCategoryEnum unionCodeCategoryEnum, String account, String code) {
        UnionCodeEntity queryUnionCodeEntity = new UnionCodeEntity();
        queryUnionCodeEntity.setCategory(unionCodeCategoryEnum.getValue());
        queryUnionCodeEntity.setAccount(account);
        UnionCodeEntity unionCode = super.getOne(Wrappers.query(queryUnionCodeEntity));
        if (unionCode == null) {
            throw new MicroBadRequestException("验证码不存在");
        }
        if (!unionCode.getCode().equals(code)) {
            // 验证失败，则失败次数+1
            UnionCodeEntity updateUnionCode = new UnionCodeEntity();
            updateUnionCode.setId(unionCode.getId());
            updateUnionCode.setFailTimes(unionCode.getFailTimes() + 1);
            super.updateById(updateUnionCode);

            // 失败次数达到上限后,直接删除验证码
            if (updateUnionCode.getFailTimes() >= unionCode.getMaxTimes()) {
                super.removeById(unionCode.getId());
            }

            throw new MicroBadRequestException("验证码错误");
        }

        // 删除验证成功后的验证码
        super.removeById(unionCode.getId());
    }

    @Override
    public boolean sendCodeMail(Integer category, String email) {
        UnionCodeCategoryEnum unionCodeCategoryEnum = UnionCodeCategoryEnum.get(category);
        EmailCategoryEnum emailCategoryEnum = EmailCategoryEnum.get(unionCodeCategoryEnum.getCategory());

        // 清理历史验证码
        QueryWrapper<UnionCodeEntity> lambdaQueryWrapper = Wrappers.query();
        UnionCodeEntity queryUnionCodeEntity = new UnionCodeEntity();
        queryUnionCodeEntity.setCategory(category);
        queryUnionCodeEntity.setAccount(email);
        List<UnionCodeEntity> unionCodeList = super.list(Wrappers.query(queryUnionCodeEntity));
        if (CollectionUtils.isNotEmpty(unionCodeList)) {
            super.removeByIds(unionCodeList.stream().map(UnionCodeEntity::getId).collect(Collectors.toList()));
        }

        try {
            String captcha = this.getFixLengthCode(6);
            UnionCodeEntity unionCode = new UnionCodeEntity();
            unionCode.setAccount(email);
            unionCode.setCode(captcha);
            unionCode.setMaxTimes(unionCodeCategoryEnum.getMaxTimes());
            unionCode.setFailTimes(0);
            unionCode.setCategory(category);
            unionCode.setExpire(unionCodeCategoryEnum.getExpire());
            unionCode.setStartTime(new Date());
            if (super.save(unionCode)) {
                EmailMessage emailMessage = new EmailMessage();
                emailMessage.setRecipients(Collections.singletonList(email));
                emailMessage.setTemplate(emailCategoryEnum.getTemplate());
                emailMessage.setSubject(emailCategoryEnum.getSubject());
                Map<String, Object> map = new HashMap<>();
                map.put("title", emailCategoryEnum.getTitle());
                map.put("email", email);
                map.put("category", emailCategoryEnum.getCategory());
                map.put("captcha", captcha);
                emailMessage.setVariables(map);
                return emailService.sendSimpleMail(emailMessage);
            }
        } catch (Exception e) {
            log.error("发送邮件异常", e);
        }

        return false;
    }

    /**
     * 返回长度为【strLength】的随机数，在前面补0
     *
     * @param length code length
     * @return code string
     */
    private String getFixLengthCode(int length) {
        double num = (1 + new Random().nextDouble()) * Math.pow(10, length);
        String fixLengthString = String.valueOf(num);
        return fixLengthString.substring(1, length + 1);
    }

}
