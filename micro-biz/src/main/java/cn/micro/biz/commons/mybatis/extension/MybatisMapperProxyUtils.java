package cn.micro.biz.commons.mybatis.extension;

import com.baomidou.mybatisplus.core.override.MybatisMapperProxy;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class MybatisMapperProxyUtils {

    private static final Map<Object, String> TARGET_MAP = new HashMap<>();

    public static String getSignature(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        Object[] argsPre = invocation.getArguments();

        try {
            if (invocation instanceof ReflectiveMethodInvocation) {
                ReflectiveMethodInvocation rmi = (ReflectiveMethodInvocation) invocation;
                Object target = rmi.getThis();
                String targetSignature = TARGET_MAP.get(target);
                if (targetSignature != null) {
                    return targetSignature;
                }

                if (target instanceof Proxy) {
                    Proxy proxy = (Proxy) target;
                    InvocationHandler invocationHandler = Proxy.getInvocationHandler(proxy);
                    if (invocationHandler instanceof MybatisMapperProxy) {
                        MybatisMapperProxy mybatisMapperProxy = (MybatisMapperProxy) invocationHandler;
                        Field field = mybatisMapperProxy.getClass().getDeclaredField("mapperInterface");
                        field.setAccessible(true);
                        Object obj = field.get(mybatisMapperProxy);
                        if (obj != null) {
                            String className = ((Class) obj).getName();
                            targetSignature = buildSignature(className, method, argsPre);
                            TARGET_MAP.put(target, targetSignature);
                            return targetSignature;
                        }
                    }
                } else {
                    String className = method.getDeclaringClass().getName();
                    targetSignature = buildSignature(className, method, argsPre);
                    TARGET_MAP.put(target, targetSignature);
                    return targetSignature;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return invocation.getMethod().toString();
    }

    private static String buildSignature(String className, Method method, Object[] argsPre) {
        StringBuilder sb = new StringBuilder();
        if (!(argsPre == null || argsPre.length == 0)) {
            for (int i = 0; i < argsPre.length; i++) {
                sb.append(argsPre[i].getClass().getName());
                if (i < argsPre.length - 1) {
                    sb.append(",");
                }
            }
        }

        return method.getReturnType().getName() + " " + className
                + "." + method.getName() + "(" + sb.toString() + ")";
    }

}
