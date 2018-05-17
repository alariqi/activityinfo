package org.activityinfo.dev.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface DevBundle extends ClientBundle {

    DevBundle RESOURCES = GWT.create(DevBundle.class);

    @Source("Dev.gss")
    Style style();

    interface Style extends CssResource {

        String component();

        String forms();
    }
}
