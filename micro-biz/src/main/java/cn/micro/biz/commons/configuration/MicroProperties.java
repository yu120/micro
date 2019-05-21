package cn.micro.biz.commons.configuration;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * Micro Properties
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro")
public class MicroProperties implements Serializable {

    public static final String TIMESTAMP_KEY = "timestamp";
    public static final String SIGN_KEY = "sign";

    /**
     * 是否开启Swagger
     */
    private boolean swagger;
    /**
     * Stack max length
     */
    private int stackMaxLength = 2000;
    /**
     * 自动包装响应结构的扫描包路径
     */
    private String[] metaPackages;

}
