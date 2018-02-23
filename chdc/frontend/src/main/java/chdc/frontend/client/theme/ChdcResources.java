package chdc.frontend.client.theme;

import com.google.gwt.resources.client.ClientBundle;

public interface ChdcResources extends ClientBundle {

    @Source({ /* Order is important! */
            "body.gss",
            "headings.gss",
            "button.gss",
            "banner.gss",
            "icons.gss",
            "panel.gss",
            "text.gss",
            "datasheet.gss"})
    ChdcStyles style();

}
