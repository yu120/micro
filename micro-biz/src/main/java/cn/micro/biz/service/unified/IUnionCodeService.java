package cn.micro.biz.service.unified;

import cn.micro.biz.entity.unified.UnionCodeEntity;
import cn.micro.biz.type.unified.UnionCodeCategoryEnum;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * Union Code Service
 *
 * @author lry
 */
public interface IUnionCodeService extends IService<UnionCodeEntity> {

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
     * @param category {@link UnionCodeCategoryEnum}
     * @param email    email
     * @return true表示发送成功
     */
    boolean sendCodeMail(Integer category, String email);

}
