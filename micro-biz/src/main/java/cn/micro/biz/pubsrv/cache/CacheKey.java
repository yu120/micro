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

    MEMBER_GROUP("用户组列表"),
    MEMBER_GROUP_ROLE("用户组角色列表"),
    PERMISSION("权限列表"),
    ROLE_PERMISSION("角色权限列表"),
    ROLE("角色列表"),
    ;

    private final String msg;

}
