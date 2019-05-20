package cn.micro.biz.model.query;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class LoginAccountQuery implements Serializable {
    private Integer device;
    private String account;
    private String password;
}
