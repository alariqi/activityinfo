package org.activityinfo.observable;

public class ExplicitlyOptimisticObservable<T> extends Observable<MaybeStale<T>> {

    private final Observable<T> observable;
    private Subscription subscription;
    private MaybeStale<T> cachedValue;

    public ExplicitlyOptimisticObservable(Observable<T> observable) {
        this.observable = observable;
    }

    @Override
    public boolean isLoading() {
        return cachedValue == null && observable.isLoading();
    }

    @Override
    public MaybeStale<T> get() {
        assert cachedValue != null : "not loaded";
        return cachedValue;
    }

    @Override
    protected void onConnect() {
        super.onConnect();
        subscription = observable.subscribe(new Observer<T>() {
            @Override
            public void onChange(Observable<T> observable) {
                maybeUpdateValue(observable);
            }
        });
    }

    @Override
    protected void onDisconnect() {
        super.onDisconnect();
        subscription.unsubscribe();
        subscription = null;
    }

    private void maybeUpdateValue(Observable<T> observable) {
        if(observable.isLoaded()) {
            cachedValue = new MaybeStale<>(observable.get(), false);
            fireChange();

        } else if(cachedValue != null) {
            cachedValue = cachedValue.outdated();
            fireChange();
        }
    }
}
