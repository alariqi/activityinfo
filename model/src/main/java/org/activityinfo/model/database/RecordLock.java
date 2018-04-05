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
package org.activityinfo.model.database;

import org.activityinfo.json.AutoJson;
import org.activityinfo.json.Json;
import org.activityinfo.json.JsonSerializable;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.annotation.AutoBuilder;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.time.LocalDateInterval;

@AutoJson
@AutoBuilder
public class RecordLock implements JsonSerializable {
    ResourceId id;
    String label;
    ResourceId resourceId;
    LocalDateInterval dateRange;

    RecordLock() {}

    /**
     * @return the id of this lock itself.
     */
    public ResourceId getId() {
        return id;
    }

    /**
     * @return the name of this lock.
     */
    public String getLabel() {
        return label;
    }

    /**
     *
     * @return the id of the resource to be locked.
     */
    public ResourceId getResourceId() {
        return resourceId;
    }

    /**
     * @return the date range of records to which this lock applies.
     */
    public LocalDateInterval getDateRange() {
        return dateRange;
    }

    @Override
    public JsonValue toJson() {
        JsonValue object = Json.createObject();
        object.put("id", id.asString());
        object.put("label", label);
        object.put("resourceId", resourceId.asString());
        object.put("dateRange", dateRange.toJson());
        return object;
    }

    public static RecordLock fromJson(JsonValue object) {
        RecordLock lock = new RecordLock();
        lock.id = ResourceId.valueOf(object.getString("id"));
        lock.label = object.getString("label");
        lock.resourceId = ResourceId.valueOf(object.getString("resourceId"));
        lock.dateRange = LocalDateInterval.fromJson(object.get("dateRange"));

        return lock;
    }

}
