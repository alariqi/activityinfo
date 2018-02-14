package org.activityinfo.ui.client.table.view;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.grid.LiveGridView;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class LiveRecordGridView extends LiveGridView<Integer> {

    private static final boolean SORTING_IMPLEMENTED = false;


    /**
     * Creates a context menu for the given column, including sort menu items and column visibility sub-menu.
     *
     * @param colIndex the column index
     * @return the context menu for the given column
     */
    protected Menu createContextMenu(final int colIndex) {
        final Menu menu = new Menu();

        if (SORTING_IMPLEMENTED && cm.isSortable(colIndex)) {
            MenuItem item = new MenuItem();
            item.setText(DefaultMessages.getMessages().gridView_sortAscText());
            item.setIcon(header.getAppearance().sortAscendingIcon());
            item.addSelectionHandler(new SelectionHandler<Item>() {
                @Override
                public void onSelection(SelectionEvent<Item> event) {
                    doSort(colIndex, SortDir.ASC);
                }
            });
            menu.add(item);

            item = new MenuItem();
            item.setText(DefaultMessages.getMessages().gridView_sortDescText());
            item.setIcon(header.getAppearance().sortDescendingIcon());
            item.addSelectionHandler(new SelectionHandler<Item>() {
                @Override
                public void onSelection(SelectionEvent<Item> event) {
                    doSort(colIndex, SortDir.DESC);
                }
            });
            menu.add(item);
        }

        return menu;
    }

    @Override
    protected void templateOnColumnWidthUpdated(int col, int w, int tw) {
        fireEvent(new ColumnResizeEvent(col, w));
    }

    public HandlerRegistration addColumnResizeHandler(ColumnResizeHandler handler) {
        return addHandler(ColumnResizeEvent.getType(), handler);
    }
}
