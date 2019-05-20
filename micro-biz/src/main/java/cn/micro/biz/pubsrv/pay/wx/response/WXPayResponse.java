package cn.micro.biz.pubsrv.pay.wx.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class WXPayResponse implements Serializable {

    /**
     * 返回状态码
     */
    @JSONField(name = "return_code")
    protected String returnCode;
    /**
     * 返回信息
     */
    @JSONField(name = "return_msg")
    protected String returnMsg;

}
