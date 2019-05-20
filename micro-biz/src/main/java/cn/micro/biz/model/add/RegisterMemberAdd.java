package cn.micro.biz.model.add;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class RegisterMemberAdd implements Serializable {

    private String name;
    private String account;
    private String password;
    private Integer type;

}
