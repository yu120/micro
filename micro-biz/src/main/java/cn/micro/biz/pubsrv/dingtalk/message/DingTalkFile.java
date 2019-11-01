package cn.micro.biz.pubsrv.dingtalk.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Ding Talk Action Card
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkFile implements Serializable {

    @JSONField(name = "media_id")
    private String mediaId;

}
