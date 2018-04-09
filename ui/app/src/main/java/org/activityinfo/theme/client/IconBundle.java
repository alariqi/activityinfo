package org.activityinfo.theme.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface IconBundle extends ClientBundle {

    IconBundle ICONS = GWT.create(IconBundle.class);

    @Source("logo.svg")
    TextResource logo();

}
