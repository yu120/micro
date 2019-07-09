package cn.micro.biz.dubbo;

import cn.micro.biz.dubbo.provider.DemoService;
import cn.micro.biz.dubbo.provider.impl.DemoServiceImpl;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.micro.neural.common.utils.ClassUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@EnableDubbo(scanBasePackages = "cn.micro.biz.dubbo.provider.impl")
@PropertySource("classpath:dubbo-provider.properties")
public class Application {

    public static void main(String[] args) throws IOException {
        ClassUtils.getClasses("cn.micro.biz.dubbo.provider.impl");
    }

    public static void main() throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Application.class);
        context.refresh();
        System.out.println("DemoService provider is starting...");
        System.in.read();

        // 服务实现
        DemoService xxxService = new DemoServiceImpl();

        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("xxx");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setAddress("10.20.130.230:9090");
        registry.setUsername("aaa");
        registry.setPassword("bbb");

        // 服务提供者协议配置
        ProtocolConfig protocol = new ProtocolConfig();
        protocol.setName("dubbo");
        protocol.setPort(12345);
        protocol.setThreads(200);

        // 注意：ServiceConfig为重对象，内部封装了与注册中心的连接，以及开启服务端口

        // 服务提供者暴露服务配置
        // 此实例很重，封装了与注册中心的连接，请自行缓存，否则可能造成内存和连接泄漏
        ServiceConfig<DemoService> service = new ServiceConfig<>();
        service.setApplication(application);
        // 多个注册中心可以用setRegistries()
        service.setRegistry(registry);
        // 多个协议可以用setProtocols()
        service.setProtocol(protocol);
        service.setInterface(DemoService.class);
        service.setInterface(DemoService.class);
        service.setRef(xxxService);
        service.setVersion("1.0.0");

        // 暴露及注册服务
        service.export();
    }
}