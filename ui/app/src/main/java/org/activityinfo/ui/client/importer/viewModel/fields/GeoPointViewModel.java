package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.io.match.coord.CoordinateAxis;
import org.activityinfo.model.form.FormField;
import org.activityinfo.ui.client.importer.state.FieldMappingSet;
import org.activityinfo.ui.client.importer.state.GeoPointMapping;

import java.util.*;

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
    public Collection<ColumnTarget> unusedTarget(FieldMappingSet explicitMappings) {
        GeoPointMapping mapping = explicitMappings.getFieldMapping(field.getName())
                .filter(m -> m instanceof GeoPointMapping)
                .map(m -> (GeoPointMapping)m)
                .orElse(new GeoPointMapping(field.getName(), Optional.empty(), Optional.empty()));

        List<ColumnTarget> targets = new ArrayList<>();
        if(!mapping.getLongitudeColumnId().isPresent()) {
            targets.add(longitudeTarget);
        }
        if(!mapping.getLatitudeColumnId().isPresent()) {
            targets.add(latitudeTarget);
        }

        return targets;
    }

    @Override
    public List<ColumnTarget> getTargets() {
        return Arrays.asList(latitudeTarget, longitudeTarget);
    }
}
