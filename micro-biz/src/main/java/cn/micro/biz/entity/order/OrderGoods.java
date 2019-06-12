package cn.micro.biz.entity.order;

import cn.micro.biz.commons.mybatis.MicroEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 订单-商品
 * <p>
 * 设计说明：商品可能被修改、删除等，因此这里要记录下单时用户关注的商品交易摘要信息，
 * 如价格、数量、型号、型号参数等。
 * 这样就算后来商品被删除了，用户在查看历史订单的时候也依然能看到商品的快照信息。
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("order_goods")
public class OrderGoods extends MicroEntity<OrderGoods> {

    private static final long serialVersionUID = 1L;

    /**
     * Order id
     *
     * @see Order#id
     **/
    private Long orderId;
    /**
     * Goods id
     *
     * @see cn.micro.biz.entity.goods.Goods#id
     **/
    private Long goodsId;

    /**
     * 商品名称(商品可能删除,所以这里要记录,不能直接读商品表)
     **/
    private String goodsName;
    /**
     * 商品价格(商品可能删除,所以这里要记录)
     **/
    private BigDecimal goodsPrice;
    /**
     * 商品型号(前台展示给客户)
     **/
    private String goodsMarque;
    /**
     * 商品条码(商品仓库条码)
     **/
    private String goodsStoreBarcode;
    /**
     * 商品型号信息(记录详细商品型号，如颜色、规格、包装等)
     **/
    private String goodsModeDesc;
    /**
     * 商品型号参数(JSON格式，记录单位编号、颜色编号、规格编号等)
     **/
    private String goodsModeParams;
    /**
     * 折扣比例(打几折)
     **/
    private BigDecimal discountRate;
    /**
     * 折扣金额
     **/
    private BigDecimal discountAmount;
    /**
     * 购买数量
     **/
    private Integer goodNumber;
    /**
     * 小计金额
     **/
    private BigDecimal subtotal;
    /**
     * 商品是否有效
     **/
    private Integer goodsExists;
    /**
     * 客户商品备注
     **/
    private String remark;

}
