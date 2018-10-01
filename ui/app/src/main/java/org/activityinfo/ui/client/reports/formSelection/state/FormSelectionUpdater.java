package org.activityinfo.ui.client.reports.formSelection.state;

import org.activityinfo.model.resource.ResourceId;

import java.util.Set;
import java.util.function.Function;

public interface FormSelectionUpdater {

    void updateFormSelection(Function<FormSelectionState, FormSelectionState> function);

    void selectForms(Set<ResourceId> forms, boolean select);
}
