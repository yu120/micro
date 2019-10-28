package cn.micro.biz.pubsrv.hook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Outgoing Result
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutgoingResult implements Serializable {

    /**
     * true表示成功
     */
    private Boolean success;
    /**
     * 描述信息
     */
    private String msg;
    /**
     * 原始报文
     */
    private String plain;

}
