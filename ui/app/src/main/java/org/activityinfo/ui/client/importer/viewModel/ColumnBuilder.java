package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.model.query.ColumnView;

public interface ColumnBuilder {
    void add(String value);
    ColumnView build();
}
