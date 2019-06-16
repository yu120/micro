package cn.micro.biz.pubsrv.mq;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.micro.neural.URL;
import org.micro.neural.extension.ExtensionLoader;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.jms.Connection;
import javax.jms.Session;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(MicroMQProperties.class)
@ConditionalOnProperty(prefix = "micro.mq", name = "enable", havingValue = "true")
public class MicroMQService implements InitializingBean, DisposableBean {

    private final MicroMQProperties properties;

    private Connection connection;

    @Override
    public void afterPropertiesSet() throws Exception {
        URL uri = URL.valueOf(properties.getUri());
        IMicroMQConnection microMQConnection = ExtensionLoader.getLoader(
                IMicroMQConnection.class).getExtension(uri.getProtocol());
        this.connection = microMQConnection.createConnection(properties);
    }

    /**
     * 创建会话
     *
     * @return {@link Session}
     */
    public Session createSession() {
        try {
            return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (Exception e) {
            throw new MicroErrorException(e.getMessage(), e);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (connection != null) {
            connection.stop();
            connection.close();
        }
    }

}
