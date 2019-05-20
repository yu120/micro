package cn.micro.biz.pubsrv.cache.serializer;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Cache Serializer
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum CacheSerializer {

    // ====

    JAVA,
    KRYO,
    FASTJSON;

}
