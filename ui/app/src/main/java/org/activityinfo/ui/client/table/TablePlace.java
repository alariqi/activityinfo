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
import org.activityinfo.ui.client.Place2;

import java.util.Objects;
import java.util.Optional;

/**
 * Table view place for a specific form.
 * Either:
 * <li>Root table</li>
 * <li>Sub form table</li>
 * <li>Edit record </li>
 */
public class TablePlace extends Place2 {

    public enum Mode {
        ROOT_TABLE,
        SUBFORM_TABLE,
        FORM
    }

    public interface Case<T> {
        T rootTable(ResourceId formId);
        T subFormTable(ResourceId formId, ResourceId subFormId, RecordRef parentRef);
        T editForm(ResourceId formId, RecordRef ref);
    }

    private ResourceId rootFormId;
    private Mode mode;
    private Optional<RecordRef> recordRef;
    private Optional<ResourceId> subFormId;

    private TablePlace() {
    }

    public TablePlace(ResourceId rootFormId) {
        this.rootFormId = rootFormId;
        this.mode = Mode.ROOT_TABLE;
    }

    public ResourceId getRootFormId() {
        return rootFormId;
    }

    public ResourceId getActiveFormId() {
        return switchCase(new Case<ResourceId>() {
            @Override
            public ResourceId rootTable(ResourceId formId) {
                return formId;
            }

            @Override
            public ResourceId subFormTable(ResourceId formId, ResourceId subFormId, RecordRef parentRef) {
                return subFormId;
            }

            @Override
            public ResourceId editForm(ResourceId formId, RecordRef ref) {
                return ref.getFormId();
            }
        });
    }

    public Mode getMode() {
        return mode;
    }

    public <T> T switchCase(Case<T> case_) {
        switch (mode) {
            default:
            case ROOT_TABLE:
                return case_.rootTable(rootFormId);
            case SUBFORM_TABLE:
                return case_.subFormTable(rootFormId, subFormId.get(), recordRef.get());
            case FORM:
                return case_.editForm(rootFormId, recordRef.get());
        }
    }

    public TablePlace subform(ResourceId subFormId, RecordRef parentRef) {
        TablePlace place = new TablePlace(this.rootFormId);
        place.mode = Mode.SUBFORM_TABLE;
        place.subFormId = Optional.of(subFormId);
        place.recordRef = Optional.of(parentRef);
        return place;
    }

    @Override
    public String toString() {
        return switchCase(new Case<String>() {
            @Override
            public String rootTable(ResourceId formId) {
                return "table/" + formId.asString();
            }

            @Override
            public String subFormTable(ResourceId formId, ResourceId subFormId, RecordRef parentRef) {
                return "table/" + formId.asString() + "/subform/" + subFormId.asString() + "/" + parentRef.toQualifiedString();
            }

            @Override
            public String editForm(ResourceId formId, RecordRef ref) {
                return "table/" + formId.asString() + "/edit/" + recordRef.get().toQualifiedString();
            }
        });
    }

    public static TablePlace parse(String[] parts) {
        assert parts[0].equals("table");
        TablePlace tablePlace = new TablePlace();
        tablePlace.rootFormId = ResourceId.valueOf(parts[1]);
        tablePlace.mode = Mode.ROOT_TABLE;

        if(parts.length > 2) {
            switch (parts[2]) {
                case "subform":
                    tablePlace.mode = Mode.SUBFORM_TABLE;
                    tablePlace.subFormId = Optional.of(ResourceId.valueOf(parts[3]));
                    tablePlace.recordRef = Optional.of(RecordRef.fromQualifiedString(parts[4]));
                    break;
                case "edit":
                    tablePlace.mode = Mode.FORM;
                    tablePlace.recordRef = Optional.of(RecordRef.fromQualifiedString(parts[3]));
                    break;

            }
        }

        return tablePlace;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TablePlace that = (TablePlace) o;
        return Objects.equals(rootFormId, that.rootFormId) &&
                mode == that.mode &&
                Objects.equals(recordRef, that.recordRef) &&
                Objects.equals(subFormId, that.subFormId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootFormId, mode, recordRef, subFormId);
    }
}
