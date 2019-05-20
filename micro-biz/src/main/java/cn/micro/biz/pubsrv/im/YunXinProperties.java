package cn.micro.biz.pubsrv.im;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * 网易云信IM配置信息
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro.im.yun-xin")
public class YunXinProperties implements Serializable {

    private boolean enable = true;
    private String uri = "https://api.netease.im/nimserver";
    private String appKey = "6bcb72cef156479228f565653925df84";
    private String appSecret = "765cccb51757";

    /**
     * 事件处理线程池(固定线程池)大小
     */
    private int eventThreadNum = 5;
    /**
     * 必须为2的N次方
     */
    private int eventRingBufferSize = 1024;

    // ===== 异常事件处理参数

    private int failCacheConcurrencyLevel = 5;
    private int failCacheMaximumSize = 1000;
    private long failCacheExpireAfterWriteSec = 600L;
    private long failPeriodSec = 60L;

}
