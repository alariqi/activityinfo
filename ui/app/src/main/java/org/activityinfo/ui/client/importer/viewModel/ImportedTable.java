package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.json.Json;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.form.FormRecord;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldImporter;

import java.util.Iterator;
import java.util.List;

public class ImportedTable {
    private final ResourceId formId;
    private final ValidRowSet validRowSet;
    private final List<FieldImporter> fieldImporters;

    public ImportedTable(ResourceId formId, ValidRowSet validRowSet, List<FieldImporter> fieldImporters) {
        this.formId = formId;
        this.validRowSet = validRowSet;
        this.fieldImporters = fieldImporters;
    }

    public Iterator<FormRecord> getRecords() {
        return new RecordIterator();
    }

    private class RecordIterator implements Iterator<FormRecord> {

        private int nextRowIndex;

        public RecordIterator() {
            this.nextRowIndex = -1;
            advanceToNextValidRow();
        }

        @Override
        public boolean hasNext() {
            return nextRowIndex < validRowSet.getNumRows();
        }

        @Override
        public FormRecord next() {

            ResourceId recordId = ResourceId.generateSubmissionId(formId);

            JsonValue fields = Json.createObject();
            for (FieldImporter fieldImporter : fieldImporters) {
                fields.put(fieldImporter.getFieldName(), fieldImporter.getValue(nextRowIndex).toJson());
            }

            FormRecord record = new FormRecord(new RecordRef(formId, recordId), null, fields);

            // Find the next valid row
            advanceToNextValidRow();

            return record;
        }

        private void advanceToNextValidRow() {
            while(nextRowIndex < validRowSet.getNumRows()) {
                nextRowIndex++;
                if(validRowSet.isValid(nextRowIndex)) {
                    break;
                }
            }
        }
    }

}
