package cn.micro.biz.model.add;

import cn.micro.biz.type.member.AccountEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class RegisterAccount implements Serializable {

    /**
     * Member name
     */
    private String name;
    /**
     * Member head image icon url
     * <p>
     * tip: 可选
     */
    private String icon;
    /**
     * Account category
     * <p>
     * {@link AccountEnum}
     */
    private Integer category;
    /**
     * Account code
     */
    private String account;
    /**
     * Account password
     * <p>
     * tip: 微信自动登录时需要加入随机密码
     */
    private String password;
    /**
     * Account register platform
     * <p>
     * {@link cn.micro.biz.type.member.PlatformEnum}
     */
    private Integer platform;

}
