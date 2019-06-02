package cn.micro.biz;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
public class JasyptTest {

    @Autowired
    StringEncryptor encryptor;

    @Test
    public void test() {
        String url = encryptor.encrypt("jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf-8");
        String username = encryptor.encrypt("root");
        String password = encryptor.encrypt("123456");
        System.out.println("URL:" + url);
        System.out.println("用户名：" + username);
        System.out.println("密码：" + password);
    }

}