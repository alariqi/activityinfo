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
package org.activityinfo.ui.client.table;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.ui.client.Place;

import java.util.Objects;
import java.util.Optional;

/**
 * Table view place for a specific form.
 */
public class TablePlace extends Place {


    private ResourceId formId;
    private Optional<String> parentId;

    private TablePlace() {
    }

    public TablePlace(ResourceId rootFormId) {
        this.formId = rootFormId;
        this.parentId = Optional.empty();
    }

    public TablePlace(ResourceId subFormId, RecordRef parentRef) {
        this.formId = subFormId;
        this.parentId = Optional.of(parentRef.getRecordId().asString());
    }

    public TablePlace(ResourceId subFormId, String recordId) {
        this.formId = subFormId;
        this.parentId = Optional.of(recordId);
    }

    public ResourceId getFormId() {
        return formId;
    }

    public Optional<String> getParentId() {
        return parentId;
    }

    @Override
    public String toString() {
        return "table/" + formId.asString() +
                parentId.map(id -> "/" + id).orElse("");
    }

    public static TablePlace parse(String[] parts) {
        assert parts[0].equals("table");
        TablePlace tablePlace = new TablePlace();
        tablePlace.formId = ResourceId.valueOf(parts[1]);
        if(parts.length == 3) {
            tablePlace.parentId = Optional.of(parts[2]);
        } else {
            tablePlace.parentId = Optional.empty();
        }
        return tablePlace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TablePlace that = (TablePlace) o;
        return Objects.equals(formId, that.formId) &&
                Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formId, parentId);
    }
}
