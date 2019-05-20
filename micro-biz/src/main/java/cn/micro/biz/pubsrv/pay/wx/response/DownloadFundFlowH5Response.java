package cn.micro.biz.pubsrv.pay.wx.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

/**
 * 下载资金账单H5响应模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_18&index=7}
 *
 * @author lry
 */
@Data
@ToString
public class DownloadFundFlowH5Response extends WXPayResponse {

    /**
     * 业务结果
     */
    @JSONField(name = "result_code")
    private String resultCode;
    /**
     * 错误代码
     */
    @JSONField(name = "err_code")
    private String errCode;
    /**
     * 错误代码描述
     */
    @JSONField(name = "err_code_des")
    private String errCodeDes;

}
