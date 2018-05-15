package org.activityinfo.ui.client.base.button;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.widget.core.client.cell.CellComponent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

/**
 * A simple button with text content and no additional styles.
 */
public class PlainTextButton extends CellComponent<String> implements SelectEvent.HasSelectHandlers {

    public PlainTextButton(String text) {
        super(new ButtonCell<>(new ButtonAppearance<>("")), text, null, false);
    }

    @Override
    public HandlerRegistration addSelectHandler(SelectEvent.SelectHandler handler) {
        return addHandler(handler, SelectEvent.getType());
    }
}
