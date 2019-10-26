package cn.micro.biz.pubsrv.email;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@ToString
public class EmailMessage implements Serializable {

    /**
     * 收件人列表
     */
    private List<String> recipients;

    /**
     * 模板名称
     */
    private String template;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 变量列表
     */
    private Map<String, Object> variables;

    /**
     * 附件列表
     */
    private List<String> attachment;

}
