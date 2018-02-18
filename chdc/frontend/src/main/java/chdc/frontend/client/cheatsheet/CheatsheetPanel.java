package chdc.frontend.client.cheatsheet;

import chdc.frontend.client.theme.CssLayoutContainer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.lookup.viewModel.LookupKeyViewModel;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;

/**
 * A panel containing a column for each lookup key.
 */
public class CheatsheetPanel implements IsWidget {

    private final CssLayoutContainer container;

    public CheatsheetPanel(LookupViewModel viewModel) {

        container = new CssLayoutContainer();
        container.setStyleName(CheatsheetAppearance.STYLE.cheatsheet());

        for (LookupKeyViewModel key : viewModel.getLookupKeys()) {
            container.add(new CheatsheetColumn(viewModel, key));
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
