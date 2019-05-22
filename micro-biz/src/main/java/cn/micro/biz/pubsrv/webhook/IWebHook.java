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
    boolean incoming(T robotSendRequest);

}
