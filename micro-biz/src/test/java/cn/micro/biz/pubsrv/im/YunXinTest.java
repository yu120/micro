package cn.micro.biz.pubsrv.im;

import cn.micro.biz.pubsrv.im.model.user.YunXinUserCreate;

public class YunXinTest {
    public static void main(String[] args) {

        YunXinService yunXinService = new YunXinService();
        yunXinService.setYunXinProperties(new YunXinProperties());

        YunXinUserCreate yunXinUserCreate = new YunXinUserCreate();
        yunXinUserCreate.setAccid("1001");
//        yunXinService.userCreate(yunXinUserCreate);
//        System.out.println(yunXinUserCreate);
        System.out.println(yunXinService.userRefreshToken("1001"));
    }
}
