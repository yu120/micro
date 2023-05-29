package cn.micro.biz.pubsrv.mq.rabbitmq;

import com.rabbitmq.jms.admin.RMQConnectionFactory;

import javax.jms.*;

public class RibbitMQQueueProducerTest {

    public static void main(String[] args) throws JMSException {
        //1.创建连接工厂
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setUri("amqp://192.168.2.56:5672");
        connectionFactory.setUsername("xxx_demo");
        connectionFactory.setPassword("xxx_demo");
        //2.获取连接
        Connection connection = connectionFactory.createConnection();
        //3.启动连接
        connection.start();
        //4.获取session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //5.创建队列对象
        Queue queue = session.createQueue("test-queue");
        //6.创建消息生产者
        MessageProducer producer = session.createProducer(queue);
        //7.创建消息
        TextMessage textMessage = session.createTextMessage("欢迎来到activeMQ----");
        //8.发送消息
        producer.send(textMessage);
        //9.关闭资源
        producer.close();
        session.close();
        connection.close();
    }

}
