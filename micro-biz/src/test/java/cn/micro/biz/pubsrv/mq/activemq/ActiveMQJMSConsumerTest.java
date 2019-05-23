package cn.micro.biz.pubsrv.mq.activemq;

import cn.micro.biz.pubsrv.mq.MicroMQProperties;
import cn.micro.biz.pubsrv.mq.MicroMQService;

import javax.jms.*;

public class ActiveMQJMSConsumerTest {

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
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        System.out.println(message.getJMSMessageID());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            Thread.sleep(100000);
        } finally {
            microMQService.destroy();
        }
    }

}
