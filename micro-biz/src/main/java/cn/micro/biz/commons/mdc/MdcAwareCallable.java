package cn.micro.biz.commons.mdc;

import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class MdcAwareCallable<T> implements Callable<T> {

    private final Callable<T> delegate;
    private final Map<String, String> contextMap;
    private final RequestAttributes requestAttributes;

    public MdcAwareCallable(Callable<T> callable, Map<String, String> contextMap, RequestAttributes requestAttributes) {
        this.delegate = callable;
        this.requestAttributes = requestAttributes;
        this.contextMap = contextMap != null ? contextMap : new HashMap<>();
    }

    @Override
    public T call() throws Exception {
        try {
            MDC.setContextMap(contextMap);
            if (requestAttributes != null) {
                RequestContextHolder.setRequestAttributes(requestAttributes);
            }
            return delegate.call();
        } finally {
            if (requestAttributes != null) {
                RequestContextHolder.resetRequestAttributes();
            }
            MDC.clear();
        }
    }

}
