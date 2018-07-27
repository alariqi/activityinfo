package org.activityinfo.ui.client.importer.viewModel;

public class NoValidation implements Validation {

    public static final NoValidation NONE = new NoValidation();

    private NoValidation() {}

    @Override
    public int getRowStatus(int rowIndex) {
        return VALID;
    }

    @Override
    public boolean isDone() {
        return true;
    }
}
