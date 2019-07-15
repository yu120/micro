package cn.micro.biz.dubbo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;

@EnableDubbo(scanBasePackages = "cn.micro.biz.dubbo.provider.impl")
@PropertySource("classpath:dubbo-provider.properties")
public class Application {

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(Application.class);
        context.refresh();
        System.out.println("DemoService provider is starting...");
        System.in.read();
    }

}