package cn.micro.biz.entity.order;

import cn.micro.biz.commons.mybatis.entity.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 微信支付结果记录表
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("wx_pay_notify")
public class WxPayNotify extends MicroEntity<WxPayNotify> {

    private static final long serialVersionUID = 1L;

    /**
     * 返回状态码
     **/
    private String returnCode;
    /**
     * 返回信息
     **/
    private String returnMsg;
    /**
     * 商户号
     **/
    private String mchId;
    /**
     * 业务结果
     **/
    private String resultCode;
    /**
     * 错误代码
     **/
    private String errCode;
    /**
     * 错误代码描述
     **/
    private String errCodeDes;
    /**
     * 用户标识
     **/
    private String openid;
    /**
     * 交易类型
     **/
    private String tradeType;
    /**
     * 订单金额
     **/
    private Integer totalFee;
    /**
     * 应结订单金额
     **/
    private Integer settlementTotalFee;
    /**
     * 微信支付订单号
     **/
    private String transactionId;
    /**
     * 商户订单号
     **/
    private String outTradeNo;
    /**
     * 支付完成时间
     **/
    private String timeEnd;
    
    /**
     * JSON数据包
     */
    private String data;

}
