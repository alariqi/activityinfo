package org.activityinfo.ui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface ThemeBundle extends ClientBundle {

    ThemeBundle INSTANCE = GWT.create(ThemeBundle.class);

    @Source("icons.svg")
    TextResource icons();
}
