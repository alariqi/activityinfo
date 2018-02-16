package chdc.frontend.client.cheatsheet;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import org.activityinfo.ui.client.lookup.viewModel.LookupKeyViewModel;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;

/**
 * A panel containing a column for each lookup key.
 */
public class CheatsheetPanel implements IsWidget {

    private final HBoxLayoutContainer container;

    public CheatsheetPanel(LookupViewModel viewModel) {

        container = new HBoxLayoutContainer();
        container.setHBoxLayoutAlign(HBoxLayoutContainer.HBoxLayoutAlign.STRETCH);

        for (LookupKeyViewModel key : viewModel.getLookupKeys()) {
            container.add(new CheatsheetColumn(key), new BoxLayoutContainer.BoxLayoutData());
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
