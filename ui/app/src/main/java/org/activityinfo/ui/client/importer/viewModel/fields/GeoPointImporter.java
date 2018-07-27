package org.activityinfo.ui.client.importer.viewModel.fields;

import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.geo.GeoPoint;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;

public class GeoPointImporter implements FieldImporter {

    private final String fieldName;
    private final ColumnView latitude;
    private final ColumnView longitude;

    public GeoPointImporter(String fieldName, SourceColumn latitudeColumn, SourceColumn longitudeColumn) {
        this.fieldName = fieldName;
        this.latitude = latitudeColumn.getColumnView();
        this.longitude = longitudeColumn.getColumnView();
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public FieldValue getValue(int rowIndex) {

        String latitudeString = latitude.getString(rowIndex);
        String longitudeString = longitude.getString(rowIndex);

        if(latitudeString == null || longitudeString == null) {
            return null;
        }
        return new GeoPoint(Double.parseDouble(latitudeString), Double.parseDouble(longitudeString));
    }
}
