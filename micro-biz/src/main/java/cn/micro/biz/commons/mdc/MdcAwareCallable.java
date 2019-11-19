package cn.micro.biz.commons.mdc;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * MdcAwareCallable
 *
 * @param <T>
 * @author lry
 */
public class MdcAwareCallable<T> implements Callable<T> {

    private final Callable<T> delegate;
    private final Map<String, String> contextMap;

    public MdcAwareCallable(Callable<T> callable, Map<String, String> contextMap) {
        this.delegate = callable;
        this.contextMap = contextMap != null ? contextMap : new HashMap<>();
    }

    @Override
    public T call() throws Exception {
        try {
            MDC.setContextMap(contextMap);
            return delegate.call();
        } finally {
            MDC.clear();
        }
    }

}
