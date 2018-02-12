package chdc.frontend.client.theme;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface ChdcResources extends ClientBundle {

    ChdcResources INSTANCE = GWT.create(ChdcResources.class);

    @Source({"body.gss", "banner.gss", "button.gss", "icons.gss"})
    ChdcStyles styles();

}
