package cn.micro.biz.pubsrv.pay.wx.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 统一下单H5请求模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_20&index=1}
 *
 * @author lry
 */
@Data
@ToString
public class UnifiedOrderH5Request implements Serializable {

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
     * 设备号
     */
    @JSONField(name = "device_info")
    private String deviceInfo;
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
    private String signType;
    /**
     * 商品描述
     */
    @JSONField(name = "body")
    private String body;
    /**
     * 商品详情
     */
    @JSONField(name = "detail")
    private String detail;
    /**
     * 附加数据
     */
    @JSONField(name = "attach")
    private String attach;
    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 货币类型
     */
    @JSONField(name = "fee_type")
    private String feeType;
    /**
     * 总金额
     */
    @JSONField(name = "total_fee")
    private Integer totalFee;
    /**
     * 终端IP
     */
    @JSONField(name = "spbill_create_ip")
    private String spBillCreateIp;
    /**
     * 交易起始时间
     */
    @JSONField(name = "time_start")
    private String timeStart;
    /**
     * 交易结束时间
     */
    @JSONField(name = "time_expire")
    private String timeExpire;
    /**
     * 商品标记
     */
    @JSONField(name = "goods_tag")
    private String goodsTag;
    /**
     * 通知地址
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;
    /**
     * 交易类型
     */
    @JSONField(name = "trade_type")
    private String tradeType;
    /**
     * 商品ID
     */
    @JSONField(name = "product_id")
    private String productId;
    /**
     * 指定支付方式
     */
    @JSONField(name = "limit_pay")
    private String limitPay;
    /**
     * 用户标识
     */
    @JSONField(name = "openid")
    private String openid;
    /**
     * 电子发票入口开放标识
     */
    @JSONField(name = "receipt")
    private String receipt;
    /**
     * 场景信息
     */
    @JSONField(name = "scene_info")
    private String sceneInfo;

}
