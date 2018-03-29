package org.activityinfo.theme.client;

import com.google.gwt.dom.client.StyleInjector;

public class Theme {

    public static void injectStyles() {
        StyleInjector.inject(Resources.RESOURCES.styles().getText());
    }
}
