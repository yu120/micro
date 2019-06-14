package org.micro.thread;

/**
 * NamedInternalThreadFactory
 * <p>
 * This is a threadFactory which produce {@link InternalThread}
 *
 * @author lry
 */
public class NamedInternalThreadFactory extends NamedThreadFactory {

    public NamedInternalThreadFactory() {
        super();
    }

    public NamedInternalThreadFactory(String prefix) {
        super(prefix, false);
    }

    public NamedInternalThreadFactory(String prefix, boolean daemon) {
        super(prefix, daemon);
    }

    @Override
    public Thread newThread(Runnable runnable) {
        String name = mPrefix + mThreadNum.getAndIncrement();
        InternalThread ret = new InternalThread(group, runnable, name, 0);
        ret.setDaemon(daemon);
        return ret;
    }

}
