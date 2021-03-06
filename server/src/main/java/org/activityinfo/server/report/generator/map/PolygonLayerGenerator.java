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

import org.activityinfo.legacy.shared.command.DimensionType;
import org.activityinfo.legacy.shared.command.Filter;
import org.activityinfo.legacy.shared.command.GetAdminEntities;
import org.activityinfo.legacy.shared.command.PivotSites;
import org.activityinfo.legacy.shared.command.PivotSites.PivotResult;
import org.activityinfo.legacy.shared.command.result.AdminEntityResult;
import org.activityinfo.legacy.shared.command.result.Bucket;
import org.activityinfo.legacy.shared.model.AdminEntityDTO;
import org.activityinfo.legacy.shared.reports.content.AdminMarker;
import org.activityinfo.legacy.shared.reports.content.AdminOverlay;
import org.activityinfo.legacy.shared.reports.content.EntityCategory;
import org.activityinfo.legacy.shared.reports.content.MapContent;
import org.activityinfo.legacy.shared.reports.model.AdminDimension;
import org.activityinfo.legacy.shared.reports.model.layers.PolygonMapLayer;
import org.activityinfo.model.type.geo.Extents;
import org.activityinfo.server.command.DispatcherSync;

public class PolygonLayerGenerator implements LayerGenerator {

    private PolygonMapLayer layer;
    private PivotResult pivotResult;
    private MagnitudeScaleBuilder.Scale colorScale;
    private AdminOverlay overlay;
    private Jenks breakBuilder = new Jenks();

    public PolygonLayerGenerator(PolygonMapLayer layer) {
        super();
        this.layer = layer;
        this.overlay = new AdminOverlay(layer.getAdminLevelId());
        this.overlay.setOutlineColor(layer.getMaxColor());
    }

    @Override
    public void query(DispatcherSync dispatcher, Filter effectiveFilter) {
        Filter layerFilter = new Filter(effectiveFilter, layer.getFilter());
        layerFilter.addRestriction(DimensionType.Indicator, layer.getIndicatorIds());

        queryBounds(dispatcher, layerFilter);
        queryBuckets(dispatcher, layerFilter);
        color();
    }

    private void queryBounds(DispatcherSync dispatcher, Filter layerFilter) {
        GetAdminEntities query = new GetAdminEntities();
        query.setLevelId(layer.getAdminLevelId());

        AdminEntityResult entities = dispatcher.execute(query);
        for (AdminEntityDTO entity : entities.getData()) {
            if (entity.hasBounds()) {
                AdminMarker marker = new AdminMarker(entity);
                overlay.addPolygon(marker);
            }
        }
    }

    private void queryBuckets(DispatcherSync dispatcher, Filter layerFilter) {
        PivotSites query = new PivotSites();
        query.setFilter(layerFilter);
        AdminDimension adminDimension = new AdminDimension(layer.getAdminLevelId());
        query.setDimensions(adminDimension);

        MagnitudeScaleBuilder scaleBuilder = new MagnitudeScaleBuilder(layer);

        this.pivotResult = query.isTooBroad() ? new PivotResult() : dispatcher.execute(query);
        for (Bucket bucket : pivotResult.getBuckets()) {
            EntityCategory category = (EntityCategory) bucket.getCategory(adminDimension);
            if (category != null) {
                int adminEntityId = category.getId();
                AdminMarker polygon = overlay.getPolygon(adminEntityId);
                if (polygon != null) {
                    polygon.setValue(bucket.doubleValue());
                    scaleBuilder.addValue(bucket.doubleValue());
                }
            }
        }
        colorScale = scaleBuilder.build();
    }

    private void color() {
        for (AdminMarker polygon : overlay.getPolygons()) {
            if (polygon.hasValue()) {
                polygon.setColor(colorScale.color(polygon.getValue()).toHexString());
            } else {
                polygon.setColor(colorScale.color(null).toHexString());
            }
        }
    }

    @Override
    public Extents calculateExtents() {
        Extents extents = Extents.emptyExtents();
        for (AdminMarker polygon : overlay.getPolygons()) {
            if (polygon.hasValue()) {
                extents.grow(polygon.getExtents());
            }
        }
        return extents;
    }

    @Override
    public Margins calculateMargins() {
        return new Margins(0);
    }

    @Override
    public void generate(TiledMap map, MapContent content) {
        content.getAdminOverlays().add(overlay);
        content.getLegends().add(colorScale.legend());
    }
}
