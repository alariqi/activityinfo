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

import com.google.common.collect.Maps;
import org.activityinfo.legacy.shared.model.SiteDTO;
import org.activityinfo.legacy.shared.reports.content.MapContent;
import org.activityinfo.legacy.shared.reports.content.PieMapMarker;
import org.activityinfo.legacy.shared.reports.model.clustering.NoClustering;
import org.activityinfo.legacy.shared.reports.model.layers.PiechartMapLayer;
import org.activityinfo.model.type.geo.AiLatLng;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PiechartMapLayerGeneratorTest {

    @Test
    public void testSomething() {
        SiteDTO siteData = new SiteDTO();
        siteData.setId(42);
        siteData.setX(15.0);
        siteData.setY(0.0);
        siteData.setIndicatorValue(1, 50.0);
        siteData.setIndicatorValue(2, 10.0);
        siteData.setIndicatorValue(3, 20.0);
        siteData.setIndicatorValue(4, 40.0);

        PiechartMapLayer pcml = new PiechartMapLayer();
        pcml.setMinRadius(10);
        pcml.setMaxRadius(50);
        pcml.addIndicatorId(1);
        pcml.addIndicatorId(2);
        pcml.addIndicatorId(3);
        pcml.addIndicatorId(4);
        pcml.setClustering(new NoClustering());

        TiledMap map = new TiledMap(500, 600, new AiLatLng(15.0, 0.0), 6);

        Map<Integer, Indicator> indicators = Maps.newHashMap();
        indicators.put(1, new Indicator());
        indicators.put(2, new Indicator());
        indicators.put(3, new Indicator());
        indicators.put(4, new Indicator());

        PiechartLayerGenerator gen = new PiechartLayerGenerator(pcml, indicators);
        gen.setSites(Arrays.asList(siteData));

        MapContent mc = new MapContent();

        gen.generate(map, mc);

        assertThat(mc.getMarkers().size(), equalTo(1));
        assertThat(((PieMapMarker) mc.getMarkers().get(0)).getSlices().size(),
                equalTo(4));
    }
}
