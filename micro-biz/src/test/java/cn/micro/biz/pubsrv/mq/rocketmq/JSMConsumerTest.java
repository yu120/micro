package cn.micro.biz.pubsrv.mq.rocketmq;

import cn.micro.biz.pubsrv.mq.MicroMQProperties;
import org.apache.rocketmq.jms.domain.message.JmsTextMessage;

import javax.jms.*;

public class JSMConsumerTest {

    public static void main(String[] args) throws Exception {
        MicroMQProperties microMQProperties = new MicroMQProperties();
        microMQProperties.setUri("rocketmq://localhost:9876?consumerId=consumerId");
        MicroRocketMQConnection microMQConnection = new MicroRocketMQConnection();
        Connection connection = microMQConnection.createConnection(microMQProperties);
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        try {
            Destination destination = session.createTopic("TopicTest:TagA");
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        JmsTextMessage jmsTextMessage = (JmsTextMessage) message;
                        System.out.println(jmsTextMessage.getJMSMessageID() + "-->" + jmsTextMessage.getText());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            connection.start();
            Thread.sleep(100000);
        } finally {
            connection.close();
        }
    }

}
