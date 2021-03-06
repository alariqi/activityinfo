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

import org.activityinfo.legacy.shared.model.SiteDTO;
import org.activityinfo.legacy.shared.reports.model.PointValue;
import org.activityinfo.legacy.shared.reports.util.mapping.TileMath;
import org.activityinfo.model.type.geo.AiLatLng;
import org.activityinfo.model.type.geo.Extents;
import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Reasonably complex, real-world point input data for testing
 *
 * @author Alex Bertram
 */
public class ComplexPoints {
    public double originalSum;
    public Extents extents;
    public List<PointValue> points;
    public List<AiLatLng> latlngs;
    public MarkerGraph graph;

    ComplexPoints() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                GraphTest.class.getResourceAsStream("/msa-points.csv")));

        extents = Extents.emptyExtents();

        originalSum = 0;

        points = new ArrayList<PointValue>();
        latlngs = new ArrayList<AiLatLng>();
        while (in.ready()) {

            String line = in.readLine();
            String[] columns = line.split(",");

            Integer.parseInt(columns[1]);

            double lng = Double.parseDouble(columns[2]);
            double lat = Double.parseDouble(columns[3]);
            double value = Double.parseDouble(columns[4]);

            PointValue pv = new PointValue();
            pv.setValue(value);
            pv.setSite(new SiteDTO());
            points.add(pv);

            originalSum += value;

            latlngs.add(new AiLatLng(lat, lng));

            extents.grow(lat, lng);

        }

        // project
        int zoom = TileMath.zoomLevelForExtents(extents, 640, 480);
        TiledMap map = new TiledMap(640, 480, extents.center(), zoom);

        for (int i = 0; i != points.size(); ++i) {
            points.get(i).setPx(map.fromLatLngToPixel(latlngs.get(i)));
        }

        // build graph
        graph = new MarkerGraph(points,
                new MarkerGraph.IntersectionCalculator() {
                    @Override
                    public boolean intersects(MarkerGraph.Node a, MarkerGraph.Node b) {
                        return a.getPoint().distance(b.getPoint()) < 30;
                    }
                });
    }

    public List<List<MarkerGraph.Node>> getLargestN(int count) {
        List<List<MarkerGraph.Node>> subgraphs = graph.getSubgraphs();
        Collections.sort(subgraphs, new Comparator<List<MarkerGraph.Node>>() {
            @Override
            public int compare(List<MarkerGraph.Node> o1,
                               List<MarkerGraph.Node> o2) {
                if (o1.size() > o2.size()) {
                    return -1;
                } else if (o1.size() < o2.size()) {
                    return +1;
                } else {
                    return 0;
                }
            }
        });

        while (subgraphs.size() > count) {
            subgraphs.remove(count);
        }

        return subgraphs;
    }
}
