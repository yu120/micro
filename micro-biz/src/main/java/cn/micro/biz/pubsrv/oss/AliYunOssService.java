package cn.micro.biz.pubsrv.oss;

import com.aliyun.oss.OSSClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

/**
 * Ali Yun Oss Service
 *
 * @author lry
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(AliYunOssProperties.class)
@ConditionalOnProperty(prefix = "micro.oss.ali-yun", name = "enable", havingValue = "false")
public class AliYunOssService implements InitializingBean, DisposableBean {

    private OSSClient ossClient;
    private final AliYunOssProperties properties;

    @Override
    public void afterPropertiesSet() {
        this.ossClient = new OSSClient(properties.getEndpoint(), properties.getAccessKeyId(), properties.getAccessKeySecret());
    }

    public String upload(String fileType, InputStream inputStream) {
        String key = UUID.randomUUID().toString().replace("-", "") + fileType;
        log.info("Key: {}", key);
        ossClient.putObject(properties.getBucketName(), key, inputStream);
        return properties.getAccessUrl() + key;
    }

    public String upload(String fileType, byte[] data) {
        String key = UUID.randomUUID().toString().replace("-", "") + "." + fileType;
        ossClient.putObject(properties.getBucketName(), key, new ByteArrayInputStream(data));
        return properties.getAccessUrl() + key;
    }

    @Override
    public void destroy() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
    }

}
