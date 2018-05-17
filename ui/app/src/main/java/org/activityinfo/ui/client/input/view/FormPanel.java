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

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.model.type.time.PeriodType;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.base.container.StaticHtml;
import org.activityinfo.ui.client.input.model.FieldInput;
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
                FieldWidget fieldWidget = widgetFactory.create(node.getField(), input -> onInput(node, input));

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
        inputHandler.updateModel(recordRef, node.getFieldId(), input);
    }

    private void addField(FormField field, FieldWidget fieldWidget) {

        StaticHtml fieldLabel;
        if(field.isRequired()) {
            fieldLabel = new StaticHtml(InputTemplates.TEMPLATES.requiredFieldHeading(field.getLabel(), I18N.CONSTANTS.required()));
        } else {
            fieldLabel = new StaticHtml(InputTemplates.TEMPLATES.fieldHeading(field.getLabel()));
        }

        HTML validationMessage = new HTML();
        validationMessage.setVisible(false);
        validationMessage.addStyleName("forminput__validationmessage");

        CssLayoutContainer fieldPanel = new CssLayoutContainer();
        fieldPanel.setStyleName("forminput__field");
        if(field.isRequired()) {
            fieldPanel.addStyleName("forminput__field--required");
        }
        fieldPanel.add(fieldLabel);
        if (!Strings.isNullOrEmpty(field.getDescription())) {
            fieldPanel.add(new StaticHtml(InputTemplates.TEMPLATES.fieldDescription(field.getDescription())));
        }
        fieldPanel.add(fieldWidget);
        fieldPanel.add(validationMessage);

        panel.add(fieldPanel);

        fieldViews.add(new FieldView(field.getId(), fieldWidget, validationMessage));
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

}
