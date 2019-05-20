package cn.micro.biz.pubsrv.im.model.user;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class YunXinUserCreate implements Serializable {

    /**
     * 网易云通信ID，最大长度32字符，必须保证一个APP内唯一
     * <p>
     * 【必选】
     * （只允许字母、数字、半角下划线_、@、半角点以及半角-组成，不区分大小写，会统一小写处理，请注意以此接口返回结果中的accid为准）
     */
    private String accid;
    private String name;
    private String props;
    private String icon;
    private String token;
    private String sign;
    private String email;
    private String birth;
    private String mobile;
    /**
     * 用户性别:0表示未知，1表示男，2表示女，其它会报参数错误
     */
    private Integer gender;
    private String ex;

}
