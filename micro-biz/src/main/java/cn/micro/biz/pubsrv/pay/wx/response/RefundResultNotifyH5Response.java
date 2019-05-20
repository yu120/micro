package cn.micro.biz.pubsrv.pay.wx.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * 退款结果通知H5响应模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_16&index=10}
 *
 * @author lry
 */
@Data
@ToString
public class RefundResultNotifyH5Response extends WXPayResponse {

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
     * 加密信息
     */
    @JSONField(name = "req_info")
    private String reqInfo;

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
     * 微信退款单号
     */
    @JSONField(name = "refund_id")
    private String refundId;
    /**
     * 商户退款单号
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;
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
     * 申请退款金额
     */
    @JSONField(name = "refund_fee")
    private Integer refundFee;
    /**
     * 退款金额
     */
    @JSONField(name = "settlement_refund_fee")
    private Integer settlementRefundFee;

    /**
     * 退款状态
     */
    @JSONField(name = "refund_status")
    private String refundStatus;
    /**
     * 退款成功时间
     */
    @JSONField(name = "success_time")
    private String successTime;
    /**
     * 退款入账账户
     */
    @JSONField(name = "refund_recv_accout")
    private String refundRecvAccout;
    /**
     * 退款资金来源
     */
    @JSONField(name = "refund_account")
    private String refundAccount;
    /**
     * 退款发起来源
     */
    @JSONField(name = "refund_request_source")
    private String refundRequestSource;

}
