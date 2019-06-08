package cn.micro.biz.commons.mybatis.p6spy;

import com.p6spy.engine.spy.P6SpyDriver;

public class MicroP6SpyDriver extends P6SpyDriver {

    @Override
    public boolean acceptsURL(String url) {
        return url != null;
    }

}
