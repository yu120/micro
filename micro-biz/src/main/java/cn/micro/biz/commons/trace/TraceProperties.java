package cn.micro.biz.commons.trace;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Trace Properties
 *
 * @author lry
 */
@Data
@ToString
@ConfigurationProperties(prefix = "micro.trace")
public class TraceProperties implements Serializable {

    /**
     * Trace enable
     */
    private boolean enable;
    /**
     * 多少毫秒会打印耗时日志的时间阀值
     */
    private Duration threshold = Duration.ofMillis(300L);
    /**
     * 是否打印输入与输出参数
     */
    private boolean printArgs;
    /**
     * 打印日志的时间间隔，默认为60秒(单位：秒)
     */
    private Duration dumpPeriod = Duration.ofSeconds(60L);
    /**
     * 是否启用默认规则
     */
    private boolean defaultExpressions = true;
    /**
     * AOP拦截前缀
     */
    private String[] packagePrefixes;
    /**
     * 自定义AOP拦截规则
     */
    private List<String> expressions = new ArrayList<>();

    /**
     * 请求链路缓存开关
     */
    private boolean cacheEnable = true;
    /**
     * 最多缓存请求数量
     */
    private long cacheMaximumSize = 500;
    /**
     * 缓存写入后过期时间
     */
    private Duration cacheExpireAfterWrite = Duration.ofSeconds(600L);

}
