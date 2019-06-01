package cn.micro.biz.service.member.impl;

import cn.micro.biz.commons.auth.MicroAuthContext;
import cn.micro.biz.commons.mybatis.extension.MicroServiceImpl;
import cn.micro.biz.commons.utils.IPUtils;
import cn.micro.biz.entity.UnionCode;
import cn.micro.biz.mapper.member.IUnionCodeMapper;
import cn.micro.biz.pubsrv.email.EmailMessage;
import cn.micro.biz.pubsrv.email.EmailService;
import cn.micro.biz.service.member.IUnionCodeService;
import cn.micro.biz.type.EmailCategoryEnum;
import cn.micro.biz.type.UnionCodeCategoryEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Union Code Service Implements
 *
 * @author lry
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UnionCodeServiceImpl extends MicroServiceImpl<IUnionCodeMapper, UnionCode> implements IUnionCodeService {

    private final EmailService emailService;

    @Override
    public boolean sendMail(Integer category, String email) {
        EmailCategoryEnum emailCategoryEnum = EmailCategoryEnum.get(category);
        UnionCodeCategoryEnum unionCodeCategoryEnum = UnionCodeCategoryEnum.get(category);

        try {
            String captcha = this.getFixLengthCode(6);
            UnionCode unionCode = new UnionCode();
            unionCode.setMemberId(MicroAuthContext.getNonMemberId());
            unionCode.setIp(IPUtils.getRequestIPAddress());
            unionCode.setCode(captcha);
            unionCode.setCategory(unionCodeCategoryEnum.getValue());
            unionCode.setExpire(unionCodeCategoryEnum.getExpire());
            unionCode.setStartTime(new Date());
            if (this.save(unionCode)) {
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
