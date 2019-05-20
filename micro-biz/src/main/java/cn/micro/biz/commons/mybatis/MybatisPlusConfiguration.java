package cn.micro.biz.commons.mybatis;

import cn.micro.biz.commons.exception.MicroErrorException;
import cn.micro.biz.commons.mybatis.extension.TraceExpendInterceptor;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.MetaObject;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
    private static final String DEFAULT_TENANT_TABLE_NAME = "tenant";

    private String mapperPackage;
    private String tenantIdColumn = "tenant_id";

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
     * @param druidDataSource {@link DataSource}
     * @return {@link PlatformTransactionManager}
     */
    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource druidDataSource) {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(druidDataSource);
        manager.setDefaultTimeout(60);
        return manager;
    }

    /**
     * Mybatis Mapper Scanner Configurer
     *
     * @return {@link MapperScannerConfigurer}
     */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer() {
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

    // ============ IBatis Trace Interceptor

    @Bean
    @ConfigurationProperties(prefix = "micro.tenant")
    public MicroTenantProperties microTenantProperties() {
        return new MicroTenantProperties();
    }

    /**
     * Paging interceptor
     *
     * @return {@link PaginationInterceptor}
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(MicroTenantProperties microTenantProperties) {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        if (!microTenantProperties.isEnable()) {
            return paginationInterceptor;
        }

        // build tenant id column
        if (microTenantProperties.getColumn() != null && microTenantProperties.getColumn().length() > 0) {
            tenantIdColumn = microTenantProperties.getColumn();
        }
        List<String> excludeTables = microTenantProperties.getExcludeTables();
        excludeTables.add(DEFAULT_TENANT_TABLE_NAME);

        List<ISqlParser> sqlParserList = new ArrayList<>();
        sqlParserList.add(new TenantSqlParser()
                .setTenantHandler(new TenantHandler() {
                    @Override
                    public Expression getTenantId() {
                        return new LongValue(1L);
                    }

                    @Override
                    public String getTenantIdColumn() {
                        return tenantIdColumn;
                    }

                    @Override
                    public boolean doTableFilter(String tableName) {
                        return excludeTables.contains(tableName);
                    }
                }));
        paginationInterceptor.setSqlParserList(sqlParserList);
        paginationInterceptor.setSqlParserFilter(metaObject -> {
            MappedStatement mappedStatement = SqlParserHelper.getMappedStatement(metaObject);
            return microTenantProperties.getSkipMapperIds().contains(mappedStatement.getId());
        });

        return paginationInterceptor;
    }

    /**
     * Optimistic Locker Interceptor
     *
     * @return {@link OptimisticLockerInterceptor}
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * IBatis Trace Interceptor
     *
     * @return {@link TraceExpendInterceptor}
     */
    @Bean
    public TraceExpendInterceptor iBatisTraceInterceptor() {
        return new TraceExpendInterceptor();
    }

}