package chdc.frontend.client.theme;

import com.google.gwt.resources.client.ClientBundle;

public interface ChdcResources extends ClientBundle {

    @Source({ /* Order is important! */
            "body.gss",
            "button.gss",
            "actionbar.gss",
            "banner.gss",
            "icons.gss",
            "panel.gss"})
    ChdcStyles styles();

}
