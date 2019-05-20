package cn.micro.biz.pubsrv.cache.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alicp.jetcache.support.AbstractValueEncoder;

/**
 * Fastjson Value Encoder
 *
 * @author lry
 */
public class FastjsonValueEncoder extends AbstractValueEncoder {

    public static final FastjsonValueEncoder INSTANCE = new FastjsonValueEncoder(true);

    public FastjsonValueEncoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    @Override
    public byte[] apply(Object o) {
        return JSON.toJSONString(o, SerializerFeature.WriteClassName).getBytes();
    }

}
