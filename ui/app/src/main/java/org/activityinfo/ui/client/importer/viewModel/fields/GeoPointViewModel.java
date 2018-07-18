package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.io.match.coord.CoordinateAxis;
import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.SourceViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class GeoPointViewModel extends FieldViewModel {

    private final FormField field;
    private final CoordTarget latitudeTarget;
    private final CoordTarget longitudeTarget;

    public GeoPointViewModel(FormField field) {
        super(field);
        this.field = field;
        this.latitudeTarget = new CoordTarget(field, CoordinateAxis.LATITUDE);
        this.longitudeTarget = new CoordTarget(field, CoordinateAxis.LONGITUDE);
    }

    @Override
    public List<ColumnTarget> getTargets() {
        return Arrays.asList(latitudeTarget, longitudeTarget);
    }

    @Override
    public Optional<MappedField> mapColumns(SourceViewModel source, FieldMappingSet fieldMappingSet) {

        Optional<SourceColumn> latitudeColumn = fieldMappingSet
                .getMappedColumn(field.getName(), CoordinateAxis.LATITUDE.name())
                .flatMap(id -> source.getColumnById(id));

        Optional<SourceColumn> longitudeColumn = fieldMappingSet
                .getMappedColumn(field.getName(), CoordinateAxis.LONGITUDE.name())
                .flatMap(id -> source.getColumnById(id));

        if(latitudeColumn.isPresent() || longitudeColumn.isPresent()) {
            return Optional.of(new GeoPointMappedField(field, latitudeColumn, longitudeColumn));
        } else {
            return Optional.empty();
        }
    }
}
