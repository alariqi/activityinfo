package org.activityinfo.ui.client.table.model;

import org.activityinfo.model.analysis.ImmutableTableAnalysisModel;
import org.activityinfo.model.analysis.TableAnalysisModel;
import org.activityinfo.model.annotation.AutoBuilder;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;

import java.util.Optional;

@AutoBuilder
public class TableModel {


    TableAnalysisModel analysisModel;

    /**
     * Selected record, if any
     */
    private Optional<RecordRef> selected = Optional.empty();

    /**
     * True if the side panel is expanded
     */
    boolean recordPanelExpanded;

    /**
     * True if the column selection panel is selected.
     */
    boolean columnSelectionExpanded;

    private TableModel() {
    }

    public TableModel(ResourceId id) {
        this.analysisModel = ImmutableTableAnalysisModel.builder()
                .formId(id)
                .build();
    }

    public ResourceId getFormId() {
        return analysisModel.getFormId();
    }


    public Optional<RecordRef> getSelected() {
        return selected;
    }

    public boolean isRecordPanelExpanded() {
        return recordPanelExpanded;
    }

    public boolean isColumnSelectionExpanded() {
        return columnSelectionExpanded;
    }

    public TableAnalysisModel getAnalysisModel() {
        return analysisModel;
    }

    public TableModel withSelection(RecordRef selectedRef) {
        TableModel updatedModel = new TableModel();
        updatedModel.analysisModel = this.analysisModel;
        updatedModel.selected = Optional.of(selectedRef);
        updatedModel.recordPanelExpanded = this.recordPanelExpanded;
        updatedModel.columnSelectionExpanded = this.columnSelectionExpanded;
        return updatedModel;
    }
}

