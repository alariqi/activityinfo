package chdc.frontend.client.table;

import chdc.frontend.client.theme.IconButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * The top banner widget that includes navigation links as well as
 * buttons for the adding and removing incidents from the table.
 */
public class TableBanner extends Composite {

    interface BannerUiBinder extends UiBinder<HTMLPanel, TableBanner> {
    }

    private static BannerUiBinder ourUiBinder = GWT.create(BannerUiBinder.class);

    @UiField
    IconButton saveButton;
    @UiField
    IconButton addButton;

    public TableBanner() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public IconButton getSaveButton() {
        return saveButton;
    }

    public IconButton getAddButton() {
        return addButton;
    }
}