package org.activityinfo.io.csv;

import org.activityinfo.model.query.ColumnView;

public interface ColumnBuilder {
    void add(String value);
    ColumnView build();
}
