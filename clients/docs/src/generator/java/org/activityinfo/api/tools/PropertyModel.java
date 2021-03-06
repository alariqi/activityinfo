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
package org.activityinfo.api.tools;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.swagger.models.properties.Property;

/**
 * Template wrapper for a property of a model
 */
public class PropertyModel {

    private final String name;
    private final Property value;
    private final DefinitionModel schema;

    public PropertyModel(String name, Property value) {
        this.name = name;
        this.value = value;
        this.schema = null;
    }

    public PropertyModel(String name, Property model, DefinitionModel definitionModel) {
        Preconditions.checkNotNull(definitionModel, "%s has null definitionModel", name);
        this.name = name;
        this.value = model;
        this.schema = definitionModel;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return value.getRequired();
    }

    public String getRequiredString() {
        return value.getRequired() ? "required" : "optional";
    }

    public String getDescription() {
        return Strings.nullToEmpty(value.getDescription());
    }

    public String getType() {
        return value.getType();
    }

    public boolean isArray() {
        return "array".equals(value.getType());
    }



}
