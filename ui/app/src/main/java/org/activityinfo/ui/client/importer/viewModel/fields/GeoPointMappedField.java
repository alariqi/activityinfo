package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

import java.util.List;
import java.util.Optional;

public class GeoPointMappedField implements MappedField {

    public GeoPointMappedField(FormField field, Optional<SourceColumn> latitudeColumn, Optional<SourceColumn> longitudeColumn) {
    }

    @Override
    public List<MappedSourceColumn> getMappedColumns() {
        throw new UnsupportedOperationException("TODO");
    }
}


