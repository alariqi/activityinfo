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
package org.activityinfo.ui.client.input.view;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.time.PeriodType;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.input.model.FieldInput;
import org.activityinfo.ui.client.input.view.field.FieldUpdater;
import org.activityinfo.ui.client.input.view.field.FieldView;
import org.activityinfo.ui.client.input.view.field.FieldWidget;
import org.activityinfo.ui.client.input.view.field.FieldWidgetFactory;
import org.activityinfo.ui.client.input.viewModel.FormInputViewModel;

import java.util.ArrayList;
import java.util.List;


/**
 * View that displays a form's fields and sub forms and accepts user input.
 */
public class FormPanel implements IsWidget {

    private final FormSource formSource;

    private final CssLayoutContainer panel;

    private final List<FieldView> fieldViews = new ArrayList<>();

    private InputHandler inputHandler;
    private RecordRef recordRef;

    private FormInputViewModel viewModel;

    public FormPanel(FormSource formSource, FormTree formTree, RecordRef recordRef, InputHandler inputHandler) {
        this.formSource = formSource;

        assert recordRef != null;

        this.recordRef = recordRef;
        this.inputHandler = inputHandler;

        panel = new CssLayoutContainer("form");

        FieldWidgetFactory widgetFactory = new FieldWidgetFactory(formSource, formTree);

        for (FormTree.Node node : formTree.getRootFields()) {
            if(node.isSubForm()) {
                // ignore
            } else if(node.isParentReference()) {
                // ignore
            } else if(node.getField().isVisible() && !isSubFormKey(node)) {
                FieldWidget fieldWidget = widgetFactory.create(node.getField(), new FieldUpdater() {
                    @Override
                    public void update(FieldInput input) {
                        onInput(node, input);
                    }

                    @Override
                    public void touch() {
                        onTouch(node);
                    }

                });

                if (fieldWidget != null) {
                    addField(node.getField(), fieldWidget);
                }
            }
        }
    }

    private boolean isSubFormKey(FormTree.Node node) {
        return node.getDefiningFormClass().isSubForm() && node.getField().isKey() &&
                node.getType() instanceof PeriodType;
    }

    public RecordRef getRecordRef() {
        return recordRef;
    }

    public void init(FormInputViewModel viewModel) {

        this.recordRef = viewModel.getRecordRef();

        for (FieldView fieldView : fieldViews) {
            fieldView.init(viewModel);
        }
    }

    public void updateView(FormInputViewModel viewModel) {

        this.viewModel = viewModel;

        // Update Field Views
        for (FieldView fieldView : fieldViews) {
            fieldView.updateView(viewModel);
        }
    }

    private void onInput(FormTree.Node node, FieldInput input) {
        inputHandler.updateField(recordRef, node.getFieldId(), input);
    }


    private void onTouch(FormTree.Node node) {
        inputHandler.touchField(recordRef, node.getFieldId());
    }

    private void addField(FormField field, FieldWidget fieldWidget) {
        FieldView fieldView = new FieldView(field, fieldWidget);
        panel.add(fieldView);
        fieldViews.add(fieldView);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    /**
     * Scrolls to the first field with an error, and shifts focus to it.
     */
    public void scrollToFirstError() {
        for (FieldView fieldView : fieldViews) {
            if(!fieldView.isValid()) {
                fieldView.focusTo();
                break;
            }
        }
    }
}
