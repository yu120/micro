package cn.micro.biz.model.query;

import cn.micro.biz.type.member.PlatformEnum;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class LoginAccount implements Serializable {

    private String account;
    private String password;

    /**
     * Account register platform
     * <p>
     * {@link cn.micro.biz.type.member.PlatformEnum}
     */
    private PlatformEnum platform;

}
