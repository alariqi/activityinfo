package org.activityinfo.theme.client;

import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.widget.core.client.button.CellButtonBase;

public class ListToolBarButton extends CellButtonBase<String> {

    /**
     * Creates a new text button.
     *
     * @param cell the button cell
     * @param text the button's text
     */
    public ListToolBarButton(Icon icon, String text) {
        super(new TextButtonCell(), text);
        setText(text);
    }

}
