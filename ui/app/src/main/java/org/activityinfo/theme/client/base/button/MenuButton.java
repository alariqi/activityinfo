package org.activityinfo.theme.client.base.button;

import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.widget.core.client.button.CellButtonBase;
import com.sencha.gxt.widget.core.client.menu.Menu;


public class MenuButton extends CellButtonBase<String> {

    public MenuButton(String label, Menu menu) {
        super(new ButtonCell<>(new MenuButtonAppearance()), label);
        setMenu(menu);
    }
}
