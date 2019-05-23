package cn.micro.biz.pubsrv.mq;

import cn.micro.biz.commons.extension.SPI;

import javax.jms.Connection;

/**
 * MQ Connection Interface
 *
 * @author lry
 */
@SPI("rocketmq")
public interface IMicroMQConnection {

    /**
     * 创建连接
     *
     * @param properties {@link MicroMQProperties}
     * @return {@link Connection}
     * @throws Exception create connection exception
     */
    Connection createConnection(MicroMQProperties properties) throws Exception;

}
