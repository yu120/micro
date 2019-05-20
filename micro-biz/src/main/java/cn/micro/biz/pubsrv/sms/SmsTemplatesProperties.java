package cn.micro.biz.pubsrv.sms;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class SmsTemplatesProperties implements Serializable {

    private String signName;
    private List<String> templateParams;

}