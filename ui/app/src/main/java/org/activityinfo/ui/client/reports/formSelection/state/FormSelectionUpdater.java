package org.activityinfo.ui.client.reports.formSelection.state;

import java.util.function.Function;

public interface FormSelectionUpdater {

    void updateFormSelection(Function<FormSelectionState, FormSelectionState> function);
}
