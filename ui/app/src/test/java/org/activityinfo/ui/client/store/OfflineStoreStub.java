package org.activityinfo.ui.client.store;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.observable.Observable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OfflineStoreStub implements OfflineStore {

    private Map<ResourceId, String> formSchemaMap = new HashMap<>();

    private Set<ResourceId> offlineForms = new HashSet<>();


    @Override
    public void putSchema(FormClass formSchema) {
        formSchemaMap.put(formSchema.getId(), formSchema.toJsonString());
    }

    @Override
    public void loadSchema(ResourceId formId, AsyncCallback<FormClass> callback) {
        String formSchemaJson = formSchemaMap.get(formId);
        if(formSchemaJson != null) {
            FormClass formSchema = FormClass.fromJson(formSchemaJson);
            callback.onSuccess(formSchema);
        }
    }

    @Override
    public void enableOffline(ResourceId formId, boolean offline) {
        if(offline) {
            offlineForms.add(formId);
        } else {
            offlineForms.remove(formId);
        }
    }

    @Override
    public Observable<Set<ResourceId>> getOfflineForms() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void store(Snapshot snapshot) {
        throw new UnsupportedOperationException();
    }
}
