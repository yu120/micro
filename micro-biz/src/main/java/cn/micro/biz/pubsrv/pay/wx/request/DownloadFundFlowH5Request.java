package cn.micro.biz.pubsrv.pay.wx.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 下载资金账单H5请求模型
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_18&index=7}
 *
 * @author lry
 */
@Data
@ToString
public class DownloadFundFlowH5Request implements Serializable {

    /**
     * 公众账号ID
     */
    @JSONField(name = "appid")
    private String appId;
    /**
     * 商户号
     */
    @JSONField(name = "mch_id")
    private String mchId;
    /**
     * 随机字符串
     */
    @JSONField(name = "nonce_str")
    private String nonceStr;
    /**
     * 签名
     */
    @JSONField(name = "sign")
    private String sign;
    /**
     * 签名类型
     */
    @JSONField(name = "sign_type")
    private String signType;
    /**
     * 资金账单日期
     */
    @JSONField(name = "bill_date")
    private String billDate;
    /**
     * 资金账户类型
     */
    @JSONField(name = "account_type")
    private String accountType;
    /**
     * 压缩账单
     */
    @JSONField(name = "tar_type")
    private String tarType;

}
