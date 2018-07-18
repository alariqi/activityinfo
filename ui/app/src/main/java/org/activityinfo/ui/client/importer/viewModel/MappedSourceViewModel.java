package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnMatchMatrix;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldViewModel;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldViewModelSet;
import org.activityinfo.ui.client.importer.viewModel.fields.MappedField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappedSourceViewModel {

    private SourceViewModel source;
    private FieldMappingSet fieldMappingSet;
    private ColumnMatchMatrix columnMatrix;
    private final List<MappedField> mappedFields = new ArrayList<>();
    private final List<MappedSourceColumn> columns = new ArrayList<>();
    private final Observable<ValidatedTable> validatedTable;

    public MappedSourceViewModel(FieldViewModelSet fields, ScoredSourceViewModel scoredViewModel, FieldMappingSet fieldMappingSet) {
        this.source = scoredViewModel.getViewModel();
        this.columnMatrix = scoredViewModel.getMatchMatrix();
        this.fieldMappingSet = fieldMappingSet;

        // Build the list of fields that are mapped to columns,
        // and keep track of the columns to which they are mapped

        Map<String, MappedSourceColumn> mappedColumns = new HashMap<>();

        for (FieldViewModel field : fields) {
            field.mapColumns(source, fieldMappingSet).ifPresent(mappedField -> {
                mappedFields.add(mappedField);
                for (MappedSourceColumn mappedColumn : mappedField.getMappedColumns()) {
                    mappedColumns.put(mappedColumn.getId(), mappedColumn);
                }
            });
        }

        // Now combine the mapped columns with unmapped and ignored columns
        for (SourceColumn sourceColumn : source.getColumns()) {
            if(fieldMappingSet.isIgnored(sourceColumn.getId())) {
                columns.add(new MappedSourceColumn(sourceColumn, ColumnStatus.IGNORED));

            } else if(mappedColumns.containsKey(sourceColumn.getId())) {
                columns.add(mappedColumns.get(sourceColumn.getId()));

            } else {
                columns.add(new MappedSourceColumn(sourceColumn, ColumnStatus.UNSET));
            }
        }

        // Finally...
        validatedTable = Observable.flatJoin(columns,
                column -> column.getValidation().transform(
                    validation -> new ValidatedColumn(column, validation))).transform(
                       validatedColumns -> new ValidatedTable(validatedColumns));
    }

    public SourceViewModel getSource() {
        return source;
    }

    public FieldMappingSet getFieldMappingSet() {
        return fieldMappingSet;
    }

    public ColumnMatchMatrix getColumnMatrix() {
        return columnMatrix;
    }

    public List<MappedSourceColumn> getColumns() {
        return columns;
    }

    public int getRowCount() {
        return source.getRowCount();
    }

    public Observable<ValidatedTable> getValidatedTable() {
        return validatedTable;
    }
}
