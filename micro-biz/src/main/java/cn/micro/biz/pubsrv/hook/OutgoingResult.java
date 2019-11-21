package cn.micro.biz.pubsrv.hook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Outgoing Result
 *
 * @author lry
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutgoingResult implements Serializable {

    private boolean success;
    private String msg;
    private String plain;

}
