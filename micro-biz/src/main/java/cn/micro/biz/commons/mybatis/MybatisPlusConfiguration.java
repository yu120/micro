package cn.micro.biz.commons.mybatis;

import cn.micro.biz.commons.exception.support.MicroErrorException;
import cn.micro.biz.commons.mybatis.extension.EnumTypeHandler;
import cn.micro.biz.commons.mybatis.extension.MicroTenantSqlParser;
import cn.micro.biz.commons.mybatis.extension.TraceExpendInterceptor;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.MappedStatement;
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
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Nullable;
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
    public void setEnvironment(@Nullable Environment env) {
        if (env == null) {
            return;
        }

        Set<String> mapperPackages = new HashSet<>();
        String mapperLocation = env.getProperty(MAPPER_LOCATIONS);
        if (mapperLocation == null || mapperLocation.length() == 0) {
            throw new MicroErrorException("Not found " + MAPPER_LOCATIONS);
        }

        try {
            for (Resource location : RESOURCE_RESOLVER.getResources(mapperLocation)) {
                if (location == null) {
                    continue;
                }

                org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
                try (InputStream inputStream = location.getInputStream()) {
                    XPathParser parser = new XPathParser(inputStream, true,
                            configuration.getVariables(), new XMLMapperEntityResolver());
                    if (configuration.isResourceLoaded(mapperLocation)) {
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
     * Micro tenant properties
     *
     * @return {@link MicroTenantProperties}
     */
    @Bean
    @ConfigurationProperties(prefix = "micro.tenant")
    public MicroTenantProperties microTenantProperties() {
        return new MicroTenantProperties();
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
     * Data source data source.
     *
     * @param druidDataSource the druid data source
     * @return the data source
     */
    @Primary
    @Bean("dataSource")
    @ConditionalOnProperty(prefix = "micro.transaction", name = "enable", havingValue = "true")
    public DataSource dataSource(DruidDataSource druidDataSource) {
        return new DataSourceProxy(druidDataSource);
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
     * Global transaction scanner
     *
     * @param microTransactionProperties {@link MicroTransactionProperties}
     * @return global transaction scanner
     */
    @Bean
    @ConditionalOnProperty(prefix = "micro.transaction", name = "seata", havingValue = "true")
    public GlobalTransactionScanner globalTransactionScanner(MicroTransactionProperties microTransactionProperties) {
        return new GlobalTransactionScanner(microTransactionProperties.getApplicationId(),
                microTransactionProperties.getTxServiceGroup());
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
        mybatisConfiguration.setDefaultEnumTypeHandler(EnumTypeHandler.class);
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
     * @param microTenantProperties {@link MicroTenantProperties}
     * @return {@link PaginationInterceptor}
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(
            MicroMybatisProperties microMybatisProperties,
            MicroTenantProperties microTenantProperties) {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        if (microMybatisProperties.isBlockAttack()) {
            // 攻击 SQL 阻断解析器、加入解析链
            sqlParserList.add(new BlockAttackSqlParser());
        }
        if (microTenantProperties.isEnable()) {
            sqlParserList.add(new MicroTenantSqlParser(microTenantProperties));
            paginationInterceptor.setSqlParserFilter(metaObject -> {
                MappedStatement mappedStatement = SqlParserHelper.getMappedStatement(metaObject);
                return microTenantProperties.getSkipMapperIds().contains(mappedStatement.getId());
            });
        }

        if (sqlParserList.size() > 0) {
            paginationInterceptor.setSqlParserList(sqlParserList);
        }
        return paginationInterceptor;
    }

    /**
     * SQL Performance Interceptor
     * <p>
     * tip: dev,test environment enable=true
     */
    @Bean
    @Profile({"dev", "test"})
    @ConditionalOnProperty(prefix = "micro.mybatis", name = "performance", havingValue = "true")
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * Optimistic Locker Interceptor
     *
     * @return {@link OptimisticLockerInterceptor}
     */
    @Bean
    @ConditionalOnProperty(prefix = "micro.mybatis", name = {"optimisticLocker", "optimistic-locker"}, havingValue = "true")
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * IBatis Trace Interceptor
     *
     * @return {@link TraceExpendInterceptor}
     */
    @Bean
    @ConditionalOnProperty(prefix = "micro.mybatis", name = {"traceExpend", "trace-expend"}, havingValue = "true")
    public TraceExpendInterceptor iBatisTraceInterceptor() {
        return new TraceExpendInterceptor();
    }

}