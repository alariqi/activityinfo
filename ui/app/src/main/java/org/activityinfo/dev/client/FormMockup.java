package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import org.activityinfo.theme.client.CssLayoutContainer;

public class FormMockup implements IsWidget {

    private final CssLayoutContainer container = new CssLayoutContainer();

    public FormMockup() {
        CheckBox checkbox = new CheckBox();
        checkbox.setBoxLabel("This is a checkbox");

        container.add(new ComponentPanel("Checkbox", checkbox));
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
