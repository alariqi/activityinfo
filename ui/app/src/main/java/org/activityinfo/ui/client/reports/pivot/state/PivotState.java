package org.activityinfo.ui.client.reports.pivot.state;

import org.activityinfo.model.analysis.pivot.ImmutablePivotModel;
import org.activityinfo.model.analysis.pivot.PivotModel;
import org.activityinfo.ui.client.reports.formSelection.state.FormPath;

import java.util.function.Function;

public class PivotState {

    private String title;
    private PivotModel model;
    private FormPath formSelection;
    private DesignPanelState panelState;

    public PivotState() {
        this.title = null;
        this.model = ImmutablePivotModel.builder().build();
        this.formSelection = new FormPath();
        this.panelState = DesignPanelState.FORM_SELECTION;
    }

    private PivotState(PivotState toCopy) {
        this.title = toCopy.title;
        this.model = toCopy.model;
        this.formSelection = toCopy.formSelection;
        this.panelState = toCopy.panelState;
    }

    public String getTitle() {
        return title;
    }

    public PivotModel getModel() {
        return model;
    }

    public FormPath getFormSelection() {
        return formSelection;
    }

    public DesignPanelState getPanelState() {
        return panelState;
    }

    public PivotState withFormSelection(FormPath updatedModel) {
        PivotState newState = new PivotState(this);
        newState.formSelection = updatedModel;
        return newState;
    }

    public PivotState updateModel(Function<PivotModel, PivotModel> function) {
        PivotState updatedState = new PivotState(this);
        updatedState.model = function.apply(model);

        return updatedState;
    }
}
