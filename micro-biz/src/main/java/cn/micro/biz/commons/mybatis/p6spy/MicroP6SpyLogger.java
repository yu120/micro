package cn.micro.biz.commons.mybatis.p6spy;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.apache.commons.lang3.StringUtils;

/**
 * P6spy SQL 打印策略
 *
 * @author lry
 */
public class MicroP6SpyLogger implements MessageFormattingStrategy {

    @Override
    public String formatMessage(int connectionId, String now, long elapsed,
                                String category, String prepared, String sql, String url) {
        if (StringUtils.isBlank(sql)) {
            return null;
        }

        return "\n========================================" +
                "\nConsume Time：" + elapsed + " ms " + now +
                "\nExecute SQL：" + sql.replaceAll("[\\s]+", " ") +
                "\n========================================";
    }

}
