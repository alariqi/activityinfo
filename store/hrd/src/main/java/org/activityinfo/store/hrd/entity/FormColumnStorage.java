package org.activityinfo.store.hrd.entity;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.*;
import com.googlecode.objectify.condition.IfNull;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.store.hrd.columns.RecordIndexSet;
import org.activityinfo.store.hrd.columns.RecordNumbering;

@Entity(name = "FormColumns")
public class FormColumnStorage {

    /**
     * The {@link FormEntity} key, the root key of the Form Entity Group.
     */
    @Parent
    private Key<FormEntity> formKey;

    @Id
    private RecordNumbering scheme;

    @Unindex
    private int recordCount;

    @Unindex
    private int deletedCount;

    @IgnoreSave(IfNull.class)
    private Blob deletedRecordSet;

    @Index
    private long version;


    public FormColumnStorage(ResourceId formId, RecordNumbering scheme) {
        this.formKey = FormEntity.key(formId);
        this.scheme = scheme;
    }

    public Key<FormEntity> getFormKey() {
        return formKey;
    }

    public ResourceId getFormId() {
        return ResourceId.valueOf(formKey.getName());
    }

    public RecordNumbering getScheme() {
        return scheme;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public static Key<FormColumnStorage> key(FormEntity rootEntity) {
        return Key.create(Key.create(rootEntity), FormColumnStorage.class, rootEntity.getActiveColumnStorage().name());
    }

    public void addDeletedIndex(int recordIndex) {
        this.deletedCount++;
        this.deletedRecordSet = RecordIndexSet.read(deletedRecordSet).add(recordIndex).toBlob();
    }
}
