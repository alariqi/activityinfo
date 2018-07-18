package org.activityinfo.observable;

import com.google.gwt.core.client.GWT;

public final class EventLoopScheduler implements Scheduler {

    public static final EventLoopScheduler SCHEDULER = new EventLoopScheduler();

    private EventLoopScheduler() {}

    @Override
    public void schedule(Runnable runnable) {
        if(GWT.isScript()) {
            com.google.gwt.core.client.Scheduler.get().scheduleFinally(() -> runnable.run());
        } else {
            runnable.run();
        }
    }
}
