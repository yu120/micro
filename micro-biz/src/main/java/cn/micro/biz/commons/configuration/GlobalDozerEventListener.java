package cn.micro.biz.commons.configuration;


import com.github.dozermapper.core.events.Event;
import com.github.dozermapper.core.events.EventListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GlobalDozerEventListener implements EventListener {

    @Override
    public void onMappingFinished(Event event) {
        log.info("onMappingFinished");
    }

    @Override
    public void onMappingStarted(Event event) {
        log.info("onMappingStarted");
    }

    @Override
    public void onPostWritingDestinationValue(Event event) {
        log.info("onPostWritingDestinationValue");
    }

    @Override
    public void onPreWritingDestinationValue(Event event) {
        log.info("onPreWritingDestinationValue");
    }

}
