package chdc.frontend.client.cheatsheet;

import chdc.frontend.client.theme.ChdcTemplates;
import chdc.frontend.client.theme.ChdcTheme;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;

public class CheatsheetDialog {


    private final HtmlLayoutContainer container;

    public CheatsheetDialog(LookupViewModel viewModel) {


        CheatsheetPanel panel = new CheatsheetPanel(viewModel);

        container = new HtmlLayoutContainer(ChdcTemplates.TEMPLATES.panel(ChdcTheme.STYLES));
        container.add(panel, new AbstractHtmlLayoutContainer.HtmlData("." + ChdcTheme.STYLES.panelContent()));

    }

    public void show() {
        RootPanel.get().add(container);

    }
}
