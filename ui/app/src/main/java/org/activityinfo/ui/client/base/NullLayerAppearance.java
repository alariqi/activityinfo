package org.activityinfo.ui.client.base;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.Layer;

/**
 * Removes Sencha's drop shadow styling.
 */
public class NullLayerAppearance implements Layer.LayerAppearance {
    @Override
    public int getShadowOffset() {
        return 0;
    }

    @Override
    public void renderShadow(SafeHtmlBuilder sb) {
        // Requires placeholder to avoid screwing up other code
        sb.appendHtmlConstant("<div><div><div></div><div></div><div></div></div><div><div></div><div></div><div></div></div><div><div></div><div></div><div></div></div></div>");
    }

    @Override
    public String shimClass() {
        return "layer_shim";
    }
}
