package cn.micro.biz.pubsrv.mq.activemq;

import cn.micro.biz.pubsrv.mq.MicroMQProperties;
import cn.micro.biz.pubsrv.mq.MicroMQService;

import javax.jms.*;

public class ActiveMQJMSProducerTest {

    public static void main(String[] args) throws Exception {
        MicroMQProperties properties = new MicroMQProperties();
        properties.setUri("amqp://192.168.2.56:5672");
        properties.setUsername("sxw_demo");
        properties.setPassword("sxw_demo");
        MicroMQService microMQService = new MicroMQService(properties);
        microMQService.afterPropertiesSet();

        try {
            Session session = microMQService.createSession();
            Destination destination = session.createTopic("TopicTest:TagA");
            MessageProducer messageProducer = session.createProducer(destination);
            TextMessage message = session.createTextMessage("中文，Hello world!");
            messageProducer.send(message);
            System.out.println("send message success! msgId:" + message.getJMSMessageID());
        } finally {
            microMQService.destroy();
        }
    }

}
