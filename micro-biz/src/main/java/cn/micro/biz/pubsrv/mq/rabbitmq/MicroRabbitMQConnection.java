package cn.micro.biz.pubsrv.mq.rabbitmq;

import cn.micro.biz.pubsrv.mq.IMicroMQConnection;
import cn.micro.biz.pubsrv.mq.MicroMQProperties;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;

import javax.jms.Connection;

/**
 * Micro RabbitMQ Connection
 *
 * @author lry
 */
public class MicroRabbitMQConnection implements IMicroMQConnection {

    @Override
    public Connection createConnection(MicroMQProperties properties) throws Exception {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setUri(properties.getUri());
        if (StringUtils.isNotBlank(properties.getUsername())) {
            connectionFactory.setUsername(properties.getUsername());
            connectionFactory.setPassword(properties.getPassword());
        }

        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

}
