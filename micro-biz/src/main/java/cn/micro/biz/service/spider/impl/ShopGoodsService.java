package cn.micro.biz.service.spider.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.micro.biz.entity.spider.SpiderGoodsEntity;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 抓取单个店铺的所有商品
 *
 * @author lry
 */
public class ShopGoodsService {

    public static void main(String[] args) throws Exception {
        String cookie = "t=430bf2f04ea1a50e00842306d0f61ca1; thw=cn; x=e%3D1%26p%3D*%26s%3D0%26c%3D0%26f%3D0%26g%3D0%26t%3D0%26__ll%3D-1%26_ato%3D0; ali_ab=171.221.137.112.1543159399970.5; _fbp=fb.1.1556972741577.943744343; UM_distinctid=16c5aebc0e92c6-0c0271982bf8cd-c343162-1fa400-16c5aebc0ea370; hng=CN%7Czh-CN%7CCNY%7C156; cookie2=1fa5f46fb06158dd306f726e596bdc05; _tb_token_=eaee81101efe7; _m_h5_tk=62db279193e8097cef7ef33ddfcc11bb_1567224171440; _m_h5_tk_enc=4e2282a51155f8975490c543e66b20c4; ctoken=6N2sQyMDXZvBK1SzLKErrhllor; cna=RspxFOqarkgCAavVDIoZOK81; v=0; unb=2202165304264; uc3=nk2=F5RDKmo5LeUQB%2BQ%3D&vt3=F8dBy3MH2pfQWNP%2Bm0E%3D&lg2=V32FPkk%2Fw0dUvg%3D%3D&id2=UUphyIl%2BY9Z3lws4lg%3D%3D; csg=b231cf38; lgc=tb665388665; cookie17=UUphyIl%2BY9Z3lws4lg%3D%3D; dnk=tb665388665; skt=8e663f17bcdf07ae; existShop=MTU2NzIxODc0Mg%3D%3D; uc4=id4=0%40U2grE1oPI%2BE3SGhpQtF6t7T8i4TpXhpZ&nk4=0%40FY4I6evGWh7MLVMxdnCAwqQAE9FTYA%3D%3D; tracknick=tb665388665; _cc_=URm48syIZQ%3D%3D; tg=0; _l_g_=Ug%3D%3D; sg=546; _nk_=tb665388665; cookie1=AHsrM12jGY5sV6ULbW%2BPbkG805Ic54laZLrBZ5fdI8c%3D; mt=ci=0_1; enc=FjpBdARKd2YaimC1hHFcaMqIqhPShefT7Z%2F0osdiWmyfI2I4lBTx5jyA0RidN6qM0kQbdVLzKpOiZV2ctebOIQ%3D%3D; CNZZDATA1277450732=246727287-1567220559-https%253A%252F%252Fshop367950010.taobao.com%252F%7C1567224615; uc1=cookie14=UoTaH0W8LhtJww%3D%3D&lng=zh_CN&cookie16=VFC%2FuZ9az08KUQ56dCrZDlbNdA%3D%3D&existShop=true&cookie21=V32FPkk%2Fhodroid0QSjisQ%3D%3D&tag=8&cookie15=U%2BGCWk%2F75gdr5Q%3D%3D&pas=0; x5sec=7b2273686f7073797374656d3b32223a223737313039353365373832616161656533356432353735633362346163346163434a6148714f7346454f436a733657356a616a6531674561447a49794d4449784e6a557a4d4451794e6a51374d513d3d227d; pnm_cku822=098%23E1hvmpvUvbpvUvCkvvvvvjiPRFSUAjnbnLLwljnEPmPWgjtRPFdWsjEhPss9zjrE2QhvCvvvMMGEvpCWmv25vvakfwFOaXgfNZ26D7zWVut%2BCNoxfw9fd5YWsnFpTEkOJZ5O%2BneYr2E9ZRAn3w0AhjHvTWex6fItb9TxfwL9ditkPrcv1UywbIyCvvOUvvVva6VivpvUvvmvnu4trC%2FtvpvIvvvvvhCvvvvvvU8ophvWI9vv96CvpC29vvm2phCvhhvvvUnvphvppvhCvvOvCvvvphvtvpvhvvvvv8wCvvpvvUmm; l=cBgRqioVqU3Iz1KXBOCwnurza77tsIRAguPzaNbMi_5hG6L6_zbOklr_YFp6cjWdTYLB4JuaUM29-etkihE70RMmxVO1.; isg=BCsr_6Qd8hiqZy5g5NB7SpJIuk_SDj_CmjBG-p2oDGrBPEueJRXsE-maljz3HJe6";
        List<SpiderGoodsEntity> spiderGoodsList = execute("https://shop367950010.taobao.com", cookie, 1);
        for (SpiderGoodsEntity spiderGoods : spiderGoodsList) {
            System.out.println(spiderGoods.getDetailUrl());
        }
    }

    public static List<SpiderGoodsEntity> execute(String shopUrl, String cookie, int pageNo) throws Exception {
        List<SpiderGoodsEntity> spiderGoodsList = new ArrayList<>();

        String url = (!shopUrl.endsWith("/") ? (shopUrl + "/") : shopUrl) + "i/asynSearch.htm?mid=w-21589203576-0&orderType=newOn_desc&pageNo=" + pageNo;
        Connection connection = Jsoup.connect(url);
        connection.header("cookie", cookie);
        Document document = connection.get();
        Elements elements = document.select("div[class=\\\"shop-hesper-bd]").select("div[class=\\\"item3line1\\\"]").select("dl");
        for (Element element : elements) {
            String dataId = element.attr("data-id").replace("\\\"", "");

            // LOGO
            String src = element.select("dt[class=\\\"photo\\\"]").select("a[class=\\\"J_TGoldData\\\"]").select("img").attr("src");
            src = "https:" + src.replace("\\\"", "");
            // 商品地址
            Elements detailElements = element.select("dd[class=\\\"detail\\\"]").select("a[class=\\\"item-name]");
            String title = detailElements.text();
            String href = detailElements.attr("href");
            href = "https:" + href.replace("\\\"", "");

            String cPrice = element.select("dd[class=\\\"detail\\\"]").select("span[class=\\\"c-price\\\"]").text();
            String sPrice = element.select("dd[class=\\\"detail\\\"]").select("span[class=\\\"s-price\\\"]").text();
            String saleNum = element.select("dd[class=\\\"detail\\\"]").select("span[class=\\\"sale-num\\\"]").text();

            SpiderGoodsEntity spiderGoods = new SpiderGoodsEntity();
            spiderGoods.setUnifiedId(dataId);
            spiderGoods.setTitle("");
            spiderGoods.setRawTitle(title);
            spiderGoods.setCategoryCode("");
            spiderGoods.setSalePrice(new BigDecimal(cPrice));
            spiderGoods.setRawPrice(new BigDecimal(sPrice));
            spiderGoods.setViewSales(Integer.valueOf(saleNum));
            spiderGoods.setPicUrl(src);
            spiderGoods.setDetailUrl(href);
            spiderGoods.setShopName("");
            spiderGoods.setShopLink(shopUrl);
            spiderGoods.setShopAddress("");
            spiderGoodsList.add(spiderGoods);
        }

        return spiderGoodsList;
    }
}
