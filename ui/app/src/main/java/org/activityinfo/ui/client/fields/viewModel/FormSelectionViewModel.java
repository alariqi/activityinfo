package org.activityinfo.ui.client.fields.viewModel;

import org.activityinfo.observable.Observable;

import java.util.List;

public class FormSelectionViewModel {

    private final List<Observable<FormSelectionColumn>> columns;

    FormSelectionViewModel(List<Observable<FormSelectionColumn>> columns) {
        this.columns = columns;
    }

    public List<Observable<FormSelectionColumn>> getColumns() {
        return columns;
    }

}