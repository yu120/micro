package cn.micro.biz.pubsrv.event.spring;

import cn.micro.biz.pubsrv.event.EventCollectFactory;
import cn.micro.biz.pubsrv.event.MicroEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * Event Collect Resolver
 *
 * @author lry
 */
@Slf4j
@Aspect
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroEventCollectResolver {

    private final EventCollectFactory asyncEventFactory;

    @Pointcut("@annotation(cn.micro.biz.pubsrv.event.MicroEvent)")
    public void pointcutService() {
    }

    @Before(value = "pointcutService()&& @annotation(microEvent)")
    public void handlerEventBefore(JoinPoint joinPoint, MicroEvent microEvent) {
        if (MicroEvent.EventPost.BEFORE.equals(microEvent.advice())) {
            // collect method name
            String methodName = collectMethodName(joinPoint, microEvent);
            // collect class name
            String className = collectClassName(joinPoint, microEvent);
            asyncEventFactory.publish(microEvent, className, methodName, joinPoint.getArgs(), null);
        }
    }

    @AfterReturning(value = "pointcutService()&& @annotation(microEvent)", returning = "result")
    public void handlerEventAfter(JoinPoint joinPoint, MicroEvent microEvent, Object result) {
        if (MicroEvent.EventPost.AFTER.equals(microEvent.advice())) {
            // collect method name
            String methodName = collectMethodName(joinPoint, microEvent);
            // collect class name
            String className = collectClassName(joinPoint, microEvent);
            asyncEventFactory.publish(microEvent, className, methodName, joinPoint.getArgs(), result);
        }
    }

    /**
     * The collect method name
     *
     * @param joinPoint  {@link JoinPoint}
     * @param microEvent {@link MicroEvent}
     * @return method name
     */
    private String collectMethodName(JoinPoint joinPoint, MicroEvent microEvent) {
        if (microEvent.methodName()) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            return signature.getMethod().getName();
        }

        return null;
    }

    /**
     * The collect class name
     *
     * @param joinPoint  {@link JoinPoint}
     * @param microEvent {@link MicroEvent}
     * @return method name
     */
    private String collectClassName(JoinPoint joinPoint, MicroEvent microEvent) {
        if (microEvent.className()) {
            return joinPoint.getTarget().getClass().getName();
        }

        return null;
    }

}
