package cn.micro.biz.pubsrv.oss;

import com.qiniu.common.Constants;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * Oss Properties
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro.oss.qi-niu")
public class QiNiuOssProperties implements Serializable {

    private boolean enable;
    private String bucket;
    /**
     * 有效时长，单位秒
     */
    private long tokenExpiresSec;
    private String accessUrl;
    private String accessKey;
    private String secretKey;

    /**
     * 上传失败重试次数
     */
    private int retryMax = 5;
    /**
     * 如果文件大小大于此值则使用断点上传, 否则使用Form上传
     */
    private int putThreshold = Constants.BLOCK_SIZE;
    /**
     * 回复超时时间 单位秒(默认30s)
     */
    private int readTimeoutSec = Constants.READ_TIMEOUT;
    /**
     * 写超时时间 单位秒(默认 0 , 不超时)
     */
    private int writeTimeoutSec = Constants.WRITE_TIMEOUT;
    /**
     * 连接超时时间 单位秒(默认10s)
     */
    private int connectTimeoutSec = Constants.CONNECT_TIMEOUT;

}
