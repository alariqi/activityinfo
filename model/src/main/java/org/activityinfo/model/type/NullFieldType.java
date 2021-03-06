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
package org.activityinfo.model.type;

import org.activityinfo.json.JsonValue;

/**
 * Type of the empty value
 */
public final class NullFieldType implements FieldType, FieldTypeClass {

    public static final NullFieldType INSTANCE = new NullFieldType();
    
    private NullFieldType() {
    }
    
    @Override
    public FieldTypeClass getTypeClass() {
        return this;
    }

    @Override
    public FieldValue parseJsonValue(JsonValue value) {
        return NullFieldValue.INSTANCE;
    }

    @Override
    public <T> T accept(FieldTypeVisitor<T> visitor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isUpdatable() {
        return true;
    }

    @Override
    public String getId() {
        return "null";
    }

    @Override
    public FieldType createType() {
        return this;
    }

    /**
     *
     * @return the singleton instance for this type
     */
    private Object readResolve() {
        return INSTANCE;
    }

}
