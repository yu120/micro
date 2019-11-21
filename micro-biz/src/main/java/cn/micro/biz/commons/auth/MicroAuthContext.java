package cn.micro.biz.commons.auth;

import cn.micro.biz.commons.exception.support.*;
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

    private final MicroAuthProperties microAuthProperties;

    @Override
    public void afterPropertiesSet() {
        MicroAuthContext.properties = microAuthProperties;

        try {
            ALGORITHM = Algorithm.HMAC256(SECRET);
            VERIFIER = JWT.require(ALGORITHM).build();
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static MicroToken build(Long membersId, String membersName, Integer platform,
                                   List<String> authorities, Map<String, Object> others) {
        try {
            String jti = UUID.randomUUID().toString();

            JWTCreator.Builder builder = build(others);
            builder.withJWTId(jti);
            builder.withIssuer(ISSUER);
            builder.withArrayClaim(SCOPE, SCOPE_TYPES);
            builder.withExpiresAt(new Date(System.currentTimeMillis() + properties.getTokenExpires().toMillis()));

            builder.withClaim(MicroTokenBody.MEMBERS_ID, membersId);
            builder.withClaim(MicroTokenBody.MEMBERS_NAME, membersName);
            builder.withClaim(MicroTokenBody.PLATFORM, platform);
            builder.withClaim(MicroTokenBody.IP, MicroAuthContext.getRequestIPAddress());
            builder.withClaim(MicroTokenBody.TIME, System.currentTimeMillis());
            if (!(authorities == null || authorities.size() == 0)) {
                builder.withArrayClaim(MicroTokenBody.AUTHORITIES, authorities.toArray(new String[0]));
            }

            String accessTokenStr = builder.sign(ALGORITHM);
            builder.withClaim(ATI_KEY, UUID.randomUUID().toString());
            builder.withExpiresAt(new Date(System.currentTimeMillis() + properties.getRefreshToken().toMillis()));
            String refreshTokenStr = builder.sign(ALGORITHM);

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

    private static String buildAccessTokenKey(Long membersId) {
        return CACHE_ACCESS_TOKEN_KEY + membersId;
    }

    private static String buildRefreshTokenKey(Long membersId) {
        return CACHE_REFRESH_TOKEN_KEY + membersId;
    }

    private static void verifyTokenExpiredSignature(String token) {
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
            microTokenBody.setMembersId(decodedJWT.getClaim(MicroTokenBody.MEMBERS_ID).asLong());
            microTokenBody.setMembersName(decodedJWT.getClaim(MicroTokenBody.MEMBERS_NAME).asString());
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

    public static Long getMembersId() {
        return getContextAccessToken().getMembersId();
    }

    public static Long getNonMembersId() {
        try {
            return getMembersId();
        } catch (Exception e) {
            return null;
        }
    }

    public static List<String> getAuthorities() {
        return getContextAccessToken().getAuthorities();
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

    /**
     * The get request IP address
     *
     * @return ip
     */
    public static String getRequestIPAddress() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            return "127.0.0.1";
        }

        return ip;
    }

}
