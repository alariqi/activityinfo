package org.activityinfo.observable;

public interface IncrementalTask<T> {

    /**
     * Executes some part of the work to be done.
     *
     * @return {@code true} if there is more work to be done.
     */
    boolean execute();


    /**
     *
     * @return true if the task has a value that can be published. (the task need not be complete)
     */
    boolean isLoading();

    /**
     *
     * @return the value under construction.
     */
    T getValue();
}
