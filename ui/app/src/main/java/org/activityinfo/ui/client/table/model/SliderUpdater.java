package org.activityinfo.ui.client.table.model;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.input.view.InputHandler;

public interface SliderUpdater {

    TableUpdater getTableUpdater(ResourceId formId);

    InputHandler getInputHandler();

}
