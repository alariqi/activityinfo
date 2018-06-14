package org.activityinfo.ui.client.base.info;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class Alert implements IsWidget {

    public enum Type {
        ERROR,
        WARNING
    }

    private Label label;

    public Alert() {
        label = new Label();
        label.addStyleName("alert");
    }

    public Alert(Type type, String message) {
        this();
        setType(type);
        setMessage(message);
    }

    public void setMessage(String message) {
        label.setText(message);
    }

    public void setType(Type type) {
        label.setStyleName("alert--error", type == Type.ERROR);
        label.setStyleName("alert--warning", type == Type.WARNING);
    }

    public void setVisible(boolean visible) {
        label.setVisible(visible);
    }

    @Override
    public Widget asWidget() {
        return label;
    }
}
