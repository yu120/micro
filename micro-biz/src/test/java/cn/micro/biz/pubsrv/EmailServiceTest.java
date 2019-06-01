package cn.micro.biz.pubsrv;

import cn.micro.biz.pubsrv.email.EmailMessage;
import cn.micro.biz.pubsrv.email.EmailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void sendSimpleMail() throws Exception {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setRecipients(Collections.singletonList("595208882@qq.com"));
        emailMessage.setTemplate("mail");
        emailMessage.setSubject("张三哒哒哒哒哒哒");
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("title", "修改密码");
        valueMap.put("memberName", "张三");
        valueMap.put("codeType", "修改密码");
        valueMap.put("codeValue", "123456");
        emailMessage.setAttachment(Collections.singletonList("D:\\杨肖满.pdf"));
        emailMessage.setVariables(valueMap);
        emailService.sendSimpleMail(emailMessage);
    }

}
