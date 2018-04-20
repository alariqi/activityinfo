package org.activityinfo.ui.client.base.button;

import com.sencha.gxt.widget.core.client.button.CellButtonBase;
import com.sencha.gxt.widget.core.client.menu.Menu;


public class MenuButton extends CellButtonBase<String> {

    public MenuButton(String label, Menu menu) {
        super(new MenuButtonCell(), label);
        setMenu(menu);
    }
}
