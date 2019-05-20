package cn.micro.biz.pubsrv.pay.wx.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * 查询退款H5响应模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_5&index=5}
 *
 * @author lry
 */
@Data
@ToString
public class PayRefundQueryH5Response extends WXPayResponse {

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
     * 订单总退款次数
     */
    @JSONField(name = "total_refund_count")
    private Integer totalRefundCount;
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
     * 退款笔数
     */
    @JSONField(name = "refund_count")
    private Integer refundCount;
    /**
     * 商户退款单号
     */
    @JSONField(name = "out_refund_no_$n")
    private String outRefundNo_$n;
    /**
     * 微信退款单号
     */
    @JSONField(name = "refund_id_$n")
    private String refundId_$n;
    /**
     * 退款渠道
     */
    @JSONField(name = "refund_channel_$n")
    private String refundChannel_$n;
    /**
     * 申请退款金额
     */
    @JSONField(name = "refund_fee_$n")
    private Integer refundFee_$n;
    /**
     * 退款金额
     */
    @JSONField(name = "settlement_refund_fee_$n")
    private Integer settlementRefundFee_$n;
    /**
     * 代金券类型
     */
    @JSONField(name = "coupon_type_$n_$m")
    private String couponType_$n_$m;
    /**
     * 总代金券退款金额
     */
    @JSONField(name = "coupon_refund_fee_$n")
    private Integer couponRefundFee_$n;
    /**
     * 退款代金券使用数量
     */
    @JSONField(name = "coupon_refund_count_$n")
    private Integer couponRefundCount_$n;
    /**
     * 退款代金券ID
     */
    @JSONField(name = "coupon_refund_id_$n_$m")
    private String couponRefundId_$n_$m;
    /**
     * 单个代金券退款金额
     */
    @JSONField(name = "coupon_refund_fee_$n_$m")
    private Integer couponRefundFee_$n_$m;
    /**
     * 退款状态
     */
    @JSONField(name = "refund_status_$n")
    private String refundStatus_$n;
    /**
     * 退款资金来源
     */
    @JSONField(name = "refund_account_$n")
    private String refundAccount_$n;
    /**
     * 退款入账账户
     */
    @JSONField(name = "refund_recv_accout_$n")
    private String refundRecvAccout_$n;
    /**
     * 退款成功时间
     */
    @JSONField(name = "refund_success_time_$n")
    private String refundSuccessTime_$n;

}
