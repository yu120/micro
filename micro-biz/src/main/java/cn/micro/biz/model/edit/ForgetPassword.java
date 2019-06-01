package cn.micro.biz.model.edit;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Change Password
 *
 * @author lry
 */
@Data
@ToString
public class ForgetPassword implements Serializable {

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
    /**
     * New Account password
     * <p>
     */
    @NotBlank(message = "新密码不能为空")
    private String password;

}
