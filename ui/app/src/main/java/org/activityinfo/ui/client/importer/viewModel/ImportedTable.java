package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.io.csv.CsvWriter;
import org.activityinfo.json.Json;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.form.FormRecord;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.ui.client.importer.viewModel.fields.FieldImporter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class ImportedTable {
    private final ResourceId formId;
    private final ValidatedTable validatedTable;
    private final ValidRowSet validRowSet;
    private final List<FieldImporter> fieldImporters;

    public ImportedTable(ResourceId formId, ValidatedTable validatedTable, ValidRowSet validRowSet, List<FieldImporter> fieldImporters) {
        this.formId = formId;
        this.validatedTable = validatedTable;
        this.validRowSet = validRowSet;
        this.fieldImporters = fieldImporters;
    }

    public Iterator<FormRecord> getRecords() {
        return new RecordIterator();
    }

    public int getValidRecordCount() {
        return validRowSet.getValidRowCount();
    }

    public int getInvalidRecordCount() {
        return validRowSet.getInvalidRowCount();
    }

    public ValidatedTable getValidatedTable() {
        return validatedTable;
    }

    public ValidRowSet getValidRowSet() {
        return validRowSet;
    }

    public String invalidCsv() {
        try {
            CsvWriter writer = new CsvWriter();
            int numColumns = validatedTable.getNumColumns();
            Object[] columns = new String[numColumns];
            ColumnView[] columnViews = validatedTable.getColumnViews();

            // Write headers
            for (int j=0;j<numColumns;++j) {
                columns[j] = validatedTable.getColumns().get(j).getColumn().getLabel();
            }
            writer.writeLine(columns);

            // Write invalid rows
            int[] invalidMap = validRowSet.buildInvalidMap();
            for (int i = 0; i < invalidMap.length; i++) {
                int rowIndex = invalidMap[i];
                for (int j=0;j<numColumns;++j) {
                    columns[j] = columnViews[j].getString(rowIndex);
                }
                writer.writeLine(columns);
            }
            return writer.toString();

        } catch (IOException e) {
            // Should not happen.
            return null;
        }
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
