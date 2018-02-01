package chdc.frontend.client.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.theme.triton.client.base.grid.Css3GridAppearance;
import com.sencha.gxt.widget.core.client.grid.GridView;

public class ChdcGridAppearance extends Css3GridAppearance {

    public interface GridResources extends Css3GridAppearance.GridResources {

        @Source("ChdcGrid.gss")
        @CssResource.Import(GridView.GridStateStyles.class)
        GridStyle css();
    }


    public ChdcGridAppearance() {
        super(GWT.create(GridResources.class));
    }


}
