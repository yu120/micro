package cn.micro.biz.commons.mybatis;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Micro Mybatis Properties
 *
 * @author lry
 */
@Data
@ToString
public class MicroMybatisProperties implements Serializable {

    public static final String TIMESTAMP_KEY = "timestamp";

    /**
     * Block attack sql parser enable
     */
    private boolean blockAttack = true;
    /**
     * SQL performance interceptor enable
     */
    private boolean performance = true;
    /**
     * Optimistic locker interceptor enable
     */
    private boolean optimisticLocker = true;
    /**
     * Trace expend interceptor enable
     */
    private boolean traceExpend = true;
    /**
     * Micro tenant properties
     */
    private MicroTenantProperties tenant = new MicroTenantProperties();
    /**
     * Micro transaction properties
     */
    private MicroTransactionProperties transaction = new MicroTransactionProperties();

    @Data
    @ToString
    public static class MicroTenantProperties implements Serializable {
        /**
         * Tenant enable
         */
        private boolean enable = true;
        /**
         * Tenant id db column(default: tenant_id)
         */
        private String column = "tenant_id";
        /**
         * Tenant default value
         */
        private Long defaultValue = 1L;
        /**
         * Exclude table name list
         */
        private List<String> excludeTables = Arrays.asList("tenant", "area");
        /**
         * Skip mapper id list(Mapper method name list)
         * <p>
         * eg: cn.micro.biz.mapper.member.IMemberMapper.selectInfo
         */
        private List<String> skipMapperIds = new ArrayList<>();
    }

    @Data
    @ToString
    public static class MicroTransactionProperties implements Serializable {
        /**
         * Transaction enable
         */
        private boolean enable = true;
        /**
         * Default timeout
         */
        private int defaultTimeout = 60;

        /**
         * Distributed transaction: seata
         */
        private boolean seata = false;
        /**
         * Application id
         */
        private String applicationId;
        /**
         * Transaction service group
         */
        private String txServiceGroup = "my_tx_group";
    }

}
