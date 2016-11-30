package org.activityinfo.model.type.subform;
/*
* #%L
* ActivityInfo Server
* %%
* Copyright (C) 2009 - 2013 UNICEF
* %%
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public
* License along with this program. If not, see
* <http://www.gnu.org/licenses/gpl-3.0.html>.
* #L%
*/

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.resource.ResourceIdPrefixType;
import org.activityinfo.model.type.*;

/**
 * @author yuriyz on 12/03/2014.
 */
public class SubFormReferenceType implements ParametrizedFieldType {

    public static class TypeClass implements ParametrizedFieldTypeClass, RecordFieldTypeClass {

        private TypeClass() {
        }

        @Override
        public String getId() {
            return "SUBFORM";
        }

        @Override
        public SubFormReferenceType createType() {
            return new SubFormReferenceType();
        }

        @Override
        public FieldType deserializeType(JsonObject parametersObject) {
            ResourceId formId;
            if(parametersObject.has("classReference")) {
                formId = ResourceId.valueOf(parametersObject.get("classReference").getAsString());
            } else {
                formId = ResourceId.valueOf(parametersObject.get("formId").getAsString());
            }
            return new SubFormReferenceType(formId);
        }

        @Override
        public FormClass getParameterFormClass() {
            return new FormClass(ResourceIdPrefixType.TYPE.id("subform"));
        }


    }

    public static final TypeClass TYPE_CLASS = new TypeClass();

    /**
     * Keeps reference to subformFormClass.
     */
    private ResourceId classId;

    public SubFormReferenceType() {
        this(null);
    }

    public SubFormReferenceType(ResourceId classId) {
        this.classId = classId;
    }

    public SubFormReferenceType setClassId(ResourceId classId) {
        this.classId = classId;
        return this;
    }

    public ResourceId getClassId() {
        return classId;
    }

    @Override
    public ParametrizedFieldTypeClass getTypeClass() {
        return TYPE_CLASS;
    }

    @Override
    public FieldValue parseJsonValue(JsonElement value) {
        // Subforms don't have values in their parent.
        return null;
    }

    @Override
    public FormInstance getParameters() {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonObject getParametersAsJson() {
        JsonObject object = new JsonObject();
        object.addProperty("formId", classId.asString());
        return object;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public String toString() {
        return "SubFormType";
    }
}