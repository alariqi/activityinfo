package chdc.frontend.client.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * The top banner widget that includes navigation links as well as
 * buttons for the adding and removing incidents from the table.
 */
public class TableBanner extends Composite {
    interface TopBarUiBinder extends UiBinder<HTMLPanel, TableBanner> {
    }

    private static TopBarUiBinder ourUiBinder = GWT.create(TopBarUiBinder.class);

    public TableBanner() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}