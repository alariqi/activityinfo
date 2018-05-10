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
package org.activityinfo.store.mysql.side;

import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.legacy.CuidAdapter;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldType;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.NarrativeType;
import org.activityinfo.model.type.barcode.BarcodeType;
import org.activityinfo.model.type.enumerated.EnumItem;
import org.activityinfo.model.type.enumerated.EnumType;
import org.activityinfo.model.type.number.QuantityType;
import org.activityinfo.model.type.primitive.TextType;
import org.activityinfo.store.mysql.cursor.QueryExecutor;
import org.activityinfo.store.mysql.metadata.ActivityField;
import org.activityinfo.store.spi.CursorObserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Scans the indicatorValue and attributeValue tables 
 */
public class SideColumnBuilder {
    
    private static final Logger LOGGER = Logger.getLogger(SideColumnBuilder.class.getName());

    /**
     * Maps indicatorId to the value buffer
     */
    private Map<Integer, ValueBuffer> fieldMap = Maps.newHashMap();
    private Map<Integer, ValueBuffer> attributeMap = Maps.newHashMap();
    
    private final String newLine;
    private final FormClass formClass;
    private Optional<Integer> siteId = Optional.absent();
    
    public SideColumnBuilder(FormClass formClass) {
        this.formClass = formClass;
        this.newLine = "\n";
    }


    public void only(ResourceId resourceId) {
        this.siteId = Optional.of(CuidAdapter.getLegacyIdFromCuid(resourceId));
    }
    
    public void add(ActivityField field, final CursorObserver<FieldValue> observer) {
        ValueBuffer buffer = fieldMap.get(field.getId());

        if (buffer == null) {
            buffer = createBuffer(field.getFormField().getType(), observer);
            fieldMap.put(field.getId(), buffer);
            if(field.isAttributeGroup()) {
                // Map each individual attribute id to this buffer so we don't need to query
                // the attribute group from the database
                EnumType enumType = (EnumType) field.getFormField().getType();
                for (EnumItem enumItem : enumType.getValues()) {
                    int attributeId = CuidAdapter.getLegacyIdFromCuid(enumItem.getId());
                    attributeMap.put(attributeId, buffer);
                }
            }
        }
    
        buffer.add(observer);
    }

    private ValueBuffer createBuffer(FieldType type, CursorObserver<FieldValue> observer) {

        if(type instanceof QuantityType) {
            return new QuantityBuffer((QuantityType) type);

        } else if(type instanceof TextType ||
                type instanceof NarrativeType ||
                type instanceof BarcodeType) {
            return new TextBuffer();

        } else if(type instanceof EnumType) {
            return new AttributeBuffer((EnumType) type);

        } else {
            return new JsonValueBuffer(type);
        }
    }

    public void sitesIndicators(int activityId, QueryExecutor executor) throws SQLException {
        StringBuilder sql = new StringBuilder();

        if(formClass.getId().getDomain() == CuidAdapter.ACTIVITY_DOMAIN) {

            sql.append("SELECT site.siteId, iv.indicatorId, iv.value, iv.textValue").append(newLine);
            sql.append("FROM site").append(newLine);
            sql.append("LEFT JOIN reportingperiod rp ON (site.siteId = rp.siteId)").append(newLine);
            sql.append("LEFT JOIN indicatorvalue iv ON (rp.reportingPeriodId = iv.reportingPeriodId ");
            sql.append("  AND iv.indicatorId IN (");
            Joiner.on(", ").appendTo(sql, fieldMap.keySet());
            sql.append("))").append(newLine);
            if(siteId.isPresent()) {
                sql.append("WHERE site.SiteId=").append(siteId.get()).append(newLine);
            } else {
                sql.append("WHERE site.deleted=0 AND site.activityId=").append(activityId).append(newLine);
            }
            sql.append("ORDER BY site.siteId");
        } else {
            // Reporting periods
            sql.append("SELECT rp.reportingPeriodId, iv.indicatorId, iv.value, iv.textValue").append(newLine);
            sql.append("FROM reportingperiod rp").append(newLine);
            sql.append("LEFT JOIN indicatorvalue iv ON (rp.reportingPeriodId = iv.reportingPeriodId ");
            sql.append("  AND iv.indicatorId IN (");
            Joiner.on(", ").appendTo(sql, fieldMap.keySet());
            sql.append("))").append(newLine);
            if(siteId.isPresent()) {
                sql.append("WHERE rp.SiteId=").append(siteId.get()).append(newLine);
            } else {
                sql.append("WHERE rp.deleted=0 AND rp.activityId=").append(activityId).append(newLine);
            }
            sql.append("ORDER BY rp.reportingPeriodId");
        }
        System.out.println(sql);
        
        execute(executor, sql);
            
    }
    
    public void attributes(int activityId, QueryExecutor executor) throws SQLException {
        if(formClass.getId().getDomain() == CuidAdapter.MONTHLY_REPORT_FORM_CLASS) {
            throw new UnsupportedOperationException("Attributes are not fields of reporting periods");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT site.siteId, -1, av.attributeId, av.value").append(newLine);
        sql.append("FROM site").append(newLine);
        sql.append("LEFT JOIN attributevalue av ON (site.siteId = av.siteId)").append(newLine);
        if(siteId.isPresent()) {
            sql.append("WHERE site.SiteId=").append(siteId.get()).append(newLine);
        } else {
            sql.append("WHERE site.deleted=0 AND site.activityId=").append(activityId).append(newLine);
        }
        sql.append("ORDER BY site.siteId");
        
        execute(executor, sql);
    }
    
    private void execute(QueryExecutor executor, StringBuilder sql) throws SQLException {
        int lastRowId = -1;

        ValueBuffer buffers[] = bufferArray();

        try(ResultSet rs = executor.query(sql.toString())) {
            while(rs.next()) {
                int rowId = rs.getInt(ValueBuffer.ROW_ID_COLUMN);
                if(rowId != lastRowId && lastRowId != -1) {
                    for(int i=0;i!=buffers.length;++i) {
                        buffers[i].next();
                    }
                }
                int fieldId = rs.getInt(ValueBuffer.FIELD_ID_COLUMN);
                if(!rs.wasNull()) {
                    ValueBuffer valueBuffer;
                    if(fieldId > 0) {
                        valueBuffer = fieldMap.get(fieldId);
                    } else {
                        int attributeId = rs.getInt(ValueBuffer.ATTRIBUTE_ID_COLUMN);
                        valueBuffer = attributeMap.get(attributeId);
                    }
                    if(valueBuffer != null) {
                        valueBuffer.set(rs);
                    }
                }
                lastRowId = rowId;
            }
            if(lastRowId != -1) {
                for(int i=0;i!=buffers.length;++i) {
                    buffers[i].next();
                }
            }
            for(int i=0;i!=buffers.length;++i) {
                buffers[i].done();
            }
        }
    }

    private ValueBuffer[] bufferArray() {
        List<ValueBuffer> buffers = Lists.newArrayList(fieldMap.values());
        return buffers.toArray(new ValueBuffer[buffers.size()]);
    }

    public boolean isEmpty() {
        return fieldMap.isEmpty();
    }

}
