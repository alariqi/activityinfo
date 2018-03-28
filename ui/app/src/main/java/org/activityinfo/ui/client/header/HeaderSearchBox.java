package org.activityinfo.ui.client.header;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class HeaderSearchBox implements IsWidget {

    private SimpleComboBox<String> comboBox;

    public HeaderSearchBox() {
        comboBox = new SimpleComboBox<>(item -> item);
        comboBox.setEmptyText("Database name, form name...");
        comboBox.getStore().add("CHMP Database");
        comboBox.getStore().add("Iraq IDP Response 2018");
        comboBox.getStore().add("Cluster NFI");
    }

    @Override
    public Widget asWidget() {
        return comboBox;
    }
}
