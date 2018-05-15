package org.activityinfo.ui.client.nonideal;

import com.google.gwt.user.client.ui.IsWidget;

public interface ViewWidget<T> extends IsWidget {

    void updateView(T viewModel);

    void setVisible(boolean visible);
}
