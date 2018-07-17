package org.activityinfo.ui.client.importer.state;

import org.activityinfo.io.match.coord.CoordinateAxis;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class FieldMappingSetTest {

    @Test
    public void latLongReplace() {

        // First map columns LAT and LONG to a GEOPOINT field
        FieldMappingSet mappingSet = new FieldMappingSet()
                .withGeoPointMapping("GEOPOINT", CoordinateAxis.LATITUDE, "LAT")
                .withGeoPointMapping("GEOPOINT", CoordinateAxis.LONGITUDE, "LONG");


        assertThat(mappingSet.getFieldMapping("GEOPOINT").get(),
                equalTo(new GeoPointMapping("GEOPOINT", "LAT", "LONG")));

        // Now replace the mapping to LONG to a quantity field

        mappingSet = mappingSet.withSimpleMapping("Q1", "LONG");

        assertThat(mappingSet.getMappings(), containsInAnyOrder(
                new GeoPointMapping("GEOPOINT", Optional.of("LAT"), Optional.empty()),
                new SimpleFieldMapping("Q1", "LONG")));


    }

}