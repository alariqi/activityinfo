package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;

import java.util.List;

public class MappedReferenceField implements MappedField {

    private final List<MappedSourceColumn> mappedColumns;

    public MappedReferenceField(List<MappedSourceColumn> mappedColumns) {

        this.mappedColumns = mappedColumns;
    }

    @Override
    public List<MappedSourceColumn> getMappedColumns() {
        return mappedColumns;
    }

    @Override
    public boolean isComplete() {
        return true;
    }
}
