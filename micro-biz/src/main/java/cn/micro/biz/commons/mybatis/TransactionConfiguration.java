package cn.micro.biz.commons.mybatis;

import io.seata.spring.annotation.GlobalTransactionScanner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Transaction Configuration
 *
 * @author lry
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(TransactionProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(prefix = "micro.transaction", name = "enable", havingValue = "true")
public class TransactionConfiguration {

    private final TransactionProperties properties;

    /**
     * Global transaction scanner
     *
     * @return global transaction scanner
     */
    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
        return new GlobalTransactionScanner(properties.getApplicationId(), properties.getTxServiceGroup());
    }

}
