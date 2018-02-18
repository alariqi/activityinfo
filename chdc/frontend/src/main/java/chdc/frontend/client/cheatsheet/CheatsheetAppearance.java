package chdc.frontend.client.cheatsheet;

import chdc.theme.client.base.listview.Css3ListViewAppearance;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.sencha.gxt.themebuilder.base.client.config.ThemeDetails;

/**
 * Overrides the CSS styles used by the Sencha {@link com.sencha.gxt.widget.core.client.ListView} component.
 *
 */
public class CheatsheetAppearance extends Css3ListViewAppearance<String> {


    private static final Resources RESOURCES = GWT.create(Resources.class);

    public static final Style STYLE = RESOURCES.css();

    public interface Resources extends Css3ListViewResources  {
        @ClientBundle.Source("Cheatsheet.gss")
        Style css();

        ThemeDetails theme();
    }

    public interface Style extends Css3ListViewStyle {

        String cheatsheet();
    }

    public CheatsheetAppearance() {
        super(RESOURCES);
    }
}
