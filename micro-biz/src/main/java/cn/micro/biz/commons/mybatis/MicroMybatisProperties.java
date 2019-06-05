package cn.micro.biz.commons.mybatis;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

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
    private boolean optimisticLocker = false;

}
