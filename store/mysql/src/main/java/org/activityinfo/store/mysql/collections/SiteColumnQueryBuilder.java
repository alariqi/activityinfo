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
package org.activityinfo.store.mysql.collections;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.store.mysql.cursor.MySqlCursorBuilder;
import org.activityinfo.store.mysql.cursor.QueryExecutor;
import org.activityinfo.store.mysql.mapping.FieldMapping;
import org.activityinfo.store.mysql.mapping.TableMapping;
import org.activityinfo.store.mysql.metadata.Activity;
import org.activityinfo.store.mysql.metadata.ActivityField;
import org.activityinfo.store.mysql.side.BoundLocationBuilder;
import org.activityinfo.store.mysql.side.SideColumnBuilder;
import org.activityinfo.store.spi.ColumnQueryBuilder;
import org.activityinfo.store.spi.Cursor;
import org.activityinfo.store.spi.CursorObserver;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class SiteColumnQueryBuilder implements ColumnQueryBuilder {
    
    private static final Logger LOGGER = Logger.getLogger(SiteColumnQueryBuilder.class.getName());
    
    private final Activity activity;
    private final TableMapping tableMapping;
    private QueryExecutor executor;
    private final MySqlCursorBuilder baseCursor;
    private final SideColumnBuilder indicators;
    private final SideColumnBuilder attributes;
    private BoundLocationBuilder boundLocation;
    

    private Map<ResourceId, ActivityField> fieldMap = Maps.newHashMap();
    
    public SiteColumnQueryBuilder(Activity activity, TableMapping tableMapping, QueryExecutor executor) {
        this.activity = activity;
        this.tableMapping = tableMapping;
        this.executor = executor;
        this.baseCursor = new MySqlCursorBuilder(tableMapping, executor);
        this.indicators = new SideColumnBuilder(tableMapping.getFormClass());
        this.attributes = new SideColumnBuilder(tableMapping.getFormClass());
        this.boundLocation = new BoundLocationBuilder(activity);
        
        for(ActivityField field : activity.getFields()) {
            fieldMap.put(field.getResourceId(), field);
        }
    }
    
    @Override
    public void only(ResourceId resourceId) {
        baseCursor.only(resourceId);
        indicators.only(resourceId);
        attributes.only(resourceId);
        boundLocation.only(resourceId);
    }

    @Override
    public void addResourceId(CursorObserver<ResourceId> observer) {
        baseCursor.addResourceId(observer);
    }

    @Override
    public void addField(ResourceId fieldId, CursorObserver<FieldValue> observer) {

        if(boundLocation.accept(fieldId)) {
            boundLocation.addObserver(observer);
            return;
        }

        FieldMapping mapping = tableMapping.getMapping(fieldId);
        if(mapping != null) {  
            baseCursor.addField(fieldId, observer);

        } else {
            ActivityField field = fieldMap.get(fieldId);
            if(field == null) {
                throw new IllegalArgumentException("fieldId: " + fieldId);
            }
            if(field.isIndicator()) {
                indicators.add(field, observer);
            } else {
                attributes.add(field, observer);
            }
        }
    }

    @Override
    public void execute() {
        
        try {

            Stopwatch stopwatch = Stopwatch.createUnstarted();
            
            if(baseCursor.hasObservers()) {
                stopwatch.start();
                
                // Run base table
                Cursor cursor = baseCursor.open();
                while (cursor.next()) {
                }
                stopwatch.stop();

            }
            
            
            LOGGER.fine("Scanned site table in " + stopwatch);

            stopwatch.reset().start();
            
            if(boundLocation.hasObservers()) {
                boundLocation.execute(executor);
            }
            
            
            // Run indicator loop
            if (!indicators.isEmpty()) {
                indicators.sitesIndicators(activity.getId(), executor);
                
                LOGGER.fine("Scanned indicatorValue table in " + stopwatch);
            }
            
            
            stopwatch.reset().start();
            
            if (!attributes.isEmpty()) {
                attributes.attributes(activity.getId(), executor);

                LOGGER.fine("Scanned attributeValue in " + stopwatch);
            }
            
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
