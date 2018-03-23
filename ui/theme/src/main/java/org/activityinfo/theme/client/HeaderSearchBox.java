package org.activityinfo.theme.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class HeaderSearchBox<T> implements IsWidget {


    private final SimpleComboBox<String> comboBox;

    public HeaderSearchBox() {
        comboBox = new SimpleComboBox<>(item -> item);
        comboBox.setEmptyText("Database name, form name...");
    }

    @Override
    public Widget asWidget() {
        return comboBox;
    }
}
