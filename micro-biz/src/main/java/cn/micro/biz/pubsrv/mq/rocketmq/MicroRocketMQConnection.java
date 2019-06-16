package cn.micro.biz.pubsrv.mq.rocketmq;

import cn.micro.biz.pubsrv.mq.IMicroMQConnection;
import cn.micro.biz.pubsrv.mq.MicroMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.jms.domain.CommonConstant;
import org.apache.rocketmq.jms.domain.JmsBaseConnectionFactory;
import org.micro.neural.extension.Extension;

import javax.jms.Connection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Micro RocketMQ Connection
 * <p>
 * {@see https://github.com/apache/rocketmq-externals}
 * mvn clean source:jar install -Dmaven.test.skip=true
 *
 * @author lry
 */
@Slf4j
@Extension("rocketmq")
public class MicroRocketMQConnection implements IMicroMQConnection {

    @Override
    public Connection createConnection(MicroMQProperties properties) throws Exception {
        Map<String, String> parameters = new HashMap<>();
        URI uri = new URI(properties.getUri());
        parameters.put(CommonConstant.NAMESERVER, uri.getHost() + ":" + uri.getPort());

        String connectionUri = appendParams(properties.getUri(), parameters);
        log.info("Rocket MQ connection uri:{}", connectionUri);
        return this.createConnection(connectionUri);
    }

    /**
     * Create connection
     *
     * @param connectionUri connection uri
     * @return {@link Connection}
     * @throws Exception throw exception
     */
    private Connection createConnection(String connectionUri) throws Exception {
        JmsBaseConnectionFactory connectionFactory = new JmsBaseConnectionFactory();
        connectionFactory.setConnectionUri(new URI(connectionUri));
        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

    /**
     * 向url链接追加参数
     *
     * @param url    url
     * @param params Map<String, String>
     * @return append url
     */
    private String appendParams(String url, Map<String, String> params) {
        if (url == null || url.length() == 0) {
            return "";
        } else if (params == null || params.isEmpty()) {
            return url.trim();
        } else {
            StringBuilder sb = new StringBuilder();
            Set<String> keys = params.keySet();
            for (String key : keys) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);

            url = url.trim();
            int length = url.length();
            int index = url.indexOf("?");
            if (index > -1) {
                if ((length - 1) == index) {
                    url += sb.toString();
                } else {
                    url += "&" + sb.toString();
                }
            } else {
                url += "?" + sb.toString();
            }

            return url;
        }
    }

}
