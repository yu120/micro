package cn.micro.biz.commons.auth;

import cn.micro.biz.commons.exception.support.*;
import cn.micro.biz.commons.mybatis.MicroTenantProperties;
import cn.micro.biz.commons.utils.IPUtils;
import cn.micro.biz.pubsrv.redis.RedisService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Auth Context
 *
 * @author lry
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(MicroAuthProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MicroAuthContext implements InitializingBean {

    private static final String ATI_KEY = "ati";
    private static final String SCOPE_TYPE = "all";
    private static final String BEARER_TYPE = "Bearer";
    private static final String[] SCOPE_TYPES = {SCOPE_TYPE};
    private static final String SCOPE = "scope";
    private static final String ACCESS_TOKEN_KEY = "MICRO_TOKEN_BODY";
    private static final String REFRESH_TOKEN_HEADER_KEY = "X-Refresh-Token";

    public static final String ACCESS_TOKEN_HEADER_KEY = "X-Access-Token";
    private static final String CACHE_ACCESS_TOKEN_KEY = "ACCESS_TOKEN:";
    private static final String CACHE_REFRESH_TOKEN_KEY = "REFRESH_TOKEN:";

    private static final String ISSUER = "lry";
    private static final String SECRET = "micro-lry";

    private static Algorithm ALGORITHM;
    private static JWTVerifier VERIFIER;
    private static MicroAuthProperties properties;
    private static Long defaultTenantValue;

    private final MicroAuthProperties microAuthProperties;
    private final MicroTenantProperties microTenantProperties;

    @Override
    public void afterPropertiesSet() {
        MicroAuthContext.properties = microAuthProperties;
        MicroAuthContext.defaultTenantValue = microTenantProperties.getDefaultValue();

        try {
            ALGORITHM = Algorithm.HMAC256(SECRET);
            VERIFIER = JWT.require(ALGORITHM).build();
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static MicroToken build(MicroTokenBody microTokenBody) {
        return build(microTokenBody.getTenantId(), microTokenBody.getMemberId(), microTokenBody.getMemberName(),
                microTokenBody.getDeviceType(), microTokenBody.getAuthorities(), microTokenBody.getOthers());
    }

    public static MicroToken build(Long tenantId, Long memberId, String memberName, Integer platform, List<String> authorities) {
        return build(tenantId, memberId, memberName, platform, authorities, null);
    }

    public static MicroToken build(Long tenantId, Long memberId, String memberName, Integer platform,
                                   List<String> authorities, Map<String, Object> others) {
        try {
            String jti = UUID.randomUUID().toString();

            JWTCreator.Builder builder = build(others);
            builder.withJWTId(jti);
            builder.withIssuer(ISSUER);
            builder.withArrayClaim(SCOPE, SCOPE_TYPES);
            builder.withExpiresAt(new Date(System.currentTimeMillis() + properties.getTokenExpires().toMillis()));

            builder.withClaim(MicroTokenBody.TENANT_ID, tenantId);
            builder.withClaim(MicroTokenBody.MEMBER_ID, memberId);
            builder.withClaim(MicroTokenBody.MEMBER_NAME, memberName);
            builder.withClaim(MicroTokenBody.PLATFORM, platform);
            builder.withClaim(MicroTokenBody.IP, IPUtils.getRequestIPAddress());
            builder.withClaim(MicroTokenBody.TIME, System.currentTimeMillis());
            if (!(authorities == null || authorities.size() == 0)) {
                builder.withArrayClaim(MicroTokenBody.AUTHORITIES, authorities.toArray(new String[0]));
            }

            String accessTokenStr = builder.sign(ALGORITHM);
            builder.withClaim(ATI_KEY, UUID.randomUUID().toString());
            builder.withExpiresAt(new Date(System.currentTimeMillis() + properties.getRefreshToken().toMillis()));
            String refreshTokenStr = builder.sign(ALGORITHM);

            if (properties.isTokenExpire()) {
                try {
                    RedisService.commandSetSec(MicroAuthContext.buildAccessTokenKey(memberId), accessTokenStr, properties.getTokenExpires().getSeconds());
                    RedisService.commandSetSec(MicroAuthContext.buildRefreshTokenKey(memberId), refreshTokenStr, properties.getRefreshToken().getSeconds());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }

            return new MicroToken(BEARER_TYPE, accessTokenStr, properties.getTokenExpires().getSeconds(),
                    refreshTokenStr, properties.getRefreshToken().getSeconds(), SCOPE_TYPE, jti);
        } catch (JWTCreationException e) {
            throw new MicroErrorException(e.getMessage(), e);
        }
    }

    /**
     * The check client time,token,request sign,request expire
     *
     * @param timestamp client local timestamp
     * @param token     token
     * @param sign      client request sign
     * @param path      url path
     */
    public static void verify(String timestamp, String token, String sign, String path) throws Exception {
        if (properties.isCheckTime()) {
            verifyClientRequestTime(timestamp);
        }
        if (properties.isCheckToken()) {
            verifyTokenExpiredSignature(token);
        }
        if (properties.isCheckSign()) {
            verifyClientRequestSign(timestamp, token, sign);
        }
        if (properties.isRequestExpire()) {
            verifyServerRequestExpire(path, sign);
        }
        if (properties.isTokenExpire()) {
            verifyServerTokenExpire(MicroAuthContext.getContextAccessToken().getMemberId(), token);
        }
    }

    public static String getAndSetAccessToken(HttpServletRequest request) {
        // Authorization in Header is automatically parsed and filled.
        String accessTokenValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!(accessTokenValue == null || accessTokenValue.length() == 0)) {
            if (accessTokenValue.indexOf(" ") > 0) {
                accessTokenValue = accessTokenValue.substring(accessTokenValue.indexOf(" ") + 1);
            } else {
                throw new MicroBadRequestException("Illegal authorization parameter");
            }
        }

        // X-Access-Token in Header is automatically parsed and filled.
        if (accessTokenValue == null || accessTokenValue.length() == 0) {
            // X-Access-Token in Header is automatically parsed and filled.
            accessTokenValue = request.getHeader(ACCESS_TOKEN_HEADER_KEY);
        }

        // X-Access-Token in Query is automatically parsed and filled.
        if (accessTokenValue == null || accessTokenValue.length() == 0) {
            accessTokenValue = request.getParameter(ACCESS_TOKEN_HEADER_KEY);
        }

        // Parse and set to Context
        if (accessTokenValue != null && accessTokenValue.length() > 0) {
            request.setAttribute(ACCESS_TOKEN_KEY, parseToken(accessTokenValue));
        }

        return accessTokenValue;
    }

    public static String getRefreshAccessToken(HttpServletRequest request) {
        // X-Refresh-Token in Header is automatically parsed and filled.
        String refreshAccessTokenValue = request.getHeader(REFRESH_TOKEN_HEADER_KEY);

        // X-Refresh-Token in Query is automatically parsed and filled.
        if (refreshAccessTokenValue == null || refreshAccessTokenValue.length() == 0) {
            refreshAccessTokenValue = request.getParameter(REFRESH_TOKEN_HEADER_KEY);
        }

        return refreshAccessTokenValue;
    }

    public static String buildAccessTokenKey(Long memberId) {
        return CACHE_ACCESS_TOKEN_KEY + memberId;
    }

    public static String buildRefreshTokenKey(Long memberId) {
        return CACHE_REFRESH_TOKEN_KEY + memberId;
    }

    public static void verifyTokenExpiredSignature(String token) {
        if (token == null || token.length() == 0) {
            throw new MicroTokenNotFoundException();
        }

        try {
            VERIFIER.verify(token);
        } catch (TokenExpiredException e) {
            throw new MicroTokenExpiredException("Signature token has expired");
        } catch (Exception e) {
            throw new MicroBadRequestException("Signature verification exception");
        }
    }

    /**
     * Check client local timestamp
     *
     * @param timestamp timestamp str
     */
    private static void verifyClientRequestTime(String timestamp) {
        if (timestamp == null) {
            throw new MicroBadRequestException("Illegal Request");
        }

        long clientTimestamp;
        try {
            clientTimestamp = Long.valueOf(timestamp);
        } catch (Exception e) {
            throw new MicroBadRequestException("Illegal Request");
        }

        long currentTimeMillis = System.currentTimeMillis();
        long timeSize = currentTimeMillis - clientTimestamp;
        if (currentTimeMillis > clientTimestamp) {
            if (timeSize > properties.getFaultTolerant().toMillis()) {
                throw new MicroBadRequestException("Illegal Request");
            }
        } else {
            if (-timeSize > properties.getFaultTolerant().toMillis()) {
                throw new MicroBadRequestException("Illegal Request");
            }
        }
    }

    /**
     * Check request sign
     *
     * @param timestamp timestamp
     * @param token     token
     * @param sign      sign
     */
    private static void verifyClientRequestSign(String timestamp, String token, String sign) {
        if (!DigestUtils.md5Hex(timestamp + token).equals(sign)) {
            throw new MicroBadRequestException("Illegal Sign");
        }
    }

    /**
     * 基于Redis实现重复请求校验
     *
     * @param path request path
     * @param sign sign
     * @throws Exception throw exception
     */
    private static void verifyServerRequestExpire(String path, String sign) throws Exception {
        String key = DigestUtils.md5Hex(sign + path);
        String tempSign = RedisService.commandGet(key);
        if (tempSign == null || tempSign.length() == 0) {
            RedisService.commandSet(key, sign, properties.getFaultTolerant().getSeconds());
        } else {
            throw new MicroBadRequestException("Repeated requests");
        }
    }

    /**
     * 基于Redis实现Token过期过滤
     *
     * @param memberId member id
     * @param token    token
     * @throws Exception throw exception
     */
    private static void verifyServerTokenExpire(Long memberId, String token) throws Exception {
        String tokenStr = RedisService.commandGet(MicroAuthContext.buildAccessTokenKey(memberId));
        if (tokenStr == null || tokenStr.length() == 0) {
            throw new MicroTokenExpiredException("Server token has expired");
        }
        if (!tokenStr.equals(token)) {
            throw new MicroBadRequestException("Illegal token");
        }
    }

    private static JWTCreator.Builder build(Map<String, Object> others) {
        JWTCreator.Builder builder = JWT.create();
        if (!(others == null || others.isEmpty())) {
            for (Map.Entry<String, Object> entry : others.entrySet()) {
                withObjectClaim(builder, entry.getKey(), entry.getValue());
            }
        }

        return builder;
    }

    public static MicroTokenBody parseToken(String token) {
        if (token == null || token.length() == 0) {
            throw new MicroBadRequestException("Token can not be empty");
        }

        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            MicroTokenBody microTokenBody = new MicroTokenBody();
            microTokenBody.setMemberId(decodedJWT.getClaim(MicroTokenBody.MEMBER_ID).asLong());
            microTokenBody.setMemberName(decodedJWT.getClaim(MicroTokenBody.MEMBER_NAME).asString());
            microTokenBody.setDeviceType(decodedJWT.getClaim(MicroTokenBody.PLATFORM).asInt());
            microTokenBody.setAuthorities(decodedJWT.getClaim(MicroTokenBody.AUTHORITIES).asList(String.class));
            microTokenBody.setOthers(decodedJWT.getClaim(MicroTokenBody.OTHERS).asMap());
            return microTokenBody;
        } catch (Exception e) {
            throw new MicroBadRequestException("Decode is exception");
        }
    }

    public static MicroTokenBody getContextAccessToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new MicroErrorException("Not Found RequestAttributes");
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return (MicroTokenBody) request.getAttribute(ACCESS_TOKEN_KEY);
    }

    public static MicroTokenBody getContextRefreshToken() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new MicroErrorException("Not Found RequestAttributes");
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String refreshTokenValue = MicroAuthContext.getRefreshAccessToken(request);
        if (refreshTokenValue == null || refreshTokenValue.length() == 0) {
            throw new MicroPermissionException("Not Found X-Refresh-Token");
        }

        // verify token
        MicroAuthContext.verifyTokenExpiredSignature(refreshTokenValue);

        // parse token
        return MicroAuthContext.parseToken(refreshTokenValue);
    }


    public static Long getMemberId() {
        MicroTokenBody microTokenBody = getContextAccessToken();
        return microTokenBody.getMemberId();
    }

    public static Long getNonMemberId() {
        try {
            return getMemberId();
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getTenantId() {
        MicroTokenBody microTokenBody;
        try {
            microTokenBody = getContextAccessToken();
            if (microTokenBody == null) {
                return defaultTenantValue;
            }
        } catch (Exception e) {
            return defaultTenantValue;
        }

        return microTokenBody.getTenantId();
    }

    public static List<String> getAuthorities() {
        MicroTokenBody microTokenBody = getContextAccessToken();
        return microTokenBody.getAuthorities();
    }

    private static void withObjectClaim(JWTCreator.Builder builder, String key, Object value) {
        if (value instanceof Integer) {
            builder.withClaim(key, (Integer) value);
        } else if (value instanceof Boolean) {
            builder.withClaim(key, (Boolean) value);
        } else if (value instanceof Date) {
            builder.withClaim(key, (Date) value);
        } else if (value instanceof Long) {
            builder.withClaim(key, (Long) value);
        } else if (value instanceof Double) {
            builder.withClaim(key, (Double) value);
        } else if (value instanceof String) {
            builder.withClaim(key, (String) value);
        } else if (value instanceof Long[]) {
            builder.withArrayClaim(key, (Long[]) value);
        } else if (value instanceof String[]) {
            builder.withArrayClaim(key, (String[]) value);
        } else if (value instanceof Integer[]) {
            builder.withArrayClaim(key, (Integer[]) value);
        } else {
            throw new IllegalArgumentException("Unsupported Data Types");
        }
    }

}
