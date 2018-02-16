package chdc.frontend.client.theme;

import chdc.theme.client.base.panel.Css3HeaderAppearance;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public class ChdcHeaderAppearance extends Css3HeaderAppearance {

    public interface ChdcHeaderResources extends Css3HeaderResources {

        @Override
        @ClientBundle.Source({"com/sencha/gxt/theme/base/client/widget/Header.gss", "header.gss"})
        ChdcHeaderStyle style();

    }

    public interface ChdcHeaderStyle extends Css3HeaderStyle {

    }

    public ChdcHeaderAppearance() {
        super(GWT.create(ChdcHeaderResources.class));
    }
}
