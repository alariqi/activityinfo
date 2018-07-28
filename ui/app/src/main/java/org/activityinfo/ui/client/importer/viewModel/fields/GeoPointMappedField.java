package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.io.match.coord.CoordinateAxis;
import org.activityinfo.model.form.FormField;
import org.activityinfo.observable.EventLoopScheduler;
import org.activityinfo.observable.IncrementalObservable;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.parser.QuantityParser;
import org.activityinfo.ui.client.importer.viewModel.parser.SimpleValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeoPointMappedField implements MappedField {

    private final FormField field;
    private final Optional<SourceColumn> latitudeColumn;
    private final Optional<SourceColumn> longitudeColumn;
    private final List<MappedSourceColumn> mappedColumns;

    public GeoPointMappedField(FormField field, Optional<SourceColumn> latitudeColumn, Optional<SourceColumn> longitudeColumn) {
        this.field = field;
        this.latitudeColumn = latitudeColumn;
        this.longitudeColumn = longitudeColumn;
        this.mappedColumns = new ArrayList<>();
        latitudeColumn.ifPresent(column -> {
            mappedColumns.add(mappedColumn(field, column, CoordinateAxis.LATITUDE));
        });
        longitudeColumn.ifPresent(column -> {
            mappedColumns.add(mappedColumn(field, column, CoordinateAxis.LONGITUDE));
        });
    }

    private MappedSourceColumn mappedColumn(FormField field, SourceColumn lat, CoordinateAxis axis) {
        return new MappedSourceColumn(lat, CoordTarget.label(field, axis),
                new IncrementalObservable<>(new SimpleValidator(lat.getColumnView(), new QuantityParser()),
                        EventLoopScheduler.SCHEDULER));
    }

    @Override
    public boolean isComplete() {
        return latitudeColumn.isPresent() && longitudeColumn.isPresent();
    }

    @Override
    public Observable<FieldImporter> getImporter() {
        if(!latitudeColumn.isPresent() || !longitudeColumn.isPresent()) {
            return Observable.just(new NullImporter(field));
        } else {
            return Observable.just(new GeoPointImporter(field.getName(), latitudeColumn.get(), longitudeColumn.get()));
        }
    }

    @Override
    public FormField getField() {
        return field;
    }

    @Override
    public List<MappedSourceColumn> getMappedColumns() {
        return mappedColumns;
    }
}


