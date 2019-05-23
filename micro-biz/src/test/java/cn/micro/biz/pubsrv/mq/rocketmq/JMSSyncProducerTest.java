package cn.micro.biz.pubsrv.mq.rocketmq;

import cn.micro.biz.pubsrv.mq.MicroMQProperties;

import javax.jms.*;

public class JMSSyncProducerTest {

    public static void main(String[] args) throws Exception {
        MicroMQProperties microMQProperties = new MicroMQProperties();
        microMQProperties.setUri("rocketmq://localhost:9876?producerId=producerId");
        MicroRocketMQConnection microMQConnection = new MicroRocketMQConnection();
        Connection connection = microMQConnection.createConnection(microMQProperties);

        try {
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createTopic("TopicTest:TagA");
            MessageProducer messageProducer = session.createProducer(destination);

            connection.start();
            TextMessage message = session.createTextMessage("中文，Hello world!");

            messageProducer.send(message);
            System.out.println("send message success! msgId:" + message.getJMSMessageID());
        } finally {
            connection.close();
        }
    }

}
