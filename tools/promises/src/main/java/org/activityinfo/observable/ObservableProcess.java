package org.activityinfo.observable;

public class ObservableProcess<T> extends Observable<T> {


    private T result;


    @Override
    public boolean isLoading() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public T get() {
        throw new UnsupportedOperationException("TODO");
    }
}
