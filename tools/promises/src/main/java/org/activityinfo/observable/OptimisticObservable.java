package org.activityinfo.observable;

class OptimisticObservable<T> extends Observable<T> {

    private final Observable<T> observable;
    private Subscription subscription;
    private T cachedValue;

    OptimisticObservable(Observable<T> observable, T defaultValue) {
        this.observable = observable;
        this.cachedValue = defaultValue;
    }

    @Override
    public boolean isLoading() {
        return cachedValue == null;
    }

    @Override
    public T get() {
        assert cachedValue != null : "observable is not loaded";
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
            T newValue = observable.get();
            if(newValue != cachedValue) {
                cachedValue = newValue;
                fireChange();
            }

        } else if(cachedValue == null) {
            fireChange();
        }
    }
}
