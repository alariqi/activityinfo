package org.activityinfo.theme.client.base.button;

import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.widget.core.client.button.CellButtonBase;
import org.activityinfo.theme.client.Icon;

public class IconButton extends CellButtonBase<String> {

    public IconButton(Icon icon, String text) {
        super(new ButtonCell<>(new IconButtonAppearance(icon)), text);
    }
}
