package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnMatchMatrix;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldViewModel;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldViewModelSet;
import org.activityinfo.ui.client.importer.viewModel.fields.MappedField;

import java.util.*;

public class MappedSourceViewModel {

    private SourceViewModel source;
    private FieldMappingSet fieldMappingSet;
    private ColumnMatchMatrix columnMatrix;
    private final List<MappedSourceColumn> columns = new ArrayList<>();
    private final Observable<ValidatedTable> validatedTable;
    private final List<FieldViewModel> missingRequiredFields = new ArrayList<>();

    public MappedSourceViewModel(FieldViewModelSet fields, ScoredSourceViewModel scoredViewModel, FieldMappingSet fieldMappingSet) {
        this.source = scoredViewModel.getSource();
        this.columnMatrix = scoredViewModel.getMatchMatrix();
        this.fieldMappingSet = fieldMappingSet;

        // Build the list of fields that are mapped to columns,
        // and keep track of the columns to which they are mapped

        Map<String, MappedSourceColumn> mappedColumns = new HashMap<>();
        Map<String, MappedField> mappedFields = new HashMap<>();

        for (FieldViewModel field : fields) {
            Optional<MappedField> mapping = field.mapColumns(source, fieldMappingSet);
            mapping.ifPresent(mappedField -> {
                mappedFields.put(field.getFieldName(), mappedField);
                for (MappedSourceColumn mappedColumn : mappedField.getMappedColumns()) {
                    mappedColumns.put(mappedColumn.getId(), mappedColumn);
                }
            });
        }

        // Are all required fields mapped?
        for (FieldViewModel field : fields) {
            if(!mappedFields.containsKey(field.getFieldName()) ||
                    !mappedFields.get(field.getFieldName()).isComplete()) {
                missingRequiredFields.add(field);
            }
        }

        // Now combine the mapped columns with unmapped and ignored columns
        for (SourceColumn sourceColumn : source.getColumns()) {
            if(fieldMappingSet.isColumnIgnored(sourceColumn.getId())) {
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
                       validatedColumns -> new ValidatedTable(fields, mappedFields, validatedColumns));



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

    public Observable<Optional<ValidatedColumn>> getColumnById(String columnId) {
        return validatedTable.transform(table -> table.getColumnById(columnId));
    }

    public boolean isComplete() {
        return missingRequiredFields.isEmpty();
    }
}
