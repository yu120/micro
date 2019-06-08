package cn.micro.biz.commons.mybatis.p6spy;

import com.p6spy.engine.spy.P6ModuleManager;
import com.p6spy.engine.spy.P6SpyDriver;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.Slf4JLogger;

public class MicroP6SpyDriver extends P6SpyDriver {

    @Override
    public boolean acceptsURL(String url) {
        return url != null;
    }

    public static void initEnvironment() {
        P6SpyOptions p6SpyOptions = P6ModuleManager.getInstance().getOptions(P6SpyOptions.class);
        p6SpyOptions.setLogMessageFormat(MicroP6SpyLogger.class.getName());
        p6SpyOptions.setAppender(Slf4JLogger.class.getName());
        p6SpyOptions.setDateformat("yyyy-MM-dd HH:mm:ss");
    }

}
