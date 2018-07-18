package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;

/**
 * View model, with results of the matching exercise
 */
public class MappedSourceColumn {

    private SourceColumn column;
    private ColumnStatus status;
    private String statusLabel;
    private Observable<Validation> validation;


    public MappedSourceColumn(SourceColumn sourceColumn, ColumnStatus status) {
        column = sourceColumn;
        this.status = status;
        switch (status) {
            case UNSET:
                this.statusLabel = I18N.CONSTANTS.unset();
                break;
            case IGNORED:
                this.statusLabel = I18N.CONSTANTS.ignored();
                break;
            case MAPPED:
                throw new IllegalStateException("Expected label");
        }
        this.validation = Observable.just(NoValidation.NONE);
    }


    public MappedSourceColumn(SourceColumn column, String matchedLabel, Observable<Validation> validation) {
        this.column = column;
        this.status = ColumnStatus.MAPPED;
        this.statusLabel = matchedLabel;
        this.validation = validation;
    }

    public SourceColumn getColumn() {
        return column;
    }

    public ColumnStatus getStatus() {
        return status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public String getLabel() {
        return column.getLabel();
    }

    public String getId() {
        return column.getId();
    }

    public Observable<Validation> getValidation() {
        return validation;
    }
}
