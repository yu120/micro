package cn.micro.biz.pubsrv.dingtalk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class DingTalkResponse implements Serializable {

    @JSONField(name = "errcode")
    private Long errCode;
    @JSONField(name = "errmsg")
    private String errMsg;

}
