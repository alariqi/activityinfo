package org.activityinfo.ui.client.importer.viewModel.parser;

import org.activityinfo.model.query.ColumnView;
import org.activityinfo.observable.IncrementalTask;
import org.activityinfo.ui.client.importer.viewModel.Validation;

import java.util.BitSet;

public class SimpleValidator implements IncrementalTask<Validation> {

    private static class Result implements Validation {

        private final BitSet invalid = new BitSet();
        private int rowsValidated = 0;
        private boolean done;

        @Override
        public int getRowStatus(int rowIndex) {
            if(rowIndex < rowsValidated) {
                return invalid.get(rowIndex) ? INVALID : VALID;
            } else {
                return VALIDATING;
            }
        }

        @Override
        public boolean isDone() {
            return done;
        }
    }

    private final ColumnView columnView;
    private final FieldParser parser;
    private final int batchSize = 100;
    private final Result result = new Result();

    private int currentRow = 0;
    private int numRows;


    public SimpleValidator(ColumnView columnView, FieldParser parser) {
        this.columnView = columnView;
        this.parser = parser;
        this.currentRow = 0;
        this.numRows = columnView.numRows();
    }


    @Override
    public boolean execute() {

        int remaining = batchSize;
        while(currentRow < numRows && remaining > 0) {
            String value = columnView.getString(currentRow);
            if(value != null) {
                if(!parser.validate(value)) {
                    result.invalid.set(currentRow);
                }
            }
            remaining--;
            currentRow ++;
        }
        result.rowsValidated = currentRow;

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
    public Validation getValue() {
        return result;
    }
}
