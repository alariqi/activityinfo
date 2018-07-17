package org.activityinfo.store.testing;

import com.google.common.base.Supplier;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.form.FormRecord;
import org.activityinfo.model.resource.ResourceId;

import java.util.ArrayList;
import java.util.List;

public class JsonTestForm implements TestForm {

    private final FormClass schema;
    private final List<FormInstance> records = new ArrayList<>();

    public JsonTestForm(JsonValue jsonObject) {
        this.schema = FormClass.fromJson(jsonObject.get("schema"));

        JsonValue recordsArray = jsonObject.get("records");
        for (int i = 0; i < recordsArray.length(); i++) {
            JsonValue record = recordsArray.get(i);
            records.add(FormInstance.toFormInstance(schema, FormRecord.fromJson(record)));
        }
    }

    @Override
    public ResourceId getFormId() {
        return schema.getId();
    }

    @Override
    public FormClass getFormClass() {
        return schema;
    }

    @Override
    public List<FormInstance> getRecords() {
        return records;
    }

    @Override
    public Supplier<FormInstance> getGenerator() {
        throw new UnsupportedOperationException("TODO");
    }
}
