package cn.micro.biz.model.edit;

import cn.micro.biz.commons.validate.AssertEnum;
import cn.micro.biz.type.member.AccountEnum;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Change Email or Mobile
 *
 * @author lry
 */
@Data
@ToString
public class ChangeEmailOrMobile implements Serializable {

    /**
     * Account category
     * <p>
     * {@link cn.micro.biz.type.member.AccountEnum}
     */
    @AssertEnum(AccountEnum.class)
    private Integer category;
    /**
     * Account eg:email or mobile
     * <p>
     */
    @NotBlank(message = "账号不能为空")
    private String account;
    /**
     * Code value
     */
    @NotBlank(message = "验证码不为空")
    private String code;

}
