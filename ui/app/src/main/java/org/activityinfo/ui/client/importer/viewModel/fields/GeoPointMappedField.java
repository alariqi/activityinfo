package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.form.FormField;
import org.activityinfo.observable.Observable;
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
    public Observable<FieldImporter> getImporter() {
        if(!latitudeColumn.isPresent() || !longitudeColumn.isPresent()) {
            throw new IllegalStateException();
        }
        return Observable.just(new GeoPointImporter(field.getName(), latitudeColumn.get(), longitudeColumn.get()));
    }

    @Override
    public FormField getField() {
        return field;
    }

    @Override
    public List<MappedSourceColumn> getMappedColumns() {
        throw new UnsupportedOperationException("TODO");
    }
}


