package cn.micro.biz.dubbo.provider.impl;

import cn.micro.biz.dubbo.provider.DemoService;
import org.apache.dubbo.config.annotation.Service;

@Service
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println("===" + name);
        return "Hello " + name;
    }
}