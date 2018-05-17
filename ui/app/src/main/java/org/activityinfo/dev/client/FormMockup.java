package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;

public class FormMockup implements IsWidget {

    private final CssLayoutContainer container;

    public FormMockup() {
        CheckBox checkbox = new CheckBox();
        checkbox.setBoxLabel("This is a checkbox");

        TextField textField = new TextField();

        TextField readOnlyTextField = new TextField();
        readOnlyTextField.setReadOnly(true);

        CssLayoutContainer inner = new CssLayoutContainer("form");
        inner.add(checkbox);
        inner.add(textField);
        inner.add(readOnlyTextField);

        container = new CssLayoutContainer();
        container.addStyleName(DevBundle.RESOURCES.style().forms());
        container.add(inner);

    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
