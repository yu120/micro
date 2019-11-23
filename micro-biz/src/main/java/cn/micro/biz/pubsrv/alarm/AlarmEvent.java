package cn.micro.biz.pubsrv.alarm;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

/**
 * AlarmLevel
 *
 * @author lry
 */
@Getter
@AllArgsConstructor
public class AlarmEvent implements Serializable {

    /**
     * Event id
     */
    private String id;
    /**
     * Event key(unique identification)
     */
    private String key;
    /**
     * Event keywords
     */
    private String keywords;
    /**
     * Event happen time
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date happen;
    /**
     * Event title
     */
    private String title;
    /**
     * Open event url
     */
    private String url;
    /**
     * Event intro
     */
    private String intro;
    /**
     * Application name
     */
    private String app;
    /**
     * Application env
     */
    private String env;

}
