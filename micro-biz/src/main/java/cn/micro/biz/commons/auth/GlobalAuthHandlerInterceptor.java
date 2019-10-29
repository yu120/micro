package cn.micro.biz.commons.auth;

import cn.micro.biz.commons.exception.support.MicroPermissionException;
import cn.micro.biz.entity.member.PermissionEntity;
import cn.micro.biz.mapper.member.IRolePermissionMapper;
import cn.micro.biz.model.view.RoleCodePermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Auth Handler Interceptor
 *
 * @author lry
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GlobalAuthHandlerInterceptor extends HandlerInterceptorAdapter implements InitializingBean, DisposableBean {

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    private final ConcurrentMap<String, List<RoleCodePermission>> rolePermissions = new ConcurrentHashMap<>();
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = null;
    private final Object LOCK = new Object();

    private final MicroAuthProperties properties;
    private final IRolePermissionMapper rolePermissionMapper;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!properties.isAutoAuthRefresh()) {
            this.refresh();
            return;
        }

        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("global-auth-task-executor");
            return t;
        });
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(this::refresh,
                0, properties.getAuthRefresh().getSeconds(), TimeUnit.SECONDS);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // === Check CORS
        if (RequestMethod.OPTIONS.name().equals(request.getMethod())) {
            return true;
        }

        // === Check HandlerMethod
        if (!(handler instanceof HandlerMethod)) {
            if (!(handler instanceof ResourceHttpRequestHandler)) {
                log.warn("Illegal Handler：{}", handler);
            }

            return true;
        }

        // === check handler if need auth
        boolean needVerifyToken;
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class<?> beanTypeClass = handlerMethod.getBeanType();
        if (handlerMethod.hasMethodAnnotation(PreAuth.class)) {
            needVerifyToken = true;
        } else if (handlerMethod.hasMethodAnnotation(NonAuth.class)) {
            needVerifyToken = false;
        } else if (beanTypeClass.isAnnotationPresent(PreAuth.class)) {
            needVerifyToken = true;
        } else if (beanTypeClass.isAnnotationPresent(NonAuth.class)) {
            needVerifyToken = false;
        } else {
            needVerifyToken = false;
        }
        if (!needVerifyToken) {
            return true;
        }

        String timestamp = request.getHeader(MicroAuthProperties.TIMESTAMP_KEY);
        String accessTokenValue = MicroAuthContext.getAndSetAccessToken(request);
        String sign = request.getHeader(MicroAuthProperties.SIGN_KEY);
        MicroAuthContext.verify(timestamp, accessTokenValue, sign, request.getContextPath());

        // === Check Role Permission
        MicroTokenBody microTokenBody = MicroAuthContext.getContextAccessToken();
        if (microTokenBody.getAuthorities() == null || microTokenBody.getAuthorities().isEmpty()) {
            throw new MicroPermissionException("Unassigned role");
        }
        String servletPath = request.getServletPath();
        for (String role : microTokenBody.getAuthorities()) {
            List<RoleCodePermission> permissions = rolePermissions.get(role);
            if (permissions == null || permissions.isEmpty()) {
                continue;
            }

            for (PermissionEntity permission : permissions) {
                if (ANT_PATH_MATCHER.match(permission.getCode(), servletPath)) {
                    return super.preHandle(request, response, handler);
                }
            }
        }

        throw new MicroPermissionException("Roles unauthorized");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public void destroy() throws Exception {
        if (scheduledThreadPoolExecutor != null) {
            scheduledThreadPoolExecutor.shutdown();
        }
    }

    private void refresh() {
        ConcurrentMap<String, List<RoleCodePermission>> tempRolePermissions = new ConcurrentHashMap<>();

        List<RoleCodePermission> roleCodePermissionList = rolePermissionMapper.selectRoleCodePermissions();
        // calculation role->permission collection
        for (RoleCodePermission crp : roleCodePermissionList) {
            List<RoleCodePermission> tempPermissionList =
                    tempRolePermissions.computeIfAbsent(crp.getRoleCode(), k -> new ArrayList<>());
            tempPermissionList.add(crp);
        }

        synchronized (LOCK) {
            rolePermissions.clear();
            rolePermissions.putAll(tempRolePermissions);
        }
    }

}