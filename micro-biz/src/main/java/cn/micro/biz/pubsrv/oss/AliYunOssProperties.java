package cn.micro.biz.pubsrv.oss;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ToString
@ConfigurationProperties(prefix = "micro.oss.ali-yun")
public class AliYunOssProperties implements Serializable {

    private boolean enable;
    private String bucketName;
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String accessUrl;

}
