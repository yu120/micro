package cn.micro.biz.pubsrv.mq;

import cn.micro.biz.commons.URL;
import cn.micro.biz.commons.exception.MicroErrorException;
import cn.micro.biz.pubsrv.mq.activemq.MicroActiveMQConnection;
import cn.micro.biz.pubsrv.mq.rabbitmq.MicroRabbitMQConnection;
import cn.micro.biz.pubsrv.mq.rocketmq.MicroRocketMQConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.jms.Connection;
import javax.jms.Session;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(MicroMQProperties.class)
@ConditionalOnProperty(prefix = "micro.mq", name = "enable", havingValue = "true")
public class MicroMQService implements InitializingBean, DisposableBean {

    private final MicroMQProperties properties;

    private Connection connection;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.initialize();
    }

    public void initialize() throws Exception {
        URL uri = URL.valueOf(properties.getUri());
        if (MQProtocol.ACTIVEMQ.getProtocol().equals(uri.getProtocol())) {
            this.connection = new MicroActiveMQConnection().createConnection(properties);
        } else if (MQProtocol.RABBITMQ.getProtocol().equals(uri.getProtocol())) {
            this.connection = new MicroRabbitMQConnection().createConnection(properties);
        } else if (MQProtocol.ROCKET_MQ.getProtocol().equals(uri.getProtocol())) {
            this.connection = new MicroRocketMQConnection().createConnection(properties);
        }
    }

    /**
     * 创建会话
     *
     * @return {@link Session}
     */
    public Session createSession() {
        try {
            return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (Exception e) {
            throw new MicroErrorException(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (connection != null) {
            connection.stop();
            connection.close();
        }
    }

}
