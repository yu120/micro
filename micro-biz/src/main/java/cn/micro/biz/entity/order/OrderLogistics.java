package cn.micro.biz.entity.order;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Order logistics entity
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("order_logistics")
public class OrderLogistics extends MicroEntity<OrderLogistics> {

    private static final long serialVersionUID = 1L;

    /**
     * Order id
     *
     * @see Order#id
     **/
    private Long orderId;

    /**
     * 收货人姓名 (收货地址表可能更新或删除，因此要在这里记录)
     **/
    private String consigneeRealName;
    /**
     * 联系电话 (收货地址表可能更新或删除，因此要在这里记录)
     **/
    private String consigneeTel;
    /**
     * 备用联系电话 (收货地址表可能更新或删除，因此要在这里记录)
     **/
    private String consigneeTelBackup;
    /**
     * 收货地址 (收货地址表可能更新或删除，因此要在这里记录)
     **/
    private String consigneeAddress;
    /**
     * 邮政编码 (收货地址表可能更新或删除，因此要在这里记录)
     **/
    private String consigneeZip;

    /**
     * 物流方式
     **/
    private Integer logisticsType;
    /**
     * 物流公司名称
     **/
    private String logisticsName;
    /**
     * 物流单号 (发货快递单号)
     **/
    private String expressNo;
    /**
     * 订单物流状态
     **/
    private Integer orderLogisticsStatus;

    /**
     * 物流发货运费(显示给客户的订单运费)
     **/
    private BigDecimal logisticsFee;
    /**
     * 快递代收货款费率(快递公司代收货款费率，如货值的2%-5%，一般月结)
     **/
    private BigDecimal agencyFee;
    /**
     * 物流成本金额(实际支付给物流公司的金额)
     **/
    private BigDecimal deliveryAmount;
    /**
     * 物流结算状态 (未结算,已结算,部分结算)
     **/
    private Integer logisticsSettlementStatus;
    /**
     * 物流最后状态描述
     **/
    private String logisticsIntroLast;
    /**
     * 物流描述
     **/
    private String logisticsIntro;
    /**
     * 发货时间
     **/
    private Date logisticsCreateTime;
    /**
     * 物流更新时间
     **/
    private Date logisticsUpdateTime;
    /**
     * 物流结算时间
     **/
    private Date logisticsSettlementTime;
    /**
     * 物流支付渠道
     **/
    private Integer logisticsPayChannel;
    /**
     * 物流支付单号
     **/
    private String logisticsPayNo;
    /**
     * 物流公司已对账状态 (已对账,未对账)
     **/
    private Integer reconciliationStatus;
    /**
     * 物流公司对账日期
     **/
    private Date reconciliationTime;

}
