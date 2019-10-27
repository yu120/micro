package cn.micro.biz.pubsrv.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * Event Collect Resolver
 *
 * @author lry
 */
@Slf4j
@Aspect
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventCollectResolver {

    private final EventCollectFactory asyncEventFactory;

    @Pointcut("@annotation(cn.micro.biz.pubsrv.event.MicroEvent)")
    public void pointcutService() {
    }

    @Before(value = "pointcutService()&& @annotation(microEvent)")
    public void handlerEventBefore(JoinPoint joinPoint, MicroEvent microEvent) {
        if (MicroEvent.EventPost.BEFORE.equals(microEvent.advice())) {
            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            Method method = ms.getMethod();
            asyncEventFactory.publishEvent(microEvent, method.getName(), method.getName(), joinPoint.getArgs(), null);
        }
    }

    @AfterReturning(value = "pointcutService()&& @annotation(microEvent)", returning = "result")
    public void handlerEventAfter(JoinPoint joinPoint, MicroEvent microEvent, Object result) {
        if (MicroEvent.EventPost.AFTER.equals(microEvent.advice())) {
            MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            Method method = ms.getMethod();
            asyncEventFactory.publishEvent(microEvent, method.getName(), method.getName(), joinPoint.getArgs(), result);
        }
    }

}
