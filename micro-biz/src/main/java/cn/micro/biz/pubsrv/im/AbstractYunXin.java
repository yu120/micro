package cn.micro.biz.pubsrv.im;

import cn.micro.biz.commons.exception.MicroErrorException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract YunXin Client
 *
 * @author lry
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractYunXin {

    private static final int SUCCESS_STATUS_CODE = 200;
    private static final String RESPONSE_CODE = "code";
    private static final String RESPONSE_DESC = "desc";

    private static final String NONCE_KEY = "Nonce";
    private static final String APP_KEY_KEY = "AppKey";
    private static final String CUR_TIME_KEY = "CurTime";
    private static final String CHECK_SUM_KEY = "CheckSum";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";
    @Setter
    @Resource
    private YunXinProperties yunXinProperties;

    /**
     * The send request
     *
     * @param yunXinActionEnum {@link YunXinActionEnum}
     * @param obj              object
     * @return response body json
     */
    protected JSONObject request(YunXinActionEnum yunXinActionEnum, Object obj) {
        return this.request(yunXinActionEnum, JSON.parseObject(JSON.toJSONString(obj),
                new TypeReference<Map<String, Object>>() {
                }));
    }

    /**
     * The send request
     *
     * @param yunXinActionEnum {@link YunXinActionEnum}
     * @param args             key1,,value1,......
     * @return response body json
     */
    protected JSONObject request(YunXinActionEnum yunXinActionEnum, String... args) {
        Map<String, String> parameters = new HashMap<>();
        for (int i = 0; i < args.length; i += 2) {
            parameters.put(args[i], args[i + 1]);
        }

        return this.request(yunXinActionEnum, parameters);
    }

    /**
     * The send request
     *
     * @param yunXinActionEnum {@link YunXinActionEnum}
     * @param parameters       key1=value1,......
     * @return response body json
     */
    protected JSONObject request(YunXinActionEnum yunXinActionEnum, Map<String, Object> parameters) {
        if (!yunXinProperties.isEnable()) {
            throw new MicroErrorException("YunXin IM is disable");
        }

        String url = yunXinProperties.getUri() + yunXinActionEnum.getValue();
        String nonce = UUID.randomUUID().toString();
        String appKey = yunXinProperties.getAppKey();
        String curTime = String.valueOf(System.currentTimeMillis() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(yunXinProperties.getAppSecret(), nonce, curTime);

        Connection.Response response;
        try {
            Connection connection = HttpConnection.connect(url).ignoreContentType(true);
            Connection.Request request = connection.request();
            // setter post request header
            request.header(NONCE_KEY, nonce);
            request.header(APP_KEY_KEY, appKey);
            request.header(CUR_TIME_KEY, curTime);
            request.header(CHECK_SUM_KEY, checkSum);
            request.header(CONTENT_TYPE_KEY, CONTENT_TYPE);
            // setter post request body
            request.postDataCharset(StandardCharsets.UTF_8.name());
            if (parameters != null && !parameters.isEmpty()) {
                for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                    if (!this.isPrimitive(entry.getValue())) {
                        throw new IllegalArgumentException("不能包含常用数据类型");
                    }
                    String value = URLEncoder.encode(String.valueOf(entry.getValue()),
                            StandardCharsets.UTF_8.name());
                    request.data(HttpConnection.KeyVal.create(entry.getKey(), value));
                }
            }
            // setter post request method
            request.method(Connection.Method.POST);
            log.debug("YunXin request url:[{}], method:[{}], headers:[{}], body:[{}]",
                    url, request.method(), request.headers(), JSON.toJSONString(request.data()));
            response = connection.execute();
        } catch (Exception e) {
            throw new MicroErrorException(e.getMessage(), e);
        }
        if (SUCCESS_STATUS_CODE != response.statusCode()) {
            throw new MicroErrorException("IM网络错误, code:" +
                    response.statusCode() + ", message:" + response.statusMessage());
        }

        String responseBody = response.charset(StandardCharsets.UTF_8.name()).body();
        log.debug("YunXin response body:{}", responseBody);
        JSONObject jsonObject = JSON.parseObject(responseBody);
        if (jsonObject == null || !jsonObject.containsKey(RESPONSE_CODE)) {
            log.warn("非法响应报文:{}", responseBody);
            throw new MicroErrorException("非法响应报文");
        }

        int code = jsonObject.getInteger(RESPONSE_CODE);
        if (SUCCESS_STATUS_CODE != code) {
            log.warn("IM业务失败:{}", responseBody);
            this.error(code, jsonObject.getString(RESPONSE_DESC));
        }

        return jsonObject;
    }

    /**
     * 判断是否为常规类型
     *
     * @param obj obj
     * @return true表示为常规对象
     */
    private boolean isPrimitive(Object obj) {
        return obj instanceof String ||
                obj instanceof Number ||
                obj instanceof Boolean ||
                obj.getClass().isPrimitive();
    }

    /**
     * 服务器端状态码
     *
     * @param code code
     * @param desc desc
     */
    private void error(int code, String desc) {
        switch (code) {
            case 301:
                throw new MicroErrorException("被封禁");
            case 302:
                throw new MicroErrorException("用户名或密码错误");
            case 315:
                throw new MicroErrorException("IP限制");
            case 403:
                if (desc.contains("forbbiden info")) {
                    throw new MicroErrorException("不能使用敏感词信息");
                } else {
                    throw new MicroErrorException("非法操作或没有权限");
                }
            case 404:
                throw new MicroErrorException("对象不存在");
            case 405:
                throw new MicroErrorException("参数长度过长");
            case 406:
                throw new MicroErrorException("对象只读");
            case 408:
                throw new MicroErrorException("客户端请求超时");
            case 413:
                throw new MicroErrorException("验证失败(短信服务)");
            case 414:
                if (desc.contains("already register")) {
                    throw new MicroErrorException("已注册过");
                } else {
                    throw new MicroErrorException("参数错误");
                }
            case 415:
                throw new MicroErrorException("客户端网络问题");
            case 416:
                if (desc.contains("query too fast")) {
                    // 会被限制5分钟不能使用
                    throw new MicroErrorException("服务器繁忙，请稍后再试");
                } else {
                    throw new MicroErrorException("频率控制");
                }
            case 417:
                throw new MicroErrorException("重复操作");
            case 418:
                throw new MicroErrorException("通道不可用(短信服务)");
            case 419:
                throw new MicroErrorException("数量超过上限");
            case 422:
                throw new MicroErrorException("账号被禁用");
            case 431:
                throw new MicroErrorException("HTTP重复请求");
            case 500:
                throw new MicroErrorException("服务器内部错误");
            case 503:
                throw new MicroErrorException("服务器繁忙");
            case 508:
                throw new MicroErrorException("消息撤回时间超限");
            case 509:
                throw new MicroErrorException("无效协议");
            case 514:
                throw new MicroErrorException("服务不可用");
            case 998:
                throw new MicroErrorException("解包错误");
            case 999:
                throw new MicroErrorException("打包错误");
            default:
                this.errorTeam(code, desc);
        }
    }

    /**
     * 群相关错误码
     *
     * @param code code
     * @param desc desc
     */
    private void errorTeam(int code, String desc) {
        switch (code) {
            case 801:
                throw new MicroErrorException("群人数达到上限");
            case 802:
                throw new MicroErrorException("没有权限");
            case 803:
                throw new MicroErrorException("群不存在");
            case 804:
                throw new MicroErrorException("用户不在群");
            case 805:
                throw new MicroErrorException("群类型不匹配");
            case 806:
                throw new MicroErrorException("创建群数量达到限制");
            case 807:
                throw new MicroErrorException("群成员状态错误");
            case 808:
                throw new MicroErrorException("申请成功");
            case 809:
                throw new MicroErrorException("已经在群内");
            case 810:
                throw new MicroErrorException("邀请成功");
            default:
                this.errorVideo(code, desc);
        }
    }

    /**
     * 音视频通话/白板相关错误码
     *
     * @param code code
     * @param desc desc
     */
    private void errorVideo(int code, String desc) {
        switch (code) {
            case 9102:
                throw new MicroErrorException("通道失效");
            case 9103:
                throw new MicroErrorException("已经在他端对这个呼叫响应过了");
            case 11001:
                throw new MicroErrorException("通话不可达，对方离线状态");
            default:
                this.errorCharRoom(code, desc);
        }
    }

    /**
     * 聊天室相关错误码
     *
     * @param code code
     * @param desc desc
     */
    private void errorCharRoom(int code, String desc) {
        switch (code) {
            case 13001:
                throw new MicroErrorException("IM主连接状态异常");
            case 13002:
                throw new MicroErrorException("聊天室状态异常");
            case 13003:
                throw new MicroErrorException("账号在黑名单中,不允许进入聊天室");
            case 13004:
                throw new MicroErrorException("在禁言列表中,不允许发言");
            case 13005:
                throw new MicroErrorException("用户的聊天室昵称、头像或成员扩展字段被反垃圾");
            default:
                this.errorOther(code, desc);
        }
    }

    /**
     * 特定业务相关错误码
     *
     * @param code code
     * @param desc desc
     */
    private void errorOther(int code, String desc) {
        switch (code) {
            case 10431:
                throw new MicroErrorException("输入email不是邮箱");
            case 10432:
                throw new MicroErrorException("输入mobile不是手机号码");
            case 10433:
                throw new MicroErrorException("注册输入的两次密码不相同");
            case 10434:
                throw new MicroErrorException("企业不存在");
            case 10435:
                throw new MicroErrorException("登陆密码或账号不对");
            case 10436:
                throw new MicroErrorException("app不存在");
            case 10437:
                throw new MicroErrorException("email已注册");
            case 10438:
                throw new MicroErrorException("手机号已注册");
            case 10441:
                throw new MicroErrorException("app名字已经存在");
            default:
                throw new MicroErrorException("未知错误");
        }
    }

}
