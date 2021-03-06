package cn.micro.biz.pubsrv.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 短信发送结果
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendResult implements Serializable {

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
