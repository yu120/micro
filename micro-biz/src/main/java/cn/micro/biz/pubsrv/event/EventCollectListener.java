package cn.micro.biz.pubsrv.event;

import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Event Collect Listener
 *
 * @author lry
 */
@Slf4j
@Component
public class EventCollectListener {

    @Subscribe
    public void notify(EventCollect eventCollect) {
        log.info("{}", eventCollect);
    }

}
