package cn.micro.biz.entity.spider;

import cn.micro.biz.commons.mybatis.MicroEntity;
import cn.micro.biz.service.spider.support.SpiderApp;
import cn.micro.biz.service.spider.support.SpiderAttr;
import cn.micro.biz.service.spider.support.SpiderAttrs;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * Spider Goods
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("spider_goods")
public class SpiderGoods extends MicroEntity<SpiderGoods> {

    /**
     * 统一商品ID
     */
    @SpiderAttrs({@SpiderAttr(app = SpiderApp.TAO_BAO, value = "nid")})
    private String unifiedId;
    /**
     * 商品标题
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "title")
    private String title;
    /**
     * 商品来源平台
     */
    private Integer app;
    /**
     * 原始标题
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "raw_title")
    private String rawTitle;
    /**
     * 类别
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "category")
    private String categoryCode;
    /**
     * 商品售价
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "view_price")
    private BigDecimal salePrice;
    /**
     * 商品原价
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "raw_price")
    private BigDecimal rawPrice;
    /**
     * 销量
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "view_sales")
    private Integer viewSales;
    /**
     * 主图地址
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "pic_url", isUrl = true)
    private String picUrl;
    /**
     * 商品详情页面
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "detail_url", isUrl = true)
    private String detailUrl;

    /**
     * 商店名称
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "nick")
    private String shopName;
    /**
     * 商店地址
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "shopLink", isUrl = true)
    private String shopLink;
    /**
     * 发货地址
     */
    @SpiderAttr(app = SpiderApp.TAO_BAO, value = "item_loc")
    private String shopAddress;

}
