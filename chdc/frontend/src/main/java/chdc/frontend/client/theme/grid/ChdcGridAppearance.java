package chdc.frontend.client.theme.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.theme.triton.client.base.grid.Css3GridAppearance;
import com.sencha.gxt.widget.core.client.grid.GridView;

public class ChdcGridAppearance extends Css3GridAppearance {


    public interface Resources extends GridResources  {

        @Source({ "grid.gss" })
        @CssResource.Import(GridView.GridStateStyles.class)
        Styles css();
    }

    public interface Styles extends GridStyle {
    }

    public ChdcGridAppearance() {
        super(GWT.create(Resources.class));
    }
}
