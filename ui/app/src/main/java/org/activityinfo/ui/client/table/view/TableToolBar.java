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

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import org.activityinfo.analysis.table.TableUpdater;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormMetadata;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.button.IconButton;
import org.activityinfo.ui.client.base.button.IconButtonStyle;
import org.activityinfo.ui.client.base.toolbar.Toolbar;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.ColumnDialog;

import java.util.logging.Logger;

public class TableToolBar implements IsWidget {

    private static final Logger LOGGER = Logger.getLogger(TableToolBar.class.getName());

    private FormStore formStore;
    private TableViewModel viewModel;
    private final TableUpdater updater;

    private final IconButton newButton;
    private final TextButton importButton;

    private final SubscriptionSet subscriptions = new SubscriptionSet();

    private ExportOptionsDialog exportOptionsDialog;
    private final Toolbar toolbar;


    public TableToolBar(FormStore formStore, TableViewModel viewModel, TableUpdater updater) {
        this.formStore = formStore;
        this.viewModel = viewModel;
        this.updater = updater;

        newButton = new IconButton(Icon.BUBBLE_ADD, IconButtonStyle.PRIMARY, I18N.CONSTANTS.newRecord());
        newButton.addSelectHandler(this::onNewRecord);

        importButton = new TextButton(I18N.CONSTANTS.importText());
        importButton.addSelectHandler(this::onImport);

        TextButton exportButton = new TextButton(I18N.CONSTANTS.export());
        exportButton.addSelectHandler(this::onExport);

        IconButton columnsButton = new IconButton(Icon.BUBBLE_COLUMNS, I18N.CONSTANTS.chooseColumns());
        columnsButton.addSelectHandler(this::onChooseColumnsSelected);

        toolbar = new Toolbar();
        toolbar.asWidget().addAttachHandler(event -> onAttach(event));
        toolbar.addAction(newButton);
        toolbar.addAction(importButton);
        toolbar.addAction(exportButton);
        toolbar.addAction(columnsButton);

    }

    private void onAttach(AttachEvent event) {
        if(event.isAttached()) {
            subscriptions.add(viewModel.getFormTree().subscribe(this::onFormTreeChanged));
        } else {
            subscriptions.unsubscribeAll();
        }
    }

    private void onFormTreeChanged(Observable<FormTree> tree) {
        boolean canCreate = isNewAllowed(tree);
        newButton.setEnabled(canCreate);
        importButton.setEnabled(canCreate);
    }

    private boolean isNewAllowed(Observable<FormTree> tree) {
        if(tree.isLoading()) {
            return false;
        }
        FormMetadata rootForm = tree.get().getRootMetadata();
        return rootForm.getPermissions().isCreateRecordAllowed();
    }

    private void onNewRecord(SelectEvent event) {
        updater.newRecord();

    }

    private void onImport(SelectEvent event) {
        // Redirect to old app for the moment.
        UrlBuilder importUrl = Window.Location.createUrlBuilder();
        importUrl.setHash("#import/" + viewModel.getFormId().asString());
        importUrl.removeParameter("ui");

        Window.open(importUrl.buildString(), "_blank", null);
    }

    private void onExport(SelectEvent event) {
        if(exportOptionsDialog == null) {
            exportOptionsDialog = new ExportOptionsDialog(formStore, viewModel);
        }
        exportOptionsDialog.show();
    }


    private void onChooseColumnsSelected(SelectEvent event) {
        if(viewModel.getEffectiveTable().isLoaded()) {
            ColumnDialog dialog = new ColumnDialog(viewModel.getEffectiveTable().get());
            dialog.show(updatedModel -> {
                updater.updateModel(updatedModel);
            });
        }
    }

    @Override
    public Widget asWidget() {
        return toolbar.asWidget();
    }
}

