package cn.micro.biz.pubsrv.mq.activemq;

import cn.micro.biz.pubsrv.mq.IMicroMQConnection;
import cn.micro.biz.pubsrv.mq.MicroMQProperties;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang3.StringUtils;

import javax.jms.Connection;

/**
 * Micro ActiveMQ Connection
 *
 * @author lry
 */
public class MicroActiveMQConnection implements IMicroMQConnection {

    @Override
    public Connection createConnection(MicroMQProperties properties) throws Exception {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(properties.getUri());
        if (StringUtils.isNotBlank(properties.getUsername())) {
            connectionFactory.setUserName(properties.getUsername());
            connectionFactory.setPassword(properties.getPassword());
        }

        Connection connection = connectionFactory.createConnection();
        connection.start();
        return connection;
    }

}
