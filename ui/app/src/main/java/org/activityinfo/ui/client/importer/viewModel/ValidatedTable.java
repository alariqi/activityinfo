package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.model.query.ColumnView;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldImporter;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldViewModelSet;
import org.activityinfo.ui.client.importer.viewModel.fields.MappedField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ValidatedTable {

    private final int numColumns;
    private final int numRows;
    private final ColumnView[] columnViews;
    private final String[] columnIds;
    private final Validation[] validation;
    private final FieldViewModelSet fields;
    private final Map<String, MappedField> mappedFields;
    private final List<ValidatedColumn> columns;

    private Observable<ValidRowSet> validRows;
    private Observable<ImportedTable> importedTable;

    public ValidatedTable(FieldViewModelSet fields, Map<String, MappedField> mappedFields, List<ValidatedColumn> columns) {
        this.fields = fields;
        this.mappedFields = mappedFields;
        this.columns = columns;
        numColumns = columns.size();
        numRows = columns.isEmpty() ? 0 : columns.get(0).getColumn().getSource().getColumnView().numRows();

        columnViews = new ColumnView[numColumns];
        columnIds = new String[numColumns];
        validation = new Validation[numColumns];

        boolean validationDone = true;

        for (int i = 0; i < numColumns; i++) {
            SourceColumn sourceColumn = columns.get(i).getColumn().getSource();
            columnViews[i] = sourceColumn.getColumnView();
            columnIds[i] = sourceColumn.getId();
            validation[i] = columns.get(i).getValidation();
            if(!validation[i].isDone()) {
                validationDone = false;
            }
        }

        if(validationDone) {
            ValidRowSet validRowSet = new ValidRowSet(numRows, validation);
            validRows = Observable.just(validRowSet);

            List<Observable<FieldImporter>> importers = new ArrayList<>();
            for (MappedField mappedField : mappedFields.values()) {
                importers.add(mappedField.getImporter());
            }

            importedTable = Observable.flatten(importers).transform(
                    list -> new ImportedTable(fields.getFormId(), validRowSet, list));


        } else {
            validRows = Observable.loading();
        }
    }

    public List<ValidatedColumn> getColumns() {
        return columns;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getNumRows() {
        return numRows;
    }

    public ColumnView[] getColumnViews() {
        return columnViews;
    }

    public String[] getColumnIds() {
        return columnIds;
    }

    public Validation[] getValidationArray() {
        return validation;
    }

    public Observable<ValidRowSet> getValidRows() {
        return validRows;
    }

    public Optional<ValidatedColumn> getColumnById(String columnId) {
        return columns.stream().filter(c -> c.getId().equals(columnId)).findAny();
    }

    public Observable<ImportedTable> getImportedTable() {
        return importedTable;
    }
}
