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

    @Before(value = "pointcutService()&& @annotation(guavaEvent)")
    public void handlerEventBefore(JoinPoint joinPoint, MicroEvent guavaEvent) {
        if (guavaEvent.enable()) {
            if (MicroEvent.EventPost.BEFORE.equals(guavaEvent.advice())) {
                MethodSignature ms = (MethodSignature) joinPoint.getSignature();
                Method method = ms.getMethod();
                asyncEventFactory.publishEvent(guavaEvent, method.getName(), method.getName(), joinPoint.getArgs(), null);
            }
        }
    }

    @AfterReturning(value = "pointcutService()&& @annotation(guavaEvent)", returning = "result")
    public void handlerEventAfter(JoinPoint joinPoint, MicroEvent guavaEvent, Object result) {
        if (guavaEvent.enable()) {
            if (MicroEvent.EventPost.AFTER.equals(guavaEvent.advice())) {
                MethodSignature ms = (MethodSignature) joinPoint.getSignature();
                Method method = ms.getMethod();
                asyncEventFactory.publishEvent(guavaEvent, method.getName(), method.getName(), joinPoint.getArgs(), result);
            }
        }
    }

}
