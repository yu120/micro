package cn.micro.biz.pubsrv.dingtalk;

import com.taobao.api.internal.mapping.ApiField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class DingTalkLinks implements Serializable {

    private String messageURL;
    private String picURL;
    private String title;
    
}
