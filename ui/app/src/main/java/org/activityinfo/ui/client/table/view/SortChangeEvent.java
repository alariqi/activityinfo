package org.activityinfo.ui.client.table.view;

import com.google.common.base.Optional;
import com.google.gwt.event.shared.GwtEvent;
import org.activityinfo.model.query.SortDir;

public class SortChangeEvent extends GwtEvent<SortChangeHandler> {

    private static Type<SortChangeHandler> TYPE;
    private Optional<String> field;
    private Optional<SortDir> dir;

    public SortChangeEvent() {
        this.field = Optional.absent();
        this.dir = Optional.absent();
    }

    public SortChangeEvent(String field, SortDir dir) {
        this.field = Optional.of(field);
        this.dir = Optional.of(dir);
    }

    public static Type<SortChangeHandler> getType() {
        if (TYPE == null) {
            TYPE = new Type<>();
        }
        return TYPE;
    }

    @Override
    public Type<SortChangeHandler> getAssociatedType() {
        return getType();
    }

    public Optional<String> getField() {
        return field;
    }

    public Optional<SortDir> getDir() {
        return dir;
    }

    @Override
    protected void dispatch(SortChangeHandler handler) {
        handler.onSortChanged(this);
    }
}
