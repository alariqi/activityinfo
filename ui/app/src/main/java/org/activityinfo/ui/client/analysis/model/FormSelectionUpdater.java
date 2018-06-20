package org.activityinfo.ui.client.analysis.model;

import org.activityinfo.model.resource.ResourceId;

public interface FormSelectionUpdater {

    void navigateTo(int columnIndex, ResourceId itemId);

    void select(ResourceId id);
}
