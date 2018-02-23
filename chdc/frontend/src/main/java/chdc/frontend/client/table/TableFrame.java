package chdc.frontend.client.table;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import org.activityinfo.ui.client.store.FormStore;

/**
 * Outer frame for the table interface, which includes the top
 * navigation bar and the grid itself.
 */
public class TableFrame implements IsWidget {

    private final TableBanner banner;
    private final IncidentGrid grid;
    private final BorderLayoutContainer container;

    public TableFrame(FormStore formSource) {

        banner = new TableBanner();

        grid = new IncidentGrid(formSource, banner);

        BorderLayoutContainer.BorderLayoutData gridLayout = new BorderLayoutContainer.BorderLayoutData();
        gridLayout.setMargins(new Margins(36, 0, 0, 24));

        container = new BorderLayoutContainer();
        container.setNorthWidget(banner, new BorderLayoutContainer.BorderLayoutData(55));
        container.setCenterWidget(grid, gridLayout);


    }

    @Override
    public Widget asWidget() {
        return container;
    }

}