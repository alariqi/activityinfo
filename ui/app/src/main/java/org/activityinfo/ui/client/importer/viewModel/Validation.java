package org.activityinfo.ui.client.importer.viewModel;

public interface Validation {

    int VALID = 0;
    int INVALID = 1;
    int VALIDATING = 2;

    int getRowStatus(int rowIndex);

    boolean isDone();
}
