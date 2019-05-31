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
public class ChangePassword implements Serializable {

    /**
     * Old Account password
     * <p>
     */
    @NotBlank(message = "旧密码不能为空")
    private String oldPassword;

    /**
     * New Account password
     * <p>
     */
    @NotBlank(message = "新密码不能为空")
    private String newPassword;

}
