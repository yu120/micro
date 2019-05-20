package cn.micro.biz.pubsrv.pay.wx;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WXPayDomain implements IWXPayDomain {

    @Override
    public void report(String domain, long elapsedTimeMillis, Exception ex) {
        log.error("WX pay report:" + domain + "," + elapsedTimeMillis, ex);
    }

    @Override
    public DomainInfo getDomain(WXPayConfig config) {
        return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
    }

}
