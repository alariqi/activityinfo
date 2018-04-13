package org.activityinfo.theme.client.base.button;

import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.widget.core.client.button.CellButtonBase;
import org.activityinfo.theme.client.Icon;

public class IconButton extends CellButtonBase<String> {

    public IconButton(Icon icon, IconButtonStyle style, String text) {
        super(new ButtonCell<>(new IconButtonAppearance(icon, style)), text);
    }

    public IconButton(Icon icon, String text) {
        this(icon, IconButtonStyle.SECONDARY, text);
    }
}
