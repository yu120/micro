package cn.micro.biz.pubsrv.pay.wx.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * 关闭订单H5响应模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_3&index=3}
 *
 * @author lry
 */
@Data
@ToString
public class CloseOrderH5Response extends WXPayResponse {

    /**
     * 公众账号ID
     */
    @JSONField(name = "appid")
    private String appId;
    /**
     * 商户号
     */
    @JSONField(name = "mch_id")
    private String mchId;
    /**
     * 机字符串
     */
    @JSONField(name = "nonce_str")
    private String nonceStr;
    /**
     * 签名
     */
    @JSONField(name = "sign")
    private String sign;
    /**
     * 业务结果
     */
    @JSONField(name = "result_code")
    private String resultCode;
    /**
     * 业务结果描述
     */
    @JSONField(name = "result_msg")
    private String resultMsg;
    /**
     * 错误代码
     */
    @JSONField(name = "err_code")
    private String errCode;
    /**
     * 错误代码描述
     */
    @JSONField(name = "err_code_des")
    private String errCodeDes;

}
