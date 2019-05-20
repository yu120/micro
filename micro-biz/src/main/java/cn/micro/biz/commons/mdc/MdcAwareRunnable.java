package cn.micro.biz.commons.mdc;

import org.slf4j.MDC;

import java.util.Map;

/**
 * MDC Runnable
 *
 * @author lry
 */
public abstract class MdcAwareRunnable implements Runnable {

    private Map<String, String> context = MDC.getCopyOfContextMap();

    @Override
    public final void run() {
        try {
            MDC.setContextMap(context);
            this.execute();
        } finally {
            MDC.clear();
        }
    }

    /**
     * Run Task
     */
    protected abstract void execute();

}
