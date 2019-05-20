package cn.micro.biz.pubsrv.pay.wx.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 关闭订单H5请求模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_3&index=3}
 *
 * @author lry
 */
@Data
@ToString
public class CloseOrderH5Request implements Serializable {

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
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    /**
     * 随机字符串
     */
    @JSONField(name = "nonce_str")
    private String nonceStr;
    /**
     * 签名
     */
    @JSONField(name = "sign")
    private String sign;
    /**
     * 签名类型
     */
    @JSONField(name = "sign_type")
    private String sign_type;

}
