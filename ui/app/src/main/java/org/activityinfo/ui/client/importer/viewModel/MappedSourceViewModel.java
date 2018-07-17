package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnMatchMatrix;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldViewModelSet;

import java.util.ArrayList;
import java.util.List;

public class MappedSourceViewModel {

    private SourceViewModel source;
    private FieldMappingSet fieldMappingSet;
    private ColumnMatchMatrix columnMatrix;
    private final List<MappedSourceColumn> columns;

    public MappedSourceViewModel(FieldViewModelSet fields, ScoredSourceViewModel scoredViewModel, FieldMappingSet fieldMappingSet) {
        this.source = scoredViewModel.getViewModel();
        this.columnMatrix = scoredViewModel.getMatchMatrix();
        this.fieldMappingSet = fieldMappingSet;

        columns = new ArrayList<>();
        for (SourceColumn sourceColumn : source.getColumns()) {
            if(fieldMappingSet.isIgnored(sourceColumn.getId())) {
                columns.add(new MappedSourceColumn(sourceColumn, MappedSourceColumn.Status.IGNORED));
            } else {
                columns.add(fieldMappingSet.getColumnMapping(sourceColumn.getId())
                        .flatMap(fieldMapping -> fields.columnMappingLabel(sourceColumn.getId(), fieldMapping))
                        .map(label -> new MappedSourceColumn(sourceColumn, label))
                        .orElse(new MappedSourceColumn(sourceColumn, MappedSourceColumn.Status.UNSET)));
            }
        }
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


}
