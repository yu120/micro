package cn.micro.biz.pubsrv.pay.wx.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 下载对账单H5请求模型
 * <p>
 * 1、微信在次日9点启动生成前一天的对账单，建议商户10点后再获取
 * 2、对账单中涉及金额的字段单位为“元”
 * 3、对账单接口只能下载三个月以内的账单
 * <p>
 * {@see https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_6&index=6}
 *
 * @author lry
 */
@Data
@ToString
public class DownloadBillH5Request implements Serializable {

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
     * 对账单日期
     */
    @JSONField(name = "bill_date")
    private String billDate;
    /**
     * 账单类型
     */
    @JSONField(name = "bill_type")
    private String billType;
    /**
     * 压缩账单
     */
    @JSONField(name = "tar_type")
    private String tarType;

}
