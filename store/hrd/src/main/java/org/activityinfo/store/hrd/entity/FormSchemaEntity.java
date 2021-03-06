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
package org.activityinfo.store.hrd.entity;

import com.google.appengine.api.datastore.EmbeddedEntity;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Unindex;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.store.hrd.FormConverter;
import org.activityinfo.store.spi.SubFormPatch;

/**
 * Entity storing a Form's Schema.
 */
@Entity(name = "FormSchema")
public class FormSchemaEntity {

    private static final long ENTITY_ID = 1L;

    /**
     * The {@link FormEntity} key, the root key of the Form Entity Group.
     */
    @Parent
    private Key<FormEntity> formKey;

    /**
     * Always {@code 1L} as there is one {@code FormSchema} per Form Entity Group.
     */
    @Id
    private long id;

    /**
     * The current version of the schema for this Form.
     */
    @Unindex
    private long schemaVersion;

    /**
     * The {@link FormClass} serialized as an {@link EmbeddedEntity} via {@link FormClass#toJson()} ()}
     */
    @Unindex
    private EmbeddedEntity schema;


    public FormSchemaEntity() {
    }

    public FormSchemaEntity(FormClass formClass) {
        
        this.formKey = FormEntity.key(formClass);
        this.id = ENTITY_ID;
        this.schema = FormConverter.toEmbeddedEntity(formClass.toJson());
    }
    
    public static com.googlecode.objectify.Key<FormSchemaEntity> key(ResourceId formId) {
        return com.googlecode.objectify.Key.create(FormEntity.key(formId), FormSchemaEntity.class, ENTITY_ID);
    }

    public ResourceId getFormId() {
        return ResourceId.valueOf(formKey.getName());
    }

    public long getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(long schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public void setSchema(FormClass formClass) {
        schema = FormConverter.toEmbeddedEntity(formClass.toJson());
    }

    public EmbeddedEntity getSchema() {
        return schema;
    }

    public FormClass readFormClass() {
        return
            SubFormPatch.patch(
              FormClass.fromJson(FormConverter.fromEmbeddedEntity(schema)));
    }
}
