package org.activityinfo.ui.client.table.state;

import org.activityinfo.model.analysis.ImmutableTableAnalysisModel;
import org.activityinfo.model.analysis.TableAnalysisModel;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.ui.client.fields.state.FieldChoiceState;

import java.util.Optional;
import java.util.function.Function;

public class TableState {

    TableAnalysisModel analysisModel;

    /**
     * Selected record, if any
     */
    private Optional<RecordRef> selected = Optional.empty();

    /**
     * True if the side panel is expanded
     */
    boolean recordPanelExpanded;

    FieldChoiceState columnOptions = new FieldChoiceState(false);

    TableState() {
    }

    private TableState(TableState from) {
        this.analysisModel = from.analysisModel;
        this.selected = from.selected;
        this.recordPanelExpanded = from.recordPanelExpanded;
        this.columnOptions = from.columnOptions;
    }

    public TableState(ResourceId id) {
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
        return columnOptions.isExpanded();
    }

    public FieldChoiceState getColumnOptions() {
        return columnOptions;
    }

    public TableAnalysisModel getAnalysisModel() {
        return analysisModel;
    }


    public TableState withSelection(RecordRef selectedRef) {

        if(this.selected.isPresent() && this.selected.get().equals(selectedRef)) {
            return this;
        }

        TableState updatedModel = new TableState(this);
        updatedModel.selected = Optional.of(selectedRef);
        return updatedModel;
    }

    public TableState withRecordPanelExpanded(boolean expanded) {
        if (this.recordPanelExpanded == expanded) {
            return this;
        }
        TableState updatedModel = new TableState(this);
        updatedModel.recordPanelExpanded = expanded;
        return updatedModel;
    }

    public TableState updateColumnOptions(Function<FieldChoiceState, FieldChoiceState> function) {
        FieldChoiceState updatedColumnOptions = function.apply(this.columnOptions);
        if(this.columnOptions == updatedColumnOptions) {
            return this;
        }
        TableState updatedTable = new TableState(this);
        updatedTable.columnOptions = updatedColumnOptions;
        return updatedTable;
    }

    public TableState withAnalysisModel(TableAnalysisModel analysisModel) {
        TableState updatedModel = new TableState(this);
        updatedModel.analysisModel = analysisModel;
        return updatedModel;
    }

    public TableState withColumnWidth(String columnId, int newWidth) {
        return this;
    }
}

