package cn.micro.biz.entity.orders;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单-退货/退款
 * <p>
 * 设计说明：退货可能被修改、删除等，因此这里要记录退货时商家的退货地址信息
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("orders_return")
public class OrdersReturnEntity extends MicroEntity<OrdersReturnEntity> {

    /**
     * Order id
     *
     * @see OrdersEntity#id
     **/
    private Long orderId;

    /**
     * 退货编号(供客户查询)
     **/
    private String returnsNo;
    /**
     * 物流单号(退货物流单号)
     **/
    private String expressNo;
    /**
     * 收货人姓名
     **/
    private String consigneeRealName;
    /**
     * 联系电话
     **/
    private String consigneeTel;
    /**
     * 备用联系电话
     **/
    private String consigneeTelBackup;
    /**
     * 收货地址
     **/
    private String consigneeAddress;
    /**
     * 邮政编码
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
     * 物流发货运费(退货运费)
     **/
    private BigDecimal logisticsFee;
    /**
     * 物流状态
     **/
    private Integer orderLogisticsStatus;
    /**
     * 物流最后状态描述
     **/
    private String logisticsIntroLast;
    /**
     * 物流描述
     **/
    private String logisticsIntro;
    /**
     * 物流更新时间
     **/
    private Date logisticsUpdateTime;
    /**
     * 物流发货时间
     **/
    private Date logisticsCreateTime;
    /**
     * 退货类型 (全部退单,部分退单)
     **/
    private Integer returnsType;
    /**
     * 退货处理方式
     * <p>
     * PUPAWAY:退货入库
     * REDELIVERY:重新发货
     * RECLAIM-REDELIVERY:不要求归还并重新发货
     * REFUND:退款
     * COMPENSATION:不退货并赔偿
     **/
    private Integer handlingWay;
    /**
     * 退款金额
     **/
    private BigDecimal returnsAmount;
    /**
     * 退货销售员承担的费用
     **/
    private BigDecimal sellerPunishFee;
    /**
     * 退货申请时间
     **/
    private Date returnSubmitTime;
    /**
     * 退货处理时间
     **/
    private Date handlingTime;
    /**
     * 退货原因
     **/
    private String returnsReason;

}
