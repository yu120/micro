package cn.micro.biz.dubbo;

import cn.micro.biz.dubbo.provider.DemoService;
import cn.micro.biz.dubbo.provider.impl.DemoServiceImpl;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException {
        ServiceConfig<DemoService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setApplication(new ApplicationConfig("micro-dubbo-provider"));
        serviceConfig.setRegistry(new RegistryConfig("zookeeper://127.0.0.1:2181"));
        serviceConfig.setInterface(DemoService.class);
        serviceConfig.setRef(new DemoServiceImpl());
        serviceConfig.export();
        System.in.read();
    }
}