package org.activityinfo.ui.client.component.importDialog;

import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import org.activityinfo.json.Json;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.form.FormMetadata;
import org.activityinfo.model.form.FormRecord;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.query.QueryModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.dispatch.ResourceLocatorAdaptor;

import java.io.File;
import java.io.IOException;

public class TestDataSetWriter {

    private final JsonValue forms;

    public TestDataSetWriter() {
        forms = Json.createArray();

    }


    public void addWithRecords(ResourceLocatorAdaptor locator, FormTree formTree) {
        for (FormMetadata form : formTree.getForms()) {
            if(form.isVisible()) {
                JsonValue formObject = Json.createObject();
                formObject.add("schema", form.getSchema().toJson());
                formObject.add("records", fetchRecords(locator, form.getSchema()));
                forms.add(formObject);
            }
        }
    }

    private JsonValue fetchRecords(ResourceLocatorAdaptor locator, FormClass schema) {
        QueryModel query = new QueryModel(schema.getId());
        query.selectRecordId().as("id");

        JsonValue array = Json.createArray();

        ColumnView ids = locator.queryTable(query).get().getColumnView("id");
        for (int i = 0; i < ids.numRows(); i++) {
            ResourceId recordId = ResourceId.valueOf(ids.getString(i));
            FormInstance record = locator.getFormInstance(schema.getId(), recordId).get();

            array.add(FormRecord.fromInstance(record).toJson());
        }

        return array;
    }

    public void write(String file) throws IOException {

        JsonValue dataset = Json.createObject();
        dataset.add("forms", forms);

        String json = Json.stringify(dataset, 2);

        CharSource.wrap(json).copyTo(com.google.common.io.Files.asCharSink(new File(file), Charsets.UTF_8));
    }

    public void writeExported(String file) throws IOException {

        JsonValue array = Json.createArray();

        for (FormInstance exportedRecord : PersistImportCommand.EXPORTED_RECORDS) {
            array.add(FormRecord.fromInstance(exportedRecord).toJson());
        }

        Files.asCharSink(new File(file), Charsets.UTF_8).write(Json.stringify(array, 2));

    }

}
