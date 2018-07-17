package org.activityinfo.ui.client.importer.viewModel;

import org.activityinfo.i18n.shared.I18N;

/**
 * View model, with results of the matching exercise
 */
public class MappedSourceColumn {


    public enum Status {
        UNSET,
        IGNORED,
        MAPPED;
    }

    private SourceColumn column;
    private Status status;
    private String statusLabel;


    public MappedSourceColumn(SourceColumn sourceColumn, Status status) {
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
    }


    public MappedSourceColumn(SourceColumn column, String matchedLabel) {
        this.column = column;
        this.status = Status.MAPPED;
        this.statusLabel = matchedLabel;
    }

    public SourceColumn getColumn() {
        return column;
    }

    public Status getStatus() {
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
}
