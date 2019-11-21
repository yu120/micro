package cn.micro.biz.commons.response;

import cn.micro.biz.commons.exception.MicroStatusCode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * ReturnValueConfiguration
 *
 * @author lry
 * @see GlobalResponseBodyAdvice
 */
//@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReturnValueConfiguration implements InitializingBean {

    private final RequestMappingHandlerAdapter adapter;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> handlers = adapter.getReturnValueHandlers();
        if (CollectionUtils.isEmpty(handlers)) {
            return;
        }

        List<HandlerMethodReturnValueHandler> list = new ArrayList<>(handlers.size());
        for (HandlerMethodReturnValueHandler returnValueHandler : handlers) {
            if (returnValueHandler instanceof RequestResponseBodyMethodProcessor) {
                list.add(new HandlerMethodReturnValueHandler() {
                    @Override
                    public boolean supportsReturnType(MethodParameter returnType) {
                        return returnValueHandler.supportsReturnType(returnType);
                    }

                    @Override
                    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                                  ModelAndViewContainer mavContainer,
                                                  NativeWebRequest webRequest) throws Exception {
                        MetaData metaData = MicroStatusCode.buildSuccess(returnValue);
                        returnValueHandler.handleReturnValue(metaData, returnType, mavContainer, webRequest);
                    }
                });
                continue;
            }

            list.add(returnValueHandler);
        }

        adapter.setReturnValueHandlers(list);
    }

}
