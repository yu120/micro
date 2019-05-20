package cn.micro.biz.pubsrv.mq;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MQ Type
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public enum MQProtocol {

    // ====

    /**
     * ActiveMQ
     */
    ACTIVEMQ("amqp"),

    /**
     * RabbitMQ
     */
    RABBITMQ("rabbitmq"),

    /**
     * RocketMQ
     */
    ROCKET_MQ("rocketmq");

    private final String protocol;

}
