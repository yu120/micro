package cn.micro.biz.dubbo.provider;

import java.util.List;

public interface DemoService {

    String sayHello(String name);

    User test(User user);

    List<User> testList(User user);

    List<User> demo(List<User> users);

}