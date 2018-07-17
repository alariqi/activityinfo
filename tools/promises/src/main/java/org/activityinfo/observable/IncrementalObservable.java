package org.activityinfo.observable;

public class IncrementalObservable<T> extends Observable<T> {

    private IncrementalTask<T> task;
    private final Scheduler scheduler;
    private boolean connected = false;
    private boolean completed = false;

    public IncrementalObservable(IncrementalTask<T> task, Scheduler scheduler) {
        this.task = task;
        this.scheduler = scheduler;
    }

    @Override
    public boolean isLoading() {
        return task.isLoading();
    }

    @Override
    public T get() {
        return task.getValue();
    }

    @Override
    protected void onConnect() {
        super.onConnect();
        connected = true;
        if(!completed) {
            scheduleNextSlice();
        }
    }

    @Override
    protected void onDisconnect() {
        super.onDisconnect();
        connected = true;
    }

    private void scheduleNextSlice() {
        scheduler.schedule(() -> {
            boolean moreWork = task.execute();

            if(!task.isLoading()) {
                fireChange();
            }

            if(!moreWork) {
                completed = true;
            } else {
                if(connected) {
                    scheduleNextSlice();
                }
            }
        });
    }
}
