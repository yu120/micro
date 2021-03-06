package cn.micro.biz.commons.mybatis;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mybatis Plus Config
 *
 * @author lry
 */
@Slf4j
@Configuration
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
public class MybatisPlusConfiguration implements EnvironmentAware {

    private static final String MAPPER_LOCATIONS = "mybatis-plus.mapper-locations";
    private static final ResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

    private String mapperPackage;

    /**
     * Read environment config
     *
     * @param env {@link Environment}
     */
    @Override
    public void setEnvironment(Environment env) {
        if (env == null) {
            return;
        }

        Set<String> mapperPackages = new HashSet<>();
        String mapperLocation = env.getProperty(MAPPER_LOCATIONS);
        if (mapperLocation == null || mapperLocation.length() == 0) {
            throw new MicroErrorException("Not found " + MAPPER_LOCATIONS);
        }
        String[] mapperLocationArray = mapperLocation.split(",");

        try {
            for (String mla : mapperLocationArray) {
                for (org.springframework.core.io.Resource location : RESOURCE_RESOLVER.getResources(mla)) {
                    if (location == null) {
                        continue;
                    }

                    org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
                    try (InputStream inputStream = location.getInputStream()) {
                        XPathParser parser = new XPathParser(inputStream, true,
                                configuration.getVariables(), new XMLMapperEntityResolver());
                        if (configuration.isResourceLoaded(mla)) {
                            continue;
                        }
                        XNode xNode = parser.evalNode("/mapper");
                        if (xNode == null) {
                            continue;
                        }

                        String mapperClass = xNode.getStringAttribute("namespace");
                        mapperPackages.add(mapperClass.substring(0, mapperClass.lastIndexOf(".")) + "*");
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new MicroErrorException(e.getMessage(), e);
        }

        if (mapperPackages.isEmpty()) {
            throw new MicroErrorException("Not found mapper package");
        }
        this.mapperPackage = String.join(",", mapperPackages);
        log.info("Scan to mapper packages: {}", mapperPackage);
    }

    /**
     * Micro mybatis properties
     *
     * @return {@link MicroMybatisProperties}
     */
    @Bean
    @ConfigurationProperties(prefix = "micro.mybatis")
    public MicroMybatisProperties microMybatisProperties() {
        return new MicroMybatisProperties();
    }

    /**
     * Micro transaction properties
     *
     * @return {@link MicroTransactionProperties}
     */
    @Bean
    @ConfigurationProperties(prefix = "micro.transaction")
    public MicroTransactionProperties microTransactionProperties() {
        return new MicroTransactionProperties();
    }

    /**
     * Druid data source druid data source.
     *
     * @return the druid data source
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * Global transaction configuration
     *
     * @param druidDataSource            {@link DataSource}
     * @param microTransactionProperties {@link MicroTransactionProperties}
     * @return {@link PlatformTransactionManager}
     */
    @Bean
    @ConditionalOnProperty(prefix = "micro.transaction", name = "enable", havingValue = "true")
    public PlatformTransactionManager platformTransactionManager(
            DataSource druidDataSource,
            MicroTransactionProperties microTransactionProperties) {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(druidDataSource);
        manager.setDefaultTimeout((int) microTransactionProperties.getDefaultTimeout().getSeconds());
        return manager;
    }

    /**
     * Mybatis Mapper Scanner Configurer
     *
     * @param mybatisPlusProperties {@link MybatisPlusProperties}
     * @return {@link MapperScannerConfigurer}
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(MybatisPlusProperties mybatisPlusProperties) {
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisPlusProperties.setConfiguration(mybatisConfiguration);

        MapperScannerConfigurer scannerConfigurer = new MapperScannerConfigurer();
        scannerConfigurer.setBasePackage(mapperPackage);
        return scannerConfigurer;
    }

    /**
     * Micro Logic delete injector
     *
     * @return {@link DefaultSqlInjector}
     */
    @Bean
    public DefaultSqlInjector defaultSqlInjector() {
        return new DefaultSqlInjector();
    }


    // ============ Custom Mybatis Interceptor ==============


    /**
     * Pagination interceptor
     *
     * @param microMybatisProperties {@link MicroMybatisProperties}
     * @return {@link PaginationInterceptor}
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(MicroMybatisProperties microMybatisProperties) {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        if (microMybatisProperties.isBlockAttack()) {
            // 攻击 SQL 阻断解析器、加入解析链
            sqlParserList.add(new BlockAttackSqlParser());
        }
        if (sqlParserList.size() > 0) {
            paginationInterceptor.setSqlParserList(sqlParserList);
        }

        return paginationInterceptor;
    }

}