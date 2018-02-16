package chdc.frontend.client.theme;

import chdc.theme.client.base.window.Css3WindowAppearance;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public class ChdcWindowAppearance extends Css3WindowAppearance {

    public interface ChdcWindowResources extends Css3WindowAppearance.Css3WindowResources {

        @Override
        @ClientBundle.Source("Css3Window.gss")
        Css3WindowStyle style();
    }

    public interface ChdcWindowStyle extends Css3WindowStyle {
    }

    public ChdcWindowAppearance() {
        super(GWT.create(ChdcWindowResources.class));
    }
}
