package cn.micro.biz.pubsrv.event.spring;

import cn.micro.biz.pubsrv.event.EventCollect;
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
public class MicroEventCollectListener {

    @Subscribe
    public void notify(EventCollect eventCollect) {
        log.info("{}", eventCollect);
    }

}
