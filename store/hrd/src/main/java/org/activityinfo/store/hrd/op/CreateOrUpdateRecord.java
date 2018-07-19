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
package org.activityinfo.store.hrd.op;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.VoidWork;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.primitive.TextValue;
import org.activityinfo.store.hrd.Hrd;
import org.activityinfo.store.hrd.columns.*;
import org.activityinfo.store.hrd.entity.*;
import org.activityinfo.store.query.server.InvalidUpdateException;
import org.activityinfo.store.query.shared.columns.ViewBuilderFactory;
import org.activityinfo.store.spi.RecordChangeType;
import org.activityinfo.store.spi.TypedRecordUpdate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.googlecode.objectify.ObjectifyService.ofy;


public class CreateOrUpdateRecord extends VoidWork {

    private ResourceId formId;
    private TypedRecordUpdate update;

    public CreateOrUpdateRecord(ResourceId formId, TypedRecordUpdate update) {
        this.formId = formId;
        this.update = update;
    }

    @Override
    public void vrun() {

        FormEntity rootEntity = ofy().load().key(FormEntity.key(formId)).safe();
        FormClass formClass = ofy().load().key(FormSchemaEntity.key(formId)).safe().readFormClass();

        long currentVersion = rootEntity.getVersion();
        long newVersion = currentVersion + 1;

        rootEntity.setVersion(newVersion);

        FormRecordEntity existingEntity = ofy().load().key(FormRecordEntity.key(formClass, update.getRecordId())).now();
        FormRecordEntity updated;
        RecordChangeType changeType;

        if(existingEntity != null) {
            updated = existingEntity;
            changeType = update.isDeleted() ? RecordChangeType.DELETED : RecordChangeType.UPDATED;

        } else {
            updated = new FormRecordEntity(formId, update.getRecordId());
            changeType = RecordChangeType.CREATED;

            if (update.isDeleted()) {
                throw new InvalidUpdateException("Creation of entity with deleted flag is not allowed.");
            }

            if (formClass.getParentFormId().isPresent()) {
                ResourceId parentId = update.getParentId();
                if (parentId == null) {
                    throw new InvalidUpdateException("@parent is required for subform submissions");
                }
                updated.setParentRecordId(parentId);
            }
        }
        updated.setVersion(newVersion);
        updated.setSchemaVersion(rootEntity.getSchemaVersion());
        updated.setFieldValues(formClass, update.getChangedFieldValues());

        // Store a copy as a snapshot
        FormRecordSnapshotEntity snapshotEntity = new FormRecordSnapshotEntity(update.getUserId(), changeType, updated);

        // Queue up batch
        List<Object> toSave = new ArrayList<>();
        List<Key> toDelete = new ArrayList<>();

        // Update column-based storage

        ColumnBlockUpdater blockUpdater = new ColumnBlockUpdater(formClass, Hrd.ofy().getTransaction());

        if(changeType == RecordChangeType.CREATED) {
            int newRecordCount = rootEntity.getRecordCount() + 1;
            int newRecordNumber = newRecordCount;
            updated.setRecordNumber(newRecordNumber);
            rootEntity.setRecordCount(newRecordCount);

            blockUpdater.updateId(newRecordNumber, updated.getRecordId().asString());

            if(formClass.isSubForm()) {
                blockUpdater.updateParentId(newRecordNumber, updated.getParentRecordId());
            }

        } else if(changeType == RecordChangeType.DELETED) {
            rootEntity.setDeletedCount(rootEntity.getDeletedCount() + 1);
            blockUpdater.updateTombstone(existingEntity.getRecordNumber());
        }

        if(changeType != RecordChangeType.DELETED) {
            blockUpdater.updateFields(updated.getRecordNumber(), update.getChangedFieldValues());
        }

        toSave.addAll(blockUpdater.getUpdatedBlocks());

        // Update record-based storage
        toSave.add(rootEntity);
        toSave.add(snapshotEntity);

        if (update.isDeleted()) {
            toDelete.add(Key.create(updated));
        } else {
            toSave.add(updated);
        }

        ofy().save().entities(toSave);
        if(!toDelete.isEmpty()) {
            ofy().delete().entities(toDelete);
        }
    }



}
