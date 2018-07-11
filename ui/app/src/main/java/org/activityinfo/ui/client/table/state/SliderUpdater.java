package org.activityinfo.ui.client.table.state;

import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.ui.client.input.view.InputHandler;

import java.util.function.Function;

public interface SliderUpdater {

    void update(Function<SliderState, SliderState> function);

    TableUpdater getTableUpdater(ResourceId formId);

    InputHandler getInputHandler();

}
