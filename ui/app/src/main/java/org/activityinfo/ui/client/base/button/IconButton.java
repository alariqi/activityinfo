package org.activityinfo.ui.client.base.button;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.widget.core.client.cell.CellComponent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import org.activityinfo.ui.client.Icon;

public class IconButton extends CellComponent<String> implements SelectEvent.HasSelectHandlers {

    public IconButton(Icon icon, IconButtonStyle style, String text) {
        super(new ButtonCell<>(new IconButtonAppearance(icon, style)), text, null, true);
    }

    public IconButton(Icon icon, String text) {
        this(icon, IconButtonStyle.SECONDARY, text);
    }

    @Override
    public HandlerRegistration addSelectHandler(SelectEvent.SelectHandler handler) {
        return addHandler(handler, SelectEvent.getType());
    }
}
