package org.activityinfo.ui.client.fields.state;

import org.activityinfo.model.resource.ResourceId;

import java.util.function.Function;

public interface FieldChoiceUpdater {

    void navigateToForm(int columnIndex, ResourceId itemId);

    void selectForm(ResourceId id);

    void update(Function<FieldChoiceState, FieldChoiceState> function);


}
