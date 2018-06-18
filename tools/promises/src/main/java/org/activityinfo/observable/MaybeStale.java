package org.activityinfo.observable;

import java.util.Objects;

/**
 * Wrapper for a value that <i>may</i> be out of date.
 */
public class MaybeStale<T> {
    private final boolean stale;
    private final T value;

    MaybeStale(T value, boolean stale) {
        this.stale = stale;
        this.value = value;
    }

    public boolean isStale() {
        return stale;
    }

    public T getValue() {
        return value;
    }

    MaybeStale<T> outdated() {
        if(stale) {
            return this;
        } else {
            return new MaybeStale<>(value, true);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaybeStale<?> maybeStale1 = (MaybeStale<?>) o;
        return stale == maybeStale1.stale &&
                Objects.equals(value, maybeStale1.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stale, value);
    }
}
