package cn.micro.biz.commons.mybatis;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.Duration;

@Data
@ToString
public class MicroTransactionProperties implements Serializable {

    /**
     * Transaction enable
     */
    private boolean enable = true;
    /**
     * Default timeout
     */
    private Duration defaultTimeout = Duration.ofSeconds(60L);

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
