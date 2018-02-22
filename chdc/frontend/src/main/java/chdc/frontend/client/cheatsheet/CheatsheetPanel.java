package chdc.frontend.client.cheatsheet;

import chdc.frontend.client.theme.CssLayoutContainer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.ui.client.lookup.viewModel.LookupKeyViewModel;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A panel containing a column for each lookup key.
 */
public class CheatsheetPanel implements IsWidget {

    private final CssLayoutContainer container;
    private final List<CheatsheetColumn> columns = new ArrayList<>();

    public CheatsheetPanel(LookupViewModel viewModel) {

        container = new CssLayoutContainer();
        container.setStyleName(CheatsheetAppearance.STYLE.cheatsheet());

        for (LookupKeyViewModel key : viewModel.getLookupKeys()) {
            CheatsheetColumn column = new CheatsheetColumn(viewModel, key);
            columns.add(column);
            container.add(column);
        }
    }

    public void focus() {
        columns.get(0).focus();
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    public CheatsheetColumn getLeafColumn() {
        return columns.get(columns.size() - 1);
    }

}
