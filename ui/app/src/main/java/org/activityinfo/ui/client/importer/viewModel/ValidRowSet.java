package org.activityinfo.ui.client.importer.viewModel;

import java.util.BitSet;

public class ValidRowSet {

    private final int numRows;
    private BitSet invalidRows = new BitSet();

    private boolean empty = true;

    public ValidRowSet(int numRows, Validation[] validation) {
        this.numRows = numRows;
        for (int i = 0; i < numRows; i++) {

            for (int j = 0; j < validation.length; j++) {
                if (validation[j].getRowStatus(j) == Validation.INVALID) {
                    invalidRows.set(j);
                    break;
                }
            }
            empty = false;
        }
    }

    public boolean isValid(int rowIndex) {
        return !invalidRows.get(rowIndex);
    }

    public boolean isEmpty() {
        return empty;
    }

    public int getNumRows() {
        return numRows;
    }
}
