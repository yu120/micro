package cn.micro.biz.pubsrv.oss;

import cn.micro.biz.commons.trace.Trace;
import cn.micro.biz.model.vo.OssTokenVO;
import com.qiniu.common.Zone;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Qi Niu Oss Service
 *
 * @author lry
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(QiNiuOssProperties.class)
@ConditionalOnProperty(prefix = "micro.oss.qi-niu", name = "enable", havingValue = "true")
public class QiNiuOssService implements InitializingBean {

    private final QiNiuOssProperties properties;

    private Auth qiNuiAuth;
    private UploadManager qiNiuUploadManager;

    @Override
    public void afterPropertiesSet() throws Exception {
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration();
        cfg.zone = Zone.zone0();
        cfg.retryMax = properties.getRetryMax();
        cfg.putThreshold = properties.getPutThreshold();
        cfg.readTimeout = properties.getReadTimeoutSec();
        cfg.writeTimeout = properties.getWriteTimeoutSec();
        cfg.connectTimeout = properties.getConnectTimeoutSec();
        this.qiNiuUploadManager = new UploadManager(cfg);
        this.qiNuiAuth = Auth.create(properties.getAccessKey(), properties.getSecretKey());
    }

    @Trace
    public String getUploadToken() {
        return qiNuiAuth.uploadToken(properties.getBucket(),
                null, properties.getTokenExpiresSec(), null, true);
    }

    public OssTokenVO uploadToken() {
        return new OssTokenVO(this.getUploadToken(), properties.getAccessUrl());
    }

}
