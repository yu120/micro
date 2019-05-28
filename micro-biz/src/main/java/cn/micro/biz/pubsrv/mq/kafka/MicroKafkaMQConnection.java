package cn.micro.biz.pubsrv.mq.kafka;

import cn.micro.biz.pubsrv.mq.IMicroMQConnection;
import cn.micro.biz.pubsrv.mq.MicroMQProperties;
import io.confluent.kafka.jms.KafkaConnectionFactory;
import org.micro.extension.Extension;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import java.util.Properties;

/**
 * Micro KafkaMQ Connection
 *
 * @author lry
 */
@Extension("kafka")
public class MicroKafkaMQConnection implements IMicroMQConnection {

    @Override
    public Connection createConnection(MicroMQProperties properties) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("schema.registry.url", "http://localhost:8081");
        props.put("zookeeper.connect", "localhost:2181");
        props.put("client.id", "MySampleJMSProducer");
        props.put("group.id", "jmsconsumergroup1");
        props.put("confluent.license", "RLKGWF3S8Q7LA5N4AEQ74VOWYD3VL297");

        props.put("producer.interceptor.classes", "io.confluent.monitoring.clients.interceptor.MonitoringProducerInterceptor");
        props.put("consumer.interceptor.classes", "io.confluent.monitoring.clients.interceptor.MonitoringConsumerInterceptor");

        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", "/var/private/ssl/kafka.client.truststore.jks");
        props.put("ssl.truststore.password", "test1234");

        props.put("ssl.keystore.location", "/var/private/ssl/kafka.client.keystore.jks");
        props.put("ssl.keystore.password", "test1234");
        props.put("ssl.key.password", "test1234");

        ConnectionFactory connectionFactory = new KafkaConnectionFactory(props);
        return connectionFactory.createConnection();
    }

}
