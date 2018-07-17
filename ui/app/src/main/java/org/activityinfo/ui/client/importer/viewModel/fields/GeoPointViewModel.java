package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.io.match.coord.CoordinateAxis;
import org.activityinfo.model.form.FormField;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.state.FieldMapping;
import org.activityinfo.ui.client.importer.state.GeoPointMapping;
import org.activityinfo.ui.client.importer.viewModel.MappedSourceViewModel;

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
    public Observable<java.util.Optional<ImportedFieldViewModel>> computeImport(Observable<MappedSourceViewModel> source) {
        return Observable.just(java.util.Optional.empty());
    }

    @Override
    public Optional<String> columnMappingLabel(FieldMapping fieldMapping, String columnId) {
        if(fieldMapping instanceof GeoPointMapping) {
            GeoPointMapping geoPointMapping = (GeoPointMapping) fieldMapping;
            if(geoPointMapping.isColumnMapped(columnId, CoordinateAxis.LATITUDE)) {
                return Optional.of(I18N.CONSTANTS.latitude() + " - " + field.getLabel());
            }  else if(geoPointMapping.isColumnMapped(columnId, CoordinateAxis.LONGITUDE)) {
                return Optional.of(I18N.CONSTANTS.longitude() + " - " + field.getLabel());
            }
        }
        return Optional.empty();
    }
}
