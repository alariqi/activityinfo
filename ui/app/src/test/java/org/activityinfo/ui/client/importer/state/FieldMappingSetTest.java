package org.activityinfo.ui.client.importer.state;

import org.activityinfo.io.match.coord.CoordinateAxis;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class FieldMappingSetTest {

    @Test
    public void latLongReplace() {

        // First map columns LAT and LONG to a GEOPOINT field
        FieldMappingSet mappingSet = new FieldMappingSet()
                .withMapping("GEOPOINT", CoordinateAxis.LATITUDE.name(), "LAT")
                .withMapping("GEOPOINT", CoordinateAxis.LONGITUDE.name(), "LONG");


        assertThat(mappingSet, containsInAnyOrder(
                new FieldMapping("GEOPOINT", CoordinateAxis.LATITUDE.name(), "LAT"),
                new FieldMapping("GEOPOINT", CoordinateAxis.LONGITUDE.name(), "LONG")));

        // Now replace the mapping to LONG to a quantity field

        mappingSet = mappingSet.withMapping("Q1", FieldMapping.VALUE_ROLE, "LONG");

        assertThat(mappingSet, containsInAnyOrder(
                new FieldMapping("GEOPOINT", CoordinateAxis.LATITUDE.name(), "LAT"),
                new FieldMapping("Q1", FieldMapping.VALUE_ROLE, "LONG")));

    }

}