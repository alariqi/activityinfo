package org.activityinfo.ui.client.fields.state;

import java.util.function.Function;

public interface FieldChoiceUpdater {

    void update(Function<FieldChoiceState, FieldChoiceState> function);

    void drop(DropTarget dropTarget);
}
