package cn.micro.biz.pubsrv.im.model.user;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class YunXinUpdateUinfo implements Serializable {

    private String accid;
    private String name;
    private String icon;
    private String sign;
    private String email;
    private String birth;
    private String mobile;
    private Integer gender;
    private String ex;

}
