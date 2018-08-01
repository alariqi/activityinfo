package org.activityinfo.ui.client.importer.viewModel.fields;

import com.google.common.base.Optional;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.query.ColumnView;
import org.activityinfo.observable.IncrementalTask;
import org.activityinfo.ui.client.importer.viewModel.SourceColumn;
import org.activityinfo.ui.client.importer.viewModel.Validation;
import org.activityinfo.ui.client.lookup.viewModel.LookupIndex;

public class ReferenceMatcher implements IncrementalTask<ReferenceMatcher.Result> {


    public class Result {
        private int rowsMatched;
        private boolean done;
        private String[] matchedIds;

        public boolean isDone() {
            return done;
        }

        public Validation getColumnValidation(int columnIndex) {
            return new Validation() {
                @Override
                public int getRowStatus(int rowIndex) {
                    if(rowIndex < rowsMatched) {
                        if(matchedIds[rowIndex] == null) {
                            return Validation.INVALID;
                        } else {
                            return Validation.VALID;
                        }
                    } else {
                        return Validation.VALIDATING;
                    }
                }

                @Override
                public boolean isDone() {
                    return done;
                }
            };
        }

        public Optional<FieldImporter> getImporterIfDone() {
            if(done) {
                return Optional.of(new ReferenceImporter(field.getName(), index.getFormId(), matchedIds));
            } else {
                return Optional.absent();
            }
        }
    }

    private Result result = new Result();
    private int batchSize = 20;
    private int currentRow = 0;
    private final FormField field;
    private final LookupIndex index;
    private int numRows;

    private ColumnView[] columns;
    private String[] keyBuffer;


    public ReferenceMatcher(FormField field, LookupIndex index, SourceColumn[] columns) {
        this.field = field;
        this.index = index;
        this.numRows = columns[0].getColumnView().numRows();
        this.keyBuffer = new String[columns.length];
        this.columns = new ColumnView[columns.length];
        for (int i = 0; i < columns.length; i++) {
            this.columns[i] = columns[i].getColumnView();
        }
        this.result.matchedIds = new String[numRows];
    }

    @Override
    public boolean execute() {

        int remaining = batchSize;
        while(currentRow < numRows && remaining > 0) {

            for (int i = 0; i < columns.length; i++) {
                keyBuffer[i] = columns[i].getString(currentRow);
            }

            result.matchedIds[currentRow] = index.lookup(keyBuffer);

            remaining--;
            currentRow ++;
        }
        result.rowsMatched = currentRow;

        if(currentRow == numRows) {
            result.done = true;
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean isLoading() {
        return false;
    }

    @Override
    public Result getValue() {
        return result;
    }
}
