package cn.micro.biz.commons.mybatis;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

@Data
@ToString
@ConfigurationProperties(prefix = "micro.transaction")
public class TransactionProperties implements Serializable {

    private boolean enable = false;
    private String applicationId;
    private String txServiceGroup = "my_tx_group";

}
