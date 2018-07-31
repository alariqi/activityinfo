package org.activityinfo.observable;

@FunctionalInterface
public interface CachePredicate<T> {

    boolean isSame(T oldValue, T newValue);
}
