package org.activityinfo.dev.client;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class MenuPage implements IsWidget {

    private final ComponentPanel panel;

    public MenuPage() {

        CheckMenuItem offlineNo = new CheckMenuItem("Available offline");
        CheckMenuItem offlineYes = new CheckMenuItem("Available offline");
        offlineYes.setChecked(true);

        Menu menu = new Menu();

        menu.add(new MenuItem("This is an action...", this::onAction));
        menu.add(new MenuItem("Do something else", this::onAction));
        menu.add(new MenuItem("Delete", this::onAction));

        menu.add(offlineNo);
        menu.add(offlineYes);

        panel = new ComponentPanel("Menu", menu);
    }

    private void onAction(SelectionEvent<MenuItem> event) {
        MessageBox box = new MessageBox("Selection", "You selected: " + event.getSelectedItem().getText());
        box.show();
    }

    @Override
    public Widget asWidget() {
        return panel.asWidget();
    }
}
