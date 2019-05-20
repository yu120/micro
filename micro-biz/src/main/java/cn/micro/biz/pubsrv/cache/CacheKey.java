package cn.micro.biz.pubsrv.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Cache Key
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum CacheKey {

    // ====

    MEMBER_INFO("获取用户信息");

    private final String msg;

}
