/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.server.report.generator.map;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.activityinfo.TestOutput;
import org.activityinfo.fixtures.InjectionSupport;
import org.activityinfo.legacy.shared.command.GenerateElement;
import org.activityinfo.legacy.shared.model.IndicatorDTO;
import org.activityinfo.legacy.shared.reports.content.*;
import org.activityinfo.legacy.shared.reports.model.MapReportElement;
import org.activityinfo.legacy.shared.reports.model.layers.PolygonMapLayer;
import org.activityinfo.model.type.geo.AiLatLng;
import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.geo.TestGeometry;
import org.activityinfo.server.report.Reports;
import org.activityinfo.server.report.generator.MapGenerator;
import org.activityinfo.server.report.renderer.itext.PdfReportRenderer;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/polygons.db.xml")
public class PolygonGeneratorTest extends CommandTestCase2 {


    public static final String MAP_ICON_PATH = "";

    @Inject
    private MapGenerator generator;


    @Test
    public void basicTest() throws IOException {

        PolygonMapLayer layer = new PolygonMapLayer();
        layer.addIndicatorId(1);
        layer.setAdminLevelId(1383);

        MapReportElement map = new MapReportElement();
        map.addLayer(layer);

        MapContent content = execute(new GenerateElement<MapContent>(map));
        map.setContent(content);

        Reports.toPdf(getClass(), map, "polygon");

        try(FileOutputStream fos = TestOutput.open(getClass(), "polygon.pdf")) {
            PdfReportRenderer renderer = new PdfReportRenderer(TestGeometry.get(), MAP_ICON_PATH);
            renderer.render(map, fos);
        }
    }

    @Test
    public void polygonWithHole() throws IOException {

        AdminMarker marker = new AdminMarker();
        marker.setAdminEntityId(1930);
        marker.setColor("#FFBBBB");

        AdminOverlay overlay = new AdminOverlay(1383);
        overlay.setOutlineColor("#FF0000");
        overlay.addPolygon(marker);

        PolygonMapLayer layer = new PolygonMapLayer();
        layer.addIndicatorId(1);
        layer.setAdminLevelId(1383);

        MapContent content = new MapContent();
        content.setZoomLevel(8);
        content.setBaseMap(GoogleBaseMap.ROADMAP);
        content.setCenter(new AiLatLng(12.60500192642215, -7.98924994468689));
        content.getAdminOverlays().add(overlay);
        content.setFilterDescriptions(new ArrayList<FilterDescription>());

        PolygonLegend.ColorClass clazz1 = new PolygonLegend.ColorClass(1, 53.6,
                "0000FF");
        PolygonLegend.ColorClass clazz2 = new PolygonLegend.ColorClass(600,
                600, "FF0000");

        PolygonLegend legend = new PolygonLegend(layer, Lists.newArrayList(
                clazz1, clazz2));

        content.getLegends().add(legend);

        IndicatorDTO indicator = new IndicatorDTO();
        indicator.setId(1);
        indicator.setName("Indicator Test");

        content.getIndicators().add(indicator);

        MapReportElement map = new MapReportElement();
        map.addLayer(layer);
        map.setContent(content);

        try(FileOutputStream fos = TestOutput.open(getClass(), "polygon-hole.pdf")) {
            PdfReportRenderer renderer = new PdfReportRenderer(TestGeometry.get(), MAP_ICON_PATH);
            renderer.render(map, fos);
        }
    }
}
