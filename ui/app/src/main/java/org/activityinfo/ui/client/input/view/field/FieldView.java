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
package org.activityinfo.ui.client.input.view.field;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.base.container.StaticHtml;
import org.activityinfo.ui.client.input.view.InputTemplates;
import org.activityinfo.ui.client.input.viewModel.FormInputViewModel;

import java.util.Collection;

public class FieldView implements IsWidget {
    private final ResourceId fieldId;
    private final FieldWidget widget;
    private HTML validationMessage;
    private final CssLayoutContainer container;

    private boolean valid = true;

    public FieldView(FormField field, FieldWidget fieldWidget) {

        this.fieldId = field.getId();
        this.widget = fieldWidget;

        StaticHtml fieldLabel;
        if(field.isRequired()) {
            fieldLabel = new StaticHtml(InputTemplates.TEMPLATES.requiredFieldHeading(field.getLabel(), I18N.CONSTANTS.required()));
        } else {
            fieldLabel = new StaticHtml(InputTemplates.TEMPLATES.fieldHeading(field.getLabel()));
        }

        validationMessage = new HTML();
        validationMessage.setVisible(false);
        validationMessage.addStyleName("forminput__validationmessage");

        container = new CssLayoutContainer();
        container.setStyleName("forminput__field");
        if(field.isRequired()) {
            container.addStyleName("forminput__field--required");
        }
        container.add(fieldLabel);
        if (!Strings.isNullOrEmpty(field.getDescription())) {
            container.add(new StaticHtml(InputTemplates.TEMPLATES.fieldDescription(field.getDescription())));
        }
        container.add(fieldWidget);
        container.add(validationMessage);
    }

    public ResourceId getFieldId() {
        return fieldId;
    }

    public FieldWidget getWidget() {
        return widget;
    }

    public void updateView(FormInputViewModel viewModel) {
        container.setVisible(viewModel.isRelevant(fieldId));
        widget.setRelevant(!viewModel.isLocked());

        if(viewModel.isMissingErrorVisible(fieldId)) {
            invalidate(I18N.CONSTANTS.requiredFieldMessage());
        } else {
            Collection<String> validationErrors = viewModel.getValidationErrors(fieldId);
            if(!validationErrors.isEmpty()) {
                invalidate(validationErrors.iterator().next());
            } else {
                valid = true;
                validationMessage.setVisible(false);
                container.removeStyleName("forminput__field--invalid");
            }
        }
    }

    public void invalidate(String message) {
        valid = false;
        validationMessage.setText(message);
        validationMessage.setVisible(true);
        container.addStyleName("forminput__field--invalid");
    }

    public void init(FormInputViewModel viewModel) {
        FieldValue fieldValue = viewModel.getField(fieldId);
        if(fieldValue == null) {
            widget.clear();
        } else {
            widget.init(fieldValue);
        }
    }

    public boolean isValid() {
        return valid;
    }

    /**
     * Scrolls this field into view and focus on the first input within the field.
     */
    public void focusTo() {
        container.getElement().scrollIntoView();
        widget.focus();
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}

