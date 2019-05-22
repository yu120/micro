package cn.micro.biz.pubsrv.webhook;

/**
 * Web Hook
 *
 * @author lry
 */
public interface IWebHook<T extends IRobotSendRequest> {

    /**
     * Send push to 3th
     *
     * @param robotSendRequest {@link IRobotSendRequest}
     * @return success true
     */
    boolean push(T robotSendRequest);

    // boolean hook(T robotSendRequest);

}
