package chdc.frontend.client.cheatsheet;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import org.activityinfo.ui.client.lookup.viewModel.LookupKeyViewModel;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;

/**
 * A panel containing a column for each lookup key.
 */
public class CheatsheetPanel implements IsWidget {

    private final FlowLayoutContainer container;

    public CheatsheetPanel(LookupViewModel viewModel) {

        container = new FlowLayoutContainer();
        container.setStyleName(CheatsheetAppearance.STYLE.cheatsheet());

        for (LookupKeyViewModel key : viewModel.getLookupKeys()) {
            container.add(new CheatsheetColumn(key));
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
