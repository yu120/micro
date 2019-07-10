package cn.micro.biz.dubbo.provider.impl;

import cn.micro.biz.dubbo.provider.DemoService;
import cn.micro.biz.dubbo.provider.User;
import org.apache.dubbo.config.annotation.Service;

import java.util.List;

@Service//(path = "demo001",interfaceName = "dddddddd")
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        System.out.println("1===" + name);
        return "Hello " + name;
    }

    @Override
    public User test(User user) {
        System.out.println("2===" + user);
        return user;
    }

    @Override
    public List<User> demo(List<User> users) {
        System.out.println("3===" + users);
        return users;
    }

}