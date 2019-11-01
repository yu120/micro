package cn.micro.biz.pubsrv.dingtalk.message;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Ding Talk Action Card
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DingTalkActionCard implements Serializable {

    @JSONField(name = "btn_json_list")
    private List<BtnJsonList> btnJsonList;
    @JSONField(name = "btn_orientation")
    private String btnOrientation;
    @JSONField(name = "markdown")
    private String markdown;
    @JSONField(name = "single_title")
    private String singleTitle;
    @JSONField(name = "single_url")
    private String singleUrl;
    @JSONField(name = "title")
    private String title;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BtnJsonList implements Serializable {
        @JSONField(name = "action_url")
        private String actionUrl;
        @JSONField(name = "title")
        private String title;
    }

}
