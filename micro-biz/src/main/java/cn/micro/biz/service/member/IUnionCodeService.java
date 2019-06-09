package cn.micro.biz.service.member;

import cn.micro.biz.commons.mybatis.extension.IMicroService;
import cn.micro.biz.entity.unified.UnionCode;
import cn.micro.biz.type.UnionCodeCategoryEnum;

/**
 * Union Code Service
 *
 * @author lry
 */
public interface IUnionCodeService extends IMicroService<UnionCode> {

    /**
     * 校验验证码
     *
     * @param unionCodeCategoryEnum {@link UnionCodeCategoryEnum}
     * @param account               account
     * @param code                  code
     */
    void checkCode(UnionCodeCategoryEnum unionCodeCategoryEnum, String account, String code);

    /**
     * 发送邮箱验证码
     *
     * @param category {@link cn.micro.biz.type.UnionCodeCategoryEnum}
     * @param email    email
     * @return true表示发送成功
     */
    boolean sendCodeMail(Integer category, String email);

}
