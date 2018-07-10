package org.activityinfo.ui.client.fields.model;

import org.activityinfo.model.resource.ResourceId;

public interface FieldChoiceUpdater {

    void navigateToForm(int columnIndex, ResourceId itemId);

    void selectForm(ResourceId id);

    void designMode(DesignMode visible);

    void expandPanel(boolean expanded);
}
