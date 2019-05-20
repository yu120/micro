package cn.micro.biz.pubsrv.event;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * 基于注解的AOP事件
 *
 * @author lry
 */
@Slf4j
@Aspect
@Configuration
@ConditionalOnProperty(prefix = MicroEventProperties.PREFIX, name = "enable", havingValue = "true")
public class MicroEventAop {

    @Pointcut("@annotation(cn.micro.biz.pubsrv.event.MicroEvent)")
    public void pointcutService() {
    }

    @Around("pointcutService()")
    public Object pjpService(ProceedingJoinPoint pjp) throws Throwable {
        try {
            Signature signature = pjp.getSignature();
            if (signature == null) {
                return pjp.proceed();
            }
            if (!(signature instanceof MethodSignature)) {
                return pjp.proceed();
            }
            Method method = ((MethodSignature) signature).getMethod();
            if (method == null) {
                return pjp.proceed();
            }

            MicroEvent microEvent = method.getAnnotation(MicroEvent.class);
            MicroEventService.collect(microEvent.value());
        } catch (Throwable t) {
            return pjp.proceed();
        }

        return pjp.proceed();
    }

}
