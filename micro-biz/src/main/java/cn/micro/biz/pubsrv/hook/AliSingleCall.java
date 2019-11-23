package cn.micro.biz.pubsrv.hook;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsRequest;
import com.aliyuncs.dyvmsapi.model.v20170525.SingleCallByTtsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class AliSingleCall {

    public static void main(String[] args) throws Exception {
        //设置访问超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //云通信产品-语音API服务产品名称（产品名固定，无需修改）
        final String product = "Dyvmsapi";
        //产品域名（接口地址固定，无需修改）
        final String domain = "dyvmsapi.aliyuncs.com";
        //AK信息
        final String accessKeyId = "yourAccessKeyId";
        final String accessKeySecret = "yourAccessKeySecret";
        //初始化acsClient 暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        SingleCallByTtsRequest request = new SingleCallByTtsRequest();
        //必填-被叫显号,可在语音控制台中找到所购买的显号
        request.setCalledShowNumber("02560000000");
        //必填-被叫号码
        request.setCalledNumber("1500000000");
        //必填-Tts模板ID
        request.setTtsCode("TTS_000000");
        //可选-当模板中存在变量时需要设置此值
        request.setTtsParam("{\"code\":\"123456\"}");
        //可选-音量 取值范围 0--200
        request.setVolume(100);
        //可选-播放次数
        request.setPlayTimes(3);
        //可选-外部扩展字段,此ID将在回执消息中带回给调用方
        request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SingleCallByTtsResponse singleCallByTtsResponse = acsClient.getAcsResponse(request);
        if (singleCallByTtsResponse.getCode() != null && singleCallByTtsResponse.getCode().equals("OK")) {
            //请求成功
            System.out.println("语音文本外呼---------------");
            System.out.println("RequestId=" + singleCallByTtsResponse.getRequestId());
            System.out.println("Code=" + singleCallByTtsResponse.getCode());
            System.out.println("Message=" + singleCallByTtsResponse.getMessage());
            System.out.println("CallId=" + singleCallByTtsResponse.getCallId());
        }
    }

}
