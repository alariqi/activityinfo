package chdc.frontend.client.theme.grid;

import chdc.theme.client.base.grid.Css3ColumnHeaderAppearance;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

public class ChdcColumnHeaderAppearance extends Css3ColumnHeaderAppearance {

    public interface Resources extends Css3ColumnHeaderAppearance.Css3ColumnHeaderResources  {
        @ClientBundle.Source({  "columnheader.gss"})
        ChdcStyles style();
    }

    public interface ChdcStyles extends Css3ColumnHeaderAppearance.Styles {
    }

    public ChdcColumnHeaderAppearance() {
        super(GWT.create(Resources.class));
    }
}
