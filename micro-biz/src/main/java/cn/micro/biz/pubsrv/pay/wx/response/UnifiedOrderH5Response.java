package cn.micro.biz.pubsrv.pay.wx.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * 统一下单H5响应模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_20&index=1}
 *
 * @author lry
 */
@Data
@ToString
public class UnifiedOrderH5Response extends WXPayResponse {

    /**
     * 公众账号ID
     **/
    @JSONField(name = "appid")
    private String appId;
    /**
     * 商户号
     **/
    @JSONField(name = "mch_id")
    private String mchId;
    /**
     * 设备号
     **/
    @JSONField(name = "device_info")
    private String deviceInfo;
    /**
     * 随机字符串
     **/
    @JSONField(name = "nonce_str")
    private String nonceStr;
    /**
     * 签名
     **/
    @JSONField(name = "sign")
    private String sign;
    /**
     * 业务结果
     **/
    @JSONField(name = "result_code")
    private String resultCode;
    /**
     * 错误代码
     **/
    @JSONField(name = "err_code")
    private String errCode;
    /**
     * 错误代码描述
     **/
    @JSONField(name = "err_code_des")
    private String errCodeDes;


    /**
     * 交易类型
     **/
    @JSONField(name = "trade_type")
    private String tradeType;
    /**
     * 预支付交易会话标识
     **/
    @JSONField(name = "prepay_id")
    private String prepayId;
    /**
     * 支付跳转链接
     **/
    @JSONField(name = "mweb_url")
    private String mwebUrl;

}
