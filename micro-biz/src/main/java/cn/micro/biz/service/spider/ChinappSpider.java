package cn.micro.biz.service.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import org.jsoup.nodes.Document;

public class ChinappSpider implements PageProcessor {

    private Site site = Site.me().setRetryTimes(10).setSleepTime(1000);

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        Document document = page.getHtml().getDocument();
        System.out.println(document.html());
    }

    public static void main(String[] args) {
        Spider.create(new ChinappSpider())
                .addUrl("https://www.chinapp.com").thread(1).run();
    }

}
