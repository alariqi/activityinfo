package org.activityinfo.geoadmin.merge2.state;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Geometry;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.form.FormPermissions;
import org.activityinfo.model.form.FormRecord;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.ReferenceType;
import org.activityinfo.model.type.ReferenceValue;
import org.activityinfo.model.type.geo.Extents;
import org.activityinfo.model.type.geo.GeoArea;
import org.activityinfo.model.type.geo.GeoAreaType;
import org.activityinfo.model.type.primitive.TextType;
import org.activityinfo.model.type.primitive.TextValue;
import org.activityinfo.store.spi.ColumnQueryBuilder;
import org.activityinfo.store.spi.CursorObserver;
import org.activityinfo.store.spi.FormStorage;
import org.activityinfo.store.spi.TypedRecordUpdate;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class JsonFormStorage implements FormStorage {
    
    private int id;
    private int parentId;
    private String name;
    
    private final FormClass formClass;
    private final JsonValue instances;
    
    public JsonFormStorage(String resourceName) throws IOException {
        formClass = loadFormClass(resourceName);
        instances = loadInstances(resourceName);
    }

    private FormClass loadFormClass(String resourceName) throws IOException {
        String json = getJson(resourceName + "/form.json").read();
        return FormClass.fromJson(json);
    }

    private JsonValue loadInstances(String resourceName) throws IOException {
        Gson gson = new Gson();
        try(Reader reader = getJson(resourceName + "/instances.json").openStream()) {
            return gson.fromJson(reader, JsonValue.class);
        } catch (Exception e) {
            throw new IOException("Exception loading instances for " + resourceName, e);
        }
    }

    private CharSource getJson(String resourceName) throws IOException {
        URL classUrl = Resources.getResource(resourceName);
        return Resources.asCharSource(classUrl, Charsets.UTF_8);
    }
    
    private FormField getField(ResourceId id) {
        for (FormField formField : formClass.getFields()) {
            if(formField.getId().equals(id)) {
                return formField;
            }
        }
        throw new IllegalArgumentException(id.toString());
    }


    @Override
    public FormPermissions getPermissions(int userId) {
        return FormPermissions.readWrite();
    }

    @Override
    public Optional<FormRecord> get(ResourceId resourceId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<FormRecord> getSubRecords(ResourceId resourceId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormClass getFormClass() {
         return formClass;
    }

    @Override
    public void updateFormClass(FormClass formClass) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(TypedRecordUpdate update) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(TypedRecordUpdate update) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ColumnQueryBuilder newColumnQuery() {
        return new JsonQueryBuilder();
    }

    @Override
    public long cacheVersion() {
        return 0;
    }

    @Override
    public void updateGeometry(ResourceId recordId, ResourceId fieldId, Geometry value) {
        throw new UnsupportedOperationException();
    }


    private class JsonQueryBuilder implements ColumnQueryBuilder {
        
        private List<CursorObserver<JsonValue>> bindings = new ArrayList<>();

   
        @Override
        public void only(ResourceId resourceId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addResourceId(CursorObserver<ResourceId> observer) {
            bindings.add(new IdBinding(observer));
        }

        @Override
        public void addField(ResourceId fieldId, CursorObserver<FieldValue> observer) {
            FormField field = getField(fieldId);
            if(field.getType() instanceof TextType) {
                bindings.add(new TextFieldBinding(fieldId.asString(), observer));
            } else if(field.getType() instanceof ReferenceType) {
                bindings.add(new ReferenceFieldBinding(fieldId.asString(), observer));
            } else if(field.getType() instanceof GeoAreaType) {
                bindings.add(new GeoAreaFieldBinding(fieldId.asString(), observer));
            } else {
                throw new UnsupportedOperationException("type: " + field.getType());
            }
        }

        @Override
        public void execute() {
            for(int i=0;i<instances.length();++i) {
                JsonValue instance = instances.get(i);
                for (CursorObserver<JsonValue> binding : bindings) {
                    binding.onNext(instance);
                }
            }
            for (CursorObserver<JsonValue> binding : bindings) {
                binding.done();
            }
        }
    }

    private static class IdBinding implements CursorObserver<JsonValue> {
        private CursorObserver<ResourceId> observer;

        public IdBinding(CursorObserver<ResourceId> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(JsonValue instance) {
            observer.onNext(ResourceId.valueOf(instance.get("id").asString()));
        }

        @Override
        public void done() {
            observer.done();
        }
    }
    
    private abstract static class FieldBinding implements CursorObserver<JsonValue> {
        private String field;
        private CursorObserver<FieldValue> observer;

        public FieldBinding(String field, CursorObserver<FieldValue> observer) {
            this.field = field;
            this.observer = observer;
        }

        @Override
        public void onNext(JsonValue instance) {
            if(instance.hasKey(field)) {
                observer.onNext(convert(instance.get(field)));
            } else {
                observer.onNext(null);
            }
        }

        protected abstract FieldValue convert(JsonValue jsonElement);

        @Override
        public void done() {
            observer.done();
        }
    }
    
    private static class TextFieldBinding extends FieldBinding {

        public TextFieldBinding(String field, CursorObserver<FieldValue> observer) {
            super(field, observer);
        }

        @Override
        protected FieldValue convert(JsonValue jsonElement) {
            return TextValue.valueOf(jsonElement.asString());
        }
    }
    
    private static class GeoAreaFieldBinding extends FieldBinding {

        public GeoAreaFieldBinding(String field, CursorObserver<FieldValue> observer) {
            super(field, observer);
        }

        @Override
        protected FieldValue convert(JsonValue jsonElement) {
            JsonValue array = jsonElement.get("extents");
            Extents extents = Extents.create(
                    array.get(0).asNumber(),
                    array.get(1).asNumber(),
                    array.get(2).asNumber(),
                    array.get(3).asNumber());
            
            return new GeoArea(extents);
        }
    }
    
    private static class ReferenceFieldBinding extends FieldBinding {

        private ResourceId formId;

        public ReferenceFieldBinding(String field, CursorObserver<FieldValue> observer) {
            super(field, observer);
        }

        @Override
        protected FieldValue convert(JsonValue jsonElement) {
            return new ReferenceValue(new RecordRef(formId, ResourceId.valueOf(jsonElement.asString())));
        }
    }
    
}
