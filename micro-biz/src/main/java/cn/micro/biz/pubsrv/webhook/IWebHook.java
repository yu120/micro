package cn.micro.biz.pubsrv.webhook;

import org.jsoup.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Web Hook
 *
 * @author lry
 */
public interface IWebHook<T extends IRobotSendRequest> {

    int SUCCESS_STATUS_CODE = 200;
    Logger log = LoggerFactory.getLogger(IWebHook.class);

    /**
     * Check network code
     *
     * @param response {@link Connection.Response}
     * @return ok return true
     */
    default boolean checkCode(Connection.Response response) {
        if (SUCCESS_STATUS_CODE != response.statusCode()) {
            log.warn("Network error:[code:{},message:{}]", response.statusCode(), response.statusMessage());
            return false;
        }

        return true;
    }

    /**
     * Send push to 3th
     *
     * @param robotSendRequest {@link IRobotSendRequest}
     * @return success true
     */
    boolean push(T robotSendRequest);

    // boolean hook(T robotSendRequest);

}
