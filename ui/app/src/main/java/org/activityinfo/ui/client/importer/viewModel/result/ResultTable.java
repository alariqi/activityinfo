package org.activityinfo.ui.client.importer.viewModel.result;

import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.importer.viewModel.SourceViewModel;
import org.activityinfo.ui.client.importer.viewModel.fields.ImportedFieldViewModel;

import java.util.List;
import java.util.Optional;

public class ResultTable {

    public static Observable<ResultTable> compute(Observable<SourceViewModel> source, List<Observable<Optional<ImportedFieldViewModel>>> fieldMap) {
        return source.join(s -> compute(s, fieldMap));
    }

    public static Observable<ResultTable> compute(SourceViewModel source, List<Observable<Optional<ImportedFieldViewModel>>> fields) {
        return Observable.flatten(fields).join(list -> doCompute(source, list));
    }

    public static Observable<ResultTable> doCompute(SourceViewModel source, List<Optional<ImportedFieldViewModel>> fields) {
//        fields.get()
        throw new UnsupportedOperationException("TODO");
    }
}