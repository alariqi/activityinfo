package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import java.util.List;
import java.util.Optional;

public class GeoPointMappedField implements MappedField {

    private final FormField field;
    private final Optional<SourceColumn> latitudeColumn;
    private final Optional<SourceColumn> longitudeColumn;

    public GeoPointMappedField(FormField field, Optional<SourceColumn> latitudeColumn, Optional<SourceColumn> longitudeColumn) {
        this.field = field;
        this.latitudeColumn = latitudeColumn;
        this.longitudeColumn = longitudeColumn;
    }

    @Override
    public boolean isComplete() {
        return latitudeColumn.isPresent() && longitudeColumn.isPresent();
    }

    @Override
    public List<MappedSourceColumn> getMappedColumns() {
        throw new UnsupportedOperationException("TODO");
    }
}


