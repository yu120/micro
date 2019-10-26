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
     * 发送结果状态,true表示成功
     */
    private Boolean success;
    /**
     * 结果JSON参数
     */
    private String resultJson;

}
