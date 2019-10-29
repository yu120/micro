package cn.micro.biz.entity.order;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Ali Pay Notify Entity
 * <p>
 * 通知触发条件:
 * 触发条件名	        触发条件描述	触发条件默认值
 * 1.TRADE_FINISHED	    交易完成	    false（不触发通知）
 * 2.TRADE_SUCCESS	    支付成功	    true（触发通知）
 * 3.WAIT_BUYER_PAY	    交易创建	    false（不触发通知）
 * 4.TRADE_CLOSED	    交易关闭	    true（触发通知）
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("ali_pay_notify")
public class AliPayNotifyEntity extends MicroEntity<AliPayNotifyEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * 通知时间
     */
    private Date notifyTime;
    /**
     * 通知类型
     */
    private String notifyType;
    /**
     * 通知校验ID
     */
    private String notifyId;
    /**
     * 支付宝交易号
     */
    private String tradeNo;
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 商户业务号
     */
    private String outBizNo;
    /**
     * 买家支付宝用户号
     */
    private String buyerId;
    /**
     * 买家支付宝账号
     */
    private String buyerLogonId;
    /**
     * 交易状态
     * <p>
     * 1.WAIT_BUYER_PAY:交易创建，等待买家付款
     * 2.TRADE_CLOSED:未付款交易超时关闭，或支付完成后全额退款
     * 3.TRADE_SUCCESS:交易支付成功
     * 4.TRADE_FINISHED:交易结束，不可退款
     */
    private String tradeStatus;
    /**
     * 订单金额
     */
    private BigDecimal totalAmount;
    /**
     * 实收金额
     */
    private BigDecimal receiptAmount;
    /**
     * 付款金额
     */
    private BigDecimal buyerPayAmount;
    /**
     * 总退款金额
     **/
    private BigDecimal refundFee;
    /**
     * 交易创建时间
     **/
    private Date gmtCreate;
    /**
     * 交易付款时间
     **/
    private Date gmtPayment;
    /**
     * 交易退款时间
     **/
    private Date gmtRefund;
    /**
     * 交易结束时间
     **/
    private Date gmtClose;
    /**
     * JSON数据包
     */
    private String data;

}
