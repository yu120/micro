package cn.micro.biz.entity.order;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 订单-发票
 * https://www.cnblogs.com/sochishun/p/7040628.html
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("order_invoice")
public class OrderInvoiceEntity extends MicroEntity<OrderInvoiceEntity> {

    private static final long serialVersionUID = 1L;

    /**
     * Order id
     *
     * @see OrderEntity#id
     **/
    private Long orderId;

    /**
     * 是否增值税发票(普通发票,增值发票)
     */
    private Integer vat;
    /**
     * 发票抬头名称
     */
    private String title;
    /**
     * 发票抬头内容
     */
    private String content;
    /**
     * 发票金额
     */
    private BigDecimal amount;
    /**
     * 发票税号
     */
    private String taxNo;
    /**
     * 开票税金
     */
    private BigDecimal tax;
    /**
     * 公司名称[增值税]
     */
    private String vatCompanyName;
    /**
     * 公司地址[增值税]
     */
    private String vatCompanyAddress;
    /**
     * 联系电话[增值税]
     */
    private String vatTel;
    /**
     * 开户银行[增值税]
     */
    private String vatBankName;
    /**
     * 银行帐号[增值税]
     */
    private String vatBankAccount;

}
