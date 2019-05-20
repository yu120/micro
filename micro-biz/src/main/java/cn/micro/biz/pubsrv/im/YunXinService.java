package cn.micro.biz.pubsrv.im;

import cn.micro.biz.pubsrv.im.support.AbstractYunXinTeamClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * 网易云信服务
 *
 * @author lry
 */
@Service
@EnableConfigurationProperties(YunXinProperties.class)
public class YunXinService extends AbstractYunXinTeamClient {

}
