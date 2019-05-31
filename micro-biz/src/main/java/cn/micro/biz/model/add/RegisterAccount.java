package cn.micro.biz.model.add;

import cn.micro.biz.commons.validate.AssertEnum;
import cn.micro.biz.commons.validate.Length;
import cn.micro.biz.type.member.AccountEnum;
import cn.micro.biz.type.member.PlatformEnum;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Register Account
 *
 * @author lry
 */
@Data
@ToString
public class RegisterAccount implements Serializable {

    /**
     * Member name
     */
    @NotBlank(message = "姓名不能为空")
    private String name;
    /**
     * Account category
     * <p>
     * {@link AccountEnum}
     */
    @AssertEnum(AccountEnum.class)
    private Integer category;
    /**
     * Account register platform
     * <p>
     * {@link PlatformEnum}
     */
    @AssertEnum(PlatformEnum.class)
    private Integer platform;
    /**
     * Account code
     */
    @NotBlank(message = "账号不能为空")
    private String account;
    /**
     * Account password
     * <p>
     * tip: 微信自动登录时,请使用openId加密后的值进行填充
     */
    @NotBlank(message = "密码不能为空")
    @Length(length = 32)
    private String password;

    /**
     * Member head image icon url
     * <p>
     * tip: 可选
     */
    private String icon;

}
