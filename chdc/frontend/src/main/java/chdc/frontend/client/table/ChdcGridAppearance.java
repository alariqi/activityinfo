package chdc.frontend.client.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.theme.triton.client.base.grid.Css3GridAppearance;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;
import com.sencha.gxt.widget.core.client.grid.GridView;

public class ChdcGridAppearance extends Css3GridAppearance {

    public interface ChdcResources extends ClientBundle {

        @Source("ChdcGrid.gss")
        @CssResource.Import(GridView.GridStateStyles.class)
        GridStyle css();

    }
    private static class GridResources implements Css3GridAppearance.GridResources {

        private static final ChdcResources RESOURCES = GWT.create(ChdcResources.class);


        @Override
        public GridStyle css() {
            return RESOURCES.css();
        }

        @Override
        public ThemeDetails theme() {
            return GWT.create(ThemeDetails.class);
        }
    }


    public ChdcGridAppearance() {
        super(new GridResources());
    }


}
