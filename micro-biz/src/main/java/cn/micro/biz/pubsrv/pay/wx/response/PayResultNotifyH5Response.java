package cn.micro.biz.pubsrv.pay.wx.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * 支付结果通知模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_7&index=8}
 *
 * @author lry
 */
@Data
@ToString
public class PayResultNotifyH5Response extends WXPayResponse {

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
     * 签名类型
     */
    @JSONField(name = "sign_type")
    private String signType;
    /**
     * 业务结果
     */
    @JSONField(name = "result_code")
    private String resultCode;
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

    /**
     * 用户标识
     */
    @JSONField(name = "openid")
    private String openid;
    /**
     * 是否关注公众账号
     */
    @JSONField(name = "is_subscribe")
    private String isSubscribe;
    /**
     * 交易类型
     */
    @JSONField(name = "trade_type")
    private String tradeType;
    /**
     * 付款银行
     */
    @JSONField(name = "bank_type")
    private String bankType;
    /**
     * 订单金额
     */
    @JSONField(name = "total_fee")
    private Integer totalFee;
    /**
     * 应结订单金额
     */
    @JSONField(name = "settlement_total_fee")
    private Integer settlementTotalFee;
    /**
     * 货币种类
     */
    @JSONField(name = "fee_type")
    private String feeType;
    /**
     * 现金支付金额
     */
    @JSONField(name = "cash_fee")
    private Integer cashFee;
    /**
     * 现金支付货币类型
     */
    @JSONField(name = "cash_fee_type")
    private String cashFeeType;
    /**
     * 总代金券金额
     */
    @JSONField(name = "coupon_fee")
    private Integer couponFee;
    /**
     * 代金券使用数量
     */
    @JSONField(name = "coupon_count")
    private Integer couponCount;
    /**
     * 代金券类型
     */
    @JSONField(name = "coupon_type_$n")
    private String couponType_$n;
    /**
     * 代金券ID
     */
    @JSONField(name = "coupon_id_$n")
    private String couponId_$n;
    /**
     * 单个代金券支付金额
     */
    @JSONField(name = "coupon_fee_$n")
    private Integer couponFee_$n;

    /**
     * 微信支付订单号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;
    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 商家数据包
     */
    @JSONField(name = "attach")
    private String attach;
    /**
     * 支付完成时间
     */
    @JSONField(name = "time_end")
    private String timeEnd;

}
