package cn.micro.biz.pubsrv.lanhu;

import cn.micro.biz.pubsrv.AbstractHttp;
import org.jsoup.Jsoup;

public class Lanhu extends AbstractHttp {

    private final static String LAN_HU_URI = "https://lanhuapp.com/api";

    public Lanhu() {
        super(LAN_HU_URI);
    }

    public void accountLogin(String email, String password) {
        //request()
    }

}
