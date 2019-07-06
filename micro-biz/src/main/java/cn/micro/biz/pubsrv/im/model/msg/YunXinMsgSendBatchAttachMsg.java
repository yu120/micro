package cn.micro.biz.pubsrv.im.model.msg;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * YunXin Msg Send Batch Attach Msg
 *
 * @author lry
 */
@Data
@ToString
public class YunXinMsgSendBatchAttachMsg implements Serializable {

    private String fromAccid;
    private String toAccid;
    private String attach;
    private String pushcontent;
    private String payload;
    private String sound;
    private int save;
    private String option;

}
