/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.page.common.grid;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.grid.Grid;

public abstract class AbstractEditorGridView<M extends ModelData, P extends GridPresenter<M>> extends
        AbstractGridView<M, P> {

    private M lastSelection;

    @Override
    protected void initGridListeners(final Grid<M> grid) {
        grid.getStore().setMonitorChanges(true);

        grid.getStore().addListener(Store.Update, new Listener<StoreEvent>() {
            @Override
            public void handleEvent(StoreEvent se) {
                toolBar.setDirty(true);
            }
        });

        grid.addListener(Events.CellClick, new Listener<GridEvent>() {
            @Override
            public void handleEvent(GridEvent be) {
                onCellClick(be);
            }
        });

        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<M>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<M> se) {
                if (se.getSelectedItem() != null) {
                    presenter.onSelectionChanged(se.getSelectedItem());
                }
            }
        });

        grid.addListener(Events.BeforeEdit, new Listener<GridEvent>() {
            @Override
            public void handleEvent(GridEvent be) {
                if (!presenter.beforeEdit(be.getRecord(), be.getProperty())) {
                    be.setCancelled(true);
                }
            }
        });
    }

    protected void onCellClick(GridEvent ge) {
        M selection = (M) ge.getGrid().getSelectionModel().getSelectedItem();
        if (lastSelection != selection) {
            lastSelection = selection;
            presenter.onSelectionChanged(selection);
        }
    }

    @Override
    public void setActionEnabled(String actionId, boolean enabled) {
        super.setActionEnabled(actionId, enabled);
    }
}
