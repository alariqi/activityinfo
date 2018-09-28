package org.activityinfo.ui.client.reports.pivot.state;

import org.activityinfo.ui.client.reports.formSelection.state.FormSelectionUpdater;

import java.util.function.Function;

public interface PivotUpdater extends FormSelectionUpdater {

    void update(Function<PivotState, PivotState> function);

}
