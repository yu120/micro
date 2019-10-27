package cn.micro.biz.pubsrv.cache.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alicp.jetcache.support.AbstractValueDecoder;

public class FastJsonValueDecoder extends AbstractValueDecoder {

    public static final FastJsonValueDecoder INSTANCE = new FastJsonValueDecoder(true);

    public FastJsonValueDecoder(boolean useIdentityNumber) {
        super(useIdentityNumber);
    }

    @Override
    protected Object doApply(byte[] buffer) throws Exception {
        return JSON.parse(new String(buffer), Feature.SupportAutoType);
    }

}
