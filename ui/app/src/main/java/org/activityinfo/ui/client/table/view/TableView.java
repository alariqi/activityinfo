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
package org.activityinfo.ui.client.table.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import org.activityinfo.analysis.table.EffectiveTableModel;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.ui.client.HasTitle;
import org.activityinfo.ui.client.header.HasFixedHeight;
import org.activityinfo.ui.client.page.GenericAvatar;
import org.activityinfo.ui.client.page.PageContainer;
import org.activityinfo.ui.client.page.PageStyle;
import org.activityinfo.ui.client.store.FormStore;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Displays a Form as a Table.
 */
public class TableView implements IsWidget, HasTitle, HasFixedHeight {

    public static final int MARGINS = 8;

    private static final Logger LOGGER = Logger.getLogger(TableView.class.getName());

    private final TableViewModel viewModel;

    private TableGrid grid;

    private final SidePanel sidePanel;
    private final PageContainer container;
    private final GridContainer gridContainer;

    private final SubscriptionSet subscriptions = new SubscriptionSet();


    public TableView(FormStore formStore, final TableViewModel viewModel) {

        TableBundle.INSTANCE.style().ensureInjected();

        this.viewModel = viewModel;

        TableToolBar toolBar = new TableToolBar(formStore, viewModel);

        gridContainer = new GridContainer();
        gridContainer.addStyleName("formtable__gridcontainer");

        sidePanel = new SidePanel(formStore, viewModel);
        BorderLayoutContainer.BorderLayoutData sidePaneLayout = new BorderLayoutContainer.BorderLayoutData(.3);
        sidePaneLayout.setSplit(true);
        sidePaneLayout.setMargins(new Margins(0, 0, 0, MARGINS));
        sidePaneLayout.setCollapsible(true);
        sidePaneLayout.setCollapsed(true);

        this.container = new PageContainer(PageStyle.FULLWIDTH);
        this.container.addBodyStyleName("formtable");
        this.container.getHeader().setAvatar(GenericAvatar.FORM);
        this.container.addBodyWidget(toolBar);
        this.container.addBodyWidget(gridContainer);

        subscriptions.add(viewModel.getEffectiveTable().subscribe(observable -> effectiveModelChanged()));
    }

    @Override
    public Widget asWidget() {
        return container.asWidget();
    }

    private void effectiveModelChanged() {
        if(viewModel.getEffectiveTable().isLoading()) {
//            this.container.mask();
        } else {
//            this.container.unmask();

            switch (viewModel.getEffectiveTable().get().getRootFormState()) {
                case FORBIDDEN:
                case DELETED:
                    showErrorState(viewModel.getEffectiveTable().get().getRootFormState());
                    break;
                case VALID:
                    updateGrid(viewModel.getEffectiveTable().get());
                    break;
            }
        }
    }

    private void showErrorState(FormTree.State rootFormState) {

    }

    private void updateGrid(EffectiveTableModel effectiveTableModel) {

        LOGGER.log(Level.INFO, "updating grid");

        container.getHeader().setHeading(effectiveTableModel.getFormLabel());


        // If the grid is already displayed, try to update without
        // destroying everything
        if(grid != null && grid.updateView(effectiveTableModel)) {
            return;
        }

        grid = new TableGrid(effectiveTableModel, viewModel.getColumnSet(), viewModel);
        grid.addSelectionChangedHandler(event -> {
            if(!event.getSelection().isEmpty()) {
                RecordRef ref = event.getSelection().get(0);
                viewModel.select(ref);
            }
        });
        gridContainer.setGrid(grid);
    }

    @Override
    public Observable<String> getTitle() {
        return viewModel.getFormTree().transform(formTree -> {
            switch (formTree.getRootState()) {
                case VALID:
                    return formTree.getRootFormClass().getLabel();
                case DELETED:
                    return I18N.CONSTANTS.deletedForm();
                case FORBIDDEN:
                    return I18N.CONSTANTS.forbiddenForm();
            }
            return I18N.CONSTANTS.notFound();
        });
    }
}
