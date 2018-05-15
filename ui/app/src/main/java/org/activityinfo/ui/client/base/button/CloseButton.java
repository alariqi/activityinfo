package org.activityinfo.ui.client.base.button;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.widget.core.client.cell.CellComponent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * A simple button with a white X symbol without any additional CSS classes.
 */
public class CloseButton extends CellComponent<String> implements SelectEvent.HasSelectHandlers {
    public CloseButton() {
        super(new ButtonCell<>(new CloseButtonAppearance()), "", null, false);
    }

    @Override
    public HandlerRegistration addSelectHandler(SelectEvent.SelectHandler handler) {
        return addHandler(handler, SelectEvent.getType());
    }
}
