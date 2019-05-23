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

    ALL_ROLE("角色列表"),
    ALL_PERMISSION("权限列表"),
    ALL_ROLE_PERMISSION("角色权限列表"),
    ALL_MEMBER_GROUP("用户组列表"),
    ALL_MEMBER_GROUP_ROLE("用户组角色列表"),
    ;

    private final String msg;

}
