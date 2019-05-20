package cn.micro.biz.pubsrv.pay.wx.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * 关闭订单H5响应模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_4&index=4}
 *
 * @author lry
 */
@Data
@ToString
public class PayRefundH5Response extends WXPayResponse {

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
     * 微信订单号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;
    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 商户退款单号
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;
    /**
     * 微信退款单号
     */
    @JSONField(name = "refund_id")
    private String refundId;
    /**
     * 退款金额
     */
    @JSONField(name = "refund_fee")
    private Integer refundFee;
    /**
     * 应结退款金额
     */
    @JSONField(name = "settlement_refund_fee")
    private Integer settlementRefundFee;
    /**
     * 标价金额
     */
    @JSONField(name = "total_fee")
    private Integer totalFee;
    /**
     * 应结订单金额
     */
    @JSONField(name = "settlement_total_fee")
    private Integer settlementTotalFee;
    /**
     * 标价币种
     */
    @JSONField(name = "fee_type")
    private String feeType;
    /**
     * 现金支付金额
     */
    @JSONField(name = "cash_fee")
    private Integer cashFee;
    /**
     * 现金支付币种
     */
    @JSONField(name = "cash_fee_type")
    private String cashFeeType;
    /**
     * 现金退款金额
     */
    @JSONField(name = "cash_refund_fee")
    private Integer cashRefundFee;
    /**
     * 代金券类型
     */
    @JSONField(name = "coupon_type_$n")
    private String couponType_$n;
    /**
     * 代金券退款总金额
     */
    @JSONField(name = "coupon_refund_fee")
    private Integer couponRefundFee;
    /**
     * 单个代金券退款金额
     */
    @JSONField(name = "coupon_refund_fee_$n")
    private Integer couponRefundFee_$n;
    /**
     * 退款代金券使用数量
     */
    @JSONField(name = "coupon_refund_count")
    private Integer couponRefundCount;
    /**
     * 退款代金券ID
     */
    @JSONField(name = "coupon_refund_id_$n")
    private String couponRefundId_$n;

}
