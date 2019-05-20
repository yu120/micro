package cn.micro.biz.commons.mybatis.extension;

import cn.micro.biz.commons.trace.TraceStackContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.springframework.web.context.request.RequestContextHolder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Statement;
import java.util.Properties;

/**
 * Trace and Expend by IBatis Interceptor
 *
 * @author lry
 */
@Slf4j
@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
public class TraceExpendInterceptor implements Interceptor {

    private Method oracleGetOriginalSqlMethod;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        String originalSql = getOriginalSql(invocation);
        Object ret = null;
        String stack = null;
        Integer exception = null;
        long startTime = System.currentTimeMillis();

        try {
            TraceStackContext.enter(originalSql, null);
            startTime = System.currentTimeMillis();
            return ret = invocation.proceed();
        } catch (Throwable t) {
            exception = 0;
            if (t instanceof InvocationTargetException) {
                InvocationTargetException e = (InvocationTargetException) t;
                if (e.getTargetException() != null) {
                    stack = e.getTargetException().getMessage();
                    throw t;
                }
            }

            stack = t.getClass().getSimpleName();
            throw t;
        } finally {
            if (RequestContextHolder.getRequestAttributes() != null) {
                log.debug("Total expend: {}ms", System.currentTimeMillis() - startTime);
            }
            TraceStackContext.exit(ret, exception, stack);
        }
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private String getOriginalSql(Invocation invocation) {
        Statement statement;
        Object firstArg = invocation.getArgs()[0];
        if (Proxy.isProxyClass(firstArg.getClass())) {
            statement = (Statement) SystemMetaObject.forObject(firstArg).getValue("h.statement");
        } else {
            statement = (Statement) firstArg;
        }
        try {
            statement.getClass().getDeclaredField("stmt");
            statement = (Statement) SystemMetaObject.forObject(statement).getValue("stmt.statement");
        } catch (Exception e) {
            // do nothing
        }

        String originalSql = null;
        String stmtClassName = statement.getClass().getName();
        if ("oracle.jdbc.driver.T4CPreparedStatement".equals(stmtClassName)) {
            try {
                if (oracleGetOriginalSqlMethod != null) {
                    Object stmtSql = oracleGetOriginalSqlMethod.invoke(statement);
                    if (stmtSql != null) {
                        if (stmtSql instanceof String) {
                            originalSql = (String) stmtSql;
                        }
                    }
                } else {
                    Class<?> clazz = Class.forName("oracle.jdbc.driver.OracleStatement");
                    oracleGetOriginalSqlMethod = clazz.getDeclaredMethod("getOriginalSql", (Class<?>) null);
                    if (oracleGetOriginalSqlMethod != null) {
                        Object stmtSql = oracleGetOriginalSqlMethod.invoke(statement);
                        if (stmtSql != null) {
                            if (stmtSql instanceof String) {
                                originalSql = (String) stmtSql;
                            }
                        }
                    }
                }
            } catch (Exception e) {//ignore
            }
        }
        if (originalSql == null) {
            originalSql = statement.toString();
        }

        int index = originalSql.indexOf(':');
        if (index > 0) {
            originalSql = originalSql.substring(index + 1);
        }

        while (originalSql.startsWith(" ")) {
            originalSql = originalSql.substring(1);
        }
        while (originalSql.endsWith(" ")) {
            originalSql = originalSql.substring(0, originalSql.length() - 1);
        }

        return originalSql.replaceAll("[\\s]+", " ");
    }

}
