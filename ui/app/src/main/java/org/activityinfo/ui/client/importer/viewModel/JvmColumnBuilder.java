package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.query.StringArrayColumnView;

import java.util.ArrayList;
import java.util.List;

public class JvmColumnBuilder implements ColumnBuilder {

    private List<String> values = new ArrayList<>();

    @Override
    public void add(String value) {
        values.add(value);
    }

    @Override
    public ColumnView build() {
        return new StringArrayColumnView(values);
    }
}
