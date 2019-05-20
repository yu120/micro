package cn.micro.biz.pubsrv.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableConfigurationProperties(EmailProperties.class)
@ConditionalOnProperty(prefix = "micro.email", name = "enable", havingValue = "true")
public class EmailService implements InitializingBean {

    private static final String SFC_KEY = "mail.smtp.socketFactory.class";
    private static final String SFC_VALUE = "javax.net.ssl.SSLSocketFactory";

    private final EmailProperties emailProperties;
    private final TemplateEngine templateEngine;

    private JavaMailSenderImpl javaMailSender;

    @Override
    public void afterPropertiesSet() {
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty(SFC_KEY, SFC_VALUE);
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(emailProperties.getHost());
        javaMailSender.setPort(emailProperties.getPort());
        javaMailSender.setUsername(emailProperties.getUsername());
        javaMailSender.setPassword(emailProperties.getPassword());
        javaMailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());
        javaMailSender.setJavaMailProperties(javaMailProperties);
    }

    public void sendSimpleMail(EmailMessage emailMessage) throws Exception {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
        mmh.setFrom(new InternetAddress(emailProperties.getUsername(),
                emailProperties.getPersonal(), StandardCharsets.UTF_8.name()));
        mmh.setSubject(emailMessage.getSubject());
        mmh.setTo(emailMessage.getRecipients().toArray(new String[0]));

        Context context = new Context();
        context.setVariables(emailMessage.getVariables());
        String content = this.templateEngine.process(emailMessage.getTemplate(), context);
        mmh.setText(content, true);

        // 添加附件
        if (emailMessage.getAttachment() != null && emailMessage.getAttachment().size() > 0) {
            for (String attach : emailMessage.getAttachment()) {
                FileSystemResource fileSystemResource = new FileSystemResource(new File(attach));
                String fileName = attach.substring(attach.lastIndexOf(File.separator));
                mmh.addAttachment(fileName, fileSystemResource);
            }
        }

        javaMailSender.send(mimeMessage);
    }

}
