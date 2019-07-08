package cn.micro.biz.dubbo.provider.impl;

import cn.micro.biz.dubbo.provider.DemoService;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        System.out.println("===" + name);
        return "Hello " + name;
    }
}