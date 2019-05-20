package cn.micro.biz.pubsrv.pay.wx.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * 下载对账单H5响应模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_6&index=6}
 *
 * @author lry
 */
@Data
@ToString
public class DownloadBillH5Response extends WXPayResponse {

    /**
     * 错误码
     */
    @JSONField(name = "error_code")
    private String errorCode;

}
