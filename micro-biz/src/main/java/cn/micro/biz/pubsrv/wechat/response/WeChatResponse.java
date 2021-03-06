package cn.micro.biz.pubsrv.wechat.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * WeChat Response
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeChatResponse implements Serializable {

    @JSONField(name = "errcode")
    private Long errCode;
    @JSONField(name = "errmsg")
    private String errMsg;

}
