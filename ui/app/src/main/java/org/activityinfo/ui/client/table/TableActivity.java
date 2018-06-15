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
package org.activityinfo.ui.client.table;

import com.google.common.base.Optional;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import org.activityinfo.analysis.table.TableUpdater;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.json.JsonValue;
import org.activityinfo.model.analysis.ImmutableTableColumn;
import org.activityinfo.model.analysis.ImmutableTableModel;
import org.activityinfo.model.analysis.TableColumn;
import org.activityinfo.model.analysis.TableModel;
import org.activityinfo.model.formula.FormulaNode;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.storage.LocalStorage;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.view.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TableActivity extends AbstractActivity implements TableUpdater {

    private static final Logger LOGGER = Logger.getLogger(TableActivity.class.getName());

    private FormStore formStore;
    private TablePlace place;

    private LocalStorage storage;
    private StatefulValue<TableModel> model;
    private TableViewModel viewModel;
    private TableView view;

    public TableActivity(FormStore formStore, LocalStorage storage, TablePlace place) {
        this.formStore = formStore;
        this.place = place;
        this.storage = storage;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        model = new StatefulValue<>(initialModel(place.getFormId()));
        viewModel = new TableViewModel(formStore, model);
        view = new TableView(formStore, viewModel, this);
        panel.setWidget(view);
    }


    private String modelKey(ResourceId formId) {
        return "tableViewModel:" + formId.asString();
    }

    /**
     * The load the model for this table from local storage.
     */
    private TableModel initialModel(ResourceId formId) {
        Optional<JsonValue> object = storage.getObjectIfPresent(modelKey(formId));
        if(object.isPresent()) {
            try {
                return TableModel.fromJson(object.get());
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to deserialize saved model: ", e);
            }
        }
        return ImmutableTableModel
                .builder()
                .formId(formId)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void updateModel(TableModel updatedModel) {
        if(model.updateIfNotEqual(updatedModel)) {

            // Persist the model to local storage for the next time
            // the user navigates here.
            storage.setObject(modelKey(place.getFormId()), updatedModel.toJson());
        }
    }

    @Override
    public void updateFilter(Optional<FormulaNode> filterNode) {

        Optional<String> filter = filterNode.transform(n -> n.asExpression());

        model.updateIfNotEqual(
                ImmutableTableModel.builder()
                        .from(model.get())
                        .filter(filter)
                        .build());

    }

    @Override
    public void updateColumnWidth(String columnId, int newWidth) {

        TableModel model = this.model.get();

        List<TableColumn> updatedColumns = new ArrayList<>();
        for (TableColumn column : model.getColumns()) {
            if(column.getId().equals(columnId)) {
                updatedColumns.add(ImmutableTableColumn.builder().from(column).width(newWidth).build());
            } else {
                updatedColumns.add(column);
            }
        }

        this.model.updateIfNotSame(ImmutableTableModel.builder()
                .from(model)
                .columns(updatedColumns)
                .build());
    }

    @Override
    public void newRecord() {
        ResourceId newRecordId = ResourceId.generateSubmissionId(viewModel.getFormId());
        RecordRef newRecordRef = new RecordRef(viewModel.getFormId(), newRecordId);
        view.editRecord(newRecordRef);
    }

    @Override
    public void editRecord(RecordRef recordRef) {
        view.editRecord(recordRef);
    }
}
