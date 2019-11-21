package cn.micro.biz.service.spider.impl;

import cn.micro.biz.entity.spider.SpiderGoodsEntity;
import cn.micro.biz.mapper.spider.ISpiderGoodsMapper;
import cn.micro.biz.service.spider.ISpiderGoodsService;
import cn.micro.biz.service.spider.support.SpiderApp;
import cn.micro.biz.service.spider.support.SpiderAttr;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Spider Goods Service Implements
 *
 * @author lry
 */
@Slf4j
@Service
public class SpiderGoodsServiceImpl extends ServiceImpl<ISpiderGoodsMapper, SpiderGoodsEntity> implements ISpiderGoodsService {

    private static final String REGEX = "g_page_config = \\{(.*?)\\}\\;";
    private static final Double START_PRICE = 10.00;
    private static final Double END_PRICE = 1000.00;
    private static final Integer VIEW_SALES = 100;
    private static final String TAO_BAO_COOKIE = "cookie2=1836ed7f2c09c6044dacab6550f44632; t=260997cf9f7d2d3817ed05afc967c33e; _tb_token_=b77a3375e57; cna=fNdaFTC1lH8CAXZ0D5HWhFc1; v=0; unb=667973791; uc3=id2=VWn7iBuaIu92&nk2=o%2FNV%2Fiqr4j4%3D&lg2=WqG3DMC9VAQiUQ%3D%3D&vt3=F8dBy3MPTaQh7Cuwx8M%3D; csg=08481633; lgc=%5Cu674E%5Cu666F%5Cu67AB99; cookie17=VWn7iBuaIu92; dnk=%5Cu674E%5Cu666F%5Cu67AB99; skt=7ee374f9f2ef4151; existShop=MTU2NjQ0ODg2Mw%3D%3D; uc4=id4=0%40V8jl9Eau4DFYkyfyD0uxOxjLWtE%3D&nk4=0%40oaWe6%2FXC2uIBjhJyIiy6iAMeEA%3D%3D; publishItemObj=Ng%3D%3D; tracknick=%5Cu674E%5Cu666F%5Cu67AB99; _cc_=W5iHLLyFfA%3D%3D; tg=0; _l_g_=Ug%3D%3D; sg=91e; _nk_=%5Cu674E%5Cu666F%5Cu67AB99; cookie1=VvbcIEAFt2%2FrmQFJFSw9rt1%2BJ93GGSXyVI6VGoVEtWU%3D; enc=KsYgvf7LTp7GTxl4JmZfL71eB81VZJnRmMshZ5mXdHBt6ByK%2BVtLRFSRxX9x1yiSRR90FuxfT6otbIX3CMPWkg%3D%3D; mt=ci=43_1; thw=cn; hng=CN%7Czh-CN%7CCNY%7C156; alitrackid=www.taobao.com; lastalitrackid=nz.taobao.com; uc1=cookie14=UoTaHo8K%2F2PpOg%3D%3D&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D; JSESSIONID=E95A1BD027C8E1837E9649FB61D05CF4; l=cBPTxLtqqz-YKEKyBOCZourza779SIRAguPzaNbMi_5pr6L6iY_OkJz0nFp6cjWdtrLB4M4Lmjp9-etbi-y06Pt-g3fP.; isg=BCAgni6XaT17I9U5UJ1sQ4li8Shj9wTzdN4IGJox6DvOlcC_QjhRgz4nKX2wJbzL";
    private static final List<String> CATEGORY_NAMES = new ArrayList<>();

    static {
//        CATEGORY_NAMES.add("保温杯");
//        CATEGORY_NAMES.add("饭盒");
//        CATEGORY_NAMES.add("玻璃杯");
//        CATEGORY_NAMES.add("马克杯");
//        CATEGORY_NAMES.add("餐具套装");
//        CATEGORY_NAMES.add("碗");
//        CATEGORY_NAMES.add("盘");
//        CATEGORY_NAMES.add("碟");
//        CATEGORY_NAMES.add("茶具套装");
//        CATEGORY_NAMES.add("茶杯");
//        CATEGORY_NAMES.add("茶壶");
//        CATEGORY_NAMES.add("茶具");
//        CATEGORY_NAMES.add("陶瓷餐具");
//        CATEGORY_NAMES.add("拖鞋");
//        CATEGORY_NAMES.add("雨伞雨具");
//        CATEGORY_NAMES.add("口罩");
//        CATEGORY_NAMES.add("垃圾桶");
//        CATEGORY_NAMES.add("居家鞋");
//        CATEGORY_NAMES.add("省力拖把");
//        CATEGORY_NAMES.add("家务清洁");
//        CATEGORY_NAMES.add("垃圾袋");
//        CATEGORY_NAMES.add("梳子");
//        CATEGORY_NAMES.add("抹布");
//        CATEGORY_NAMES.add("围裙");
//        CATEGORY_NAMES.add("拖把");
//        CATEGORY_NAMES.add("浴帘");
//        CATEGORY_NAMES.add("浴室置物架");
//        CATEGORY_NAMES.add("拖把桶旋转");
        CATEGORY_NAMES.add("镜子");
        CATEGORY_NAMES.add("铸铁锅");
        CATEGORY_NAMES.add("炒锅");
        CATEGORY_NAMES.add("饮具");
        CATEGORY_NAMES.add("心机小物");
        CATEGORY_NAMES.add("厨房置物架");
        CATEGORY_NAMES.add("密封罐");
        CATEGORY_NAMES.add("潮州陶瓷");
        CATEGORY_NAMES.add("景德镇陶瓷");
        CATEGORY_NAMES.add("厨用小工具");
        CATEGORY_NAMES.add("刀具砧板");
        CATEGORY_NAMES.add("烧烤烘培");
        CATEGORY_NAMES.add("餐厨");
        CATEGORY_NAMES.add("收纳整理");
        CATEGORY_NAMES.add("收纳箱");
        CATEGORY_NAMES.add("儿童收纳柜");
//        CATEGORY_NAMES.add("压缩袋");
//        CATEGORY_NAMES.add("衣柜整理");
//        CATEGORY_NAMES.add("鞋柜");
//        CATEGORY_NAMES.add("布艺软收纳");
//        CATEGORY_NAMES.add("浴室收纳");
//        CATEGORY_NAMES.add("置物架");
//        CATEGORY_NAMES.add("强力不粘钩");
//        CATEGORY_NAMES.add("厨房收纳");
//        CATEGORY_NAMES.add("桌面收纳");
//        CATEGORY_NAMES.add("壁挂收纳");
//        CATEGORY_NAMES.add("旅行收纳");
//        CATEGORY_NAMES.add("化妆包");
//        CATEGORY_NAMES.add("购物车");
//        CATEGORY_NAMES.add("环保袋");
//        CATEGORY_NAMES.add("野餐蓝");
//        CATEGORY_NAMES.add("药箱药盒");
//        CATEGORY_NAMES.add("衣物洗晒");
//        CATEGORY_NAMES.add("粘毛剪球");
//        CATEGORY_NAMES.add("脏衣篮");
//        CATEGORY_NAMES.add("木制衣架");
//        CATEGORY_NAMES.add("大型晾晒架");
//        CATEGORY_NAMES.add("裤架");
//        CATEGORY_NAMES.add("儿童衣架");
//        CATEGORY_NAMES.add("柳编");
//        CATEGORY_NAMES.add("ZAKKA风");
//        CATEGORY_NAMES.add("原生态");
//        CATEGORY_NAMES.add("棉麻风");
//        CATEGORY_NAMES.add("纸质收纳;");
    }

    @Override
    public Boolean spider(Integer appCategory) {
        for (String categoryName : CATEGORY_NAMES) {
            List<SpiderGoodsEntity> goodsList = processor(appCategory, categoryName, 1);
            if (CollectionUtils.isNotEmpty(goodsList)) {
                List<String> unifiedIds = goodsList.stream().map(SpiderGoodsEntity::getUnifiedId).collect(Collectors.toList());
                LambdaQueryWrapper<SpiderGoodsEntity> lambdaQueryWrapper = Wrappers.lambdaQuery();
                lambdaQueryWrapper.in(SpiderGoodsEntity::getUnifiedId, unifiedIds);
                List<SpiderGoodsEntity> dbGoodsList = this.list(lambdaQueryWrapper);
                List<String> hasUnifiedIds = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(goodsList)) {
                    hasUnifiedIds.addAll(dbGoodsList.stream().map(SpiderGoodsEntity::getUnifiedId).collect(Collectors.toList()));
                }

                List<SpiderGoodsEntity> resultList = new ArrayList<>();
                for (SpiderGoodsEntity goods : goodsList) {
                    System.out.println(goods.getDetailUrl());
                    if (hasUnifiedIds.contains(goods.getUnifiedId())) {
                        continue;
                    }
                    resultList.add(goods);
                }
                if (CollectionUtils.isNotEmpty(resultList)) {
                    System.out.println(this.saveBatch(resultList));
                }
            }
        }

        return true;
    }

    public List<SpiderGoodsEntity> processor(Integer appCategory, String categoryName, Integer page) {
        SpiderApp spiderApp = SpiderApp.parse(appCategory);
        List<SpiderGoodsEntity> resultList = new ArrayList<>();
        List<SpiderGoodsEntity> goodsList = parse(spiderApp, TAO_BAO_COOKIE, categoryName, START_PRICE, END_PRICE, page);
        for (SpiderGoodsEntity goods : goodsList) {
            if (goods.getViewSales() < VIEW_SALES) {
                continue;
            }
            goods.setUnifiedId(spiderApp.getCode() + goods.getUnifiedId());
            goods.setApp(spiderApp.getCategory());
            resultList.add(goods);
        }

        return resultList;
    }

    public List<SpiderGoodsEntity> parse(SpiderApp app, String cookie, String keywords, Double startPrice, Double endPrice, Integer page) {
        try {
            String json = parseJson(app, cookie, keywords, startPrice, endPrice, page);
            JSONObject jsonObject = JSON.parseObject(json);
            if (jsonObject == null) {
                return Collections.emptyList();
            }
            JSONObject mods = jsonObject.getJSONObject("mods");
            if (mods == null) {
                return Collections.emptyList();
            }
            JSONObject itemList = mods.getJSONObject("itemlist");
            if (itemList == null) {
                return Collections.emptyList();
            }
            JSONObject data = itemList.getJSONObject("data");
            if (data == null) {
                return Collections.emptyList();
            }
            JSONArray auctions = data.getJSONArray("auctions");
            if (auctions == null || auctions.isEmpty()) {
                return Collections.emptyList();
            }

            Map<SpiderApp, Map<String, SpiderAttr>> appFieldMapping = SpiderApp.APP_FIELD_MAPPING;
            Map<String, SpiderAttr> fieldMapping = appFieldMapping.get(app);
            int auctionsSize = auctions.size();
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (int i = 0; i < auctionsSize; i++) {
                JSONObject goodsJson = new JSONObject();
                JSONObject tempJson = auctions.getJSONObject(i);
                for (Map.Entry<String, SpiderAttr> entry : fieldMapping.entrySet()) {
                    SpiderAttr attr = entry.getValue();
                    Object value = tempJson.get(attr.value());
                    if (attr.isUrl()) {
                        if (String.valueOf(value).startsWith("//")) {
                            value = "https:" + value;
                        }
                    }

                    if ("view_sales".equals(attr.value())) {
                        String salesStr = String.valueOf(value).replace("人付款", "").replace("人收货", "");
                        Integer sales;
                        if (salesStr.contains("万+")) {
                            sales = (int) (Double.valueOf(salesStr.substring(0, salesStr.length() - 2)) * 10000);
                        } else if (salesStr.contains("+")) {
                            sales = Integer.valueOf(salesStr.substring(0, salesStr.length() - 1));
                        } else {
                            sales = Integer.valueOf(salesStr);
                        }
                        goodsJson.put(entry.getKey(), sales);
                    } else {
                        goodsJson.put(entry.getKey(), value);
                    }
                }
                jsonObjectList.add(goodsJson);
            }

            return JSON.parseArray(JSON.toJSONString(jsonObjectList), SpiderGoodsEntity.class);
        } catch (Exception e) {
            log.error("商品解析失败", e);
            return Collections.emptyList();
        }
    }

    private String parseJson(SpiderApp app, String cookie, String keywords, Double startPrice, Double endPrice, Integer page) throws Exception {
        String url = app.apply(keywords, startPrice, endPrice, page);
        System.out.println(url);
        Connection connection = Jsoup.connect(url);
        connection.header("cookie", cookie);
        Document document = connection.get();
        Elements elements = document.select("script");
        for (Element element : elements) {
            String data = element.data();
            if (data.contains("g_page_config")) {
                return getJsonData(data);
            }
        }

        return "{}";
    }

    private String getJsonData(String data) {
        Matcher matcher = Pattern.compile(REGEX).matcher(data);
        if (matcher.find()) {
            return "{" + matcher.group(1) + "}";
        }

        return "{}";
    }

}
