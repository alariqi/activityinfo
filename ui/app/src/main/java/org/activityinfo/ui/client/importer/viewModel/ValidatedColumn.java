package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.model.query.ColumnView;

public class ValidatedColumn {
    private final MappedSourceColumn column;
    private Validation validation;

    public ValidatedColumn(MappedSourceColumn column, Validation validation) {
        this.column = column;
        this.validation = validation;
    }

    public MappedSourceColumn getColumn() {
        return column;
    }

    public ColumnStatus getStatus() {
        return column.getStatus();
    }

    public Validation getValidation() {
        return validation;
    }

    public String getId() {
        return column.getId();
    }

    public ColumnView getColumnView() {
        return getColumn().getSource().getColumnView();
    }
}
