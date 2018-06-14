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

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CloseEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.json.Json;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.resource.RecordTransaction;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.Subscription;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.button.IconButton;
import org.activityinfo.ui.client.base.button.IconButtonStyle;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.base.container.StaticHtml;
import org.activityinfo.ui.client.base.info.Alert;
import org.activityinfo.ui.client.base.info.ErrorConfig;
import org.activityinfo.ui.client.base.info.SuccessConfig;
import org.activityinfo.ui.client.input.model.FieldInput;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.input.viewModel.FormInputViewModel;
import org.activityinfo.ui.client.input.viewModel.FormInputViewModelBuilder;
import org.activityinfo.ui.client.input.viewModel.FormStructure;
import org.activityinfo.ui.client.store.FormStore;

import java.util.logging.Logger;

/**
 * Root view for a {@link FormInputModel}
 */
public class FormInputView implements IsWidget, InputHandler {

    private static final Logger LOGGER = Logger.getLogger(FormInputView.class.getName());

    private final Observable<FormStructure> formStructure;

    /**
     * True if we've finished the initial load of the form structure and
     * existing record. If either changes after the initial load, we alter the
     * user but do not immediately screw with the form.
     */
    private boolean initialLoad = false;

    private FormPanel formPanel = null;

    private FormStore formStore;
    private final CloseEvent.CloseHandler closeHandler;
    private Maybe<RecordTree> existingRecord;
    private FormInputModel inputModel;
    private FormInputViewModelBuilder viewModelBuilder = null;

    private FormInputViewModel viewModel = null;

    private Alert errorMessage;

    private CssLayoutContainer container;

    private Subscription structureSubscription;
    private IconButton saveButton;
    private IconButton cancelButton;

    public FormInputView(FormStore formStore, RecordRef recordRef, CloseEvent.CloseHandler closeHandler) {
        this.formStore = formStore;
        this.closeHandler = closeHandler;
        this.formStructure = FormStructure.fetch(formStore, recordRef);
        this.inputModel = new FormInputModel(recordRef);

        this.errorMessage = new Alert(Alert.Type.ERROR, I18N.CONSTANTS.formError());
        this.errorMessage.setVisible(false);

        this.container = new CssLayoutContainer();
        this.container.addStyleName("forminput__inner");
        this.container.mask();
        this.container.addAttachHandler(event -> {
            if(event.isAttached()) {
                structureSubscription = this.formStructure.subscribe(this::onStructureChanged);
            } else {
                structureSubscription.unsubscribe();
                structureSubscription = null;
            }
        });
    }

    private void onStructureChanged(Observable<FormStructure> observable) {
        if(!initialLoad && observable.isLoaded()) {
            onInitialLoad(observable.get());
        } else {
            // TODO: alert the user and prompt to update the form layout
        }
    }

    private void onInitialLoad(FormStructure formStructure) {
        initialLoad = true;
        container.unmask();

        viewModelBuilder = new FormInputViewModelBuilder(formStructure.getFormTree());
        existingRecord = formStructure.getExistingRecord();

        SafeHtml heading;
        if(existingRecord.getState() == Maybe.State.NOT_FOUND) {
            heading = SafeHtmlUtils.fromString(I18N.CONSTANTS.addRecord());
        } else {
            heading = I18N.MESSAGES.editRecordHeading(formStructure.getFormLabel());
        }

        formPanel = new FormPanel(formStore, formStructure.getFormTree(), inputModel.getRecordRef(), this);

        viewModel = viewModelBuilder.build(inputModel, existingRecord);
        formPanel.init(viewModel);
        updateView();

        cancelButton = new IconButton(Icon.BUBBLE_CLOSE, I18N.CONSTANTS.cancel());
        cancelButton.addSelectHandler(event -> closeHandler.onClose(new CloseEvent(null)));
        saveButton = new IconButton(Icon.BUBBLE_CHECKMARK, IconButtonStyle.PRIMARY, I18N.CONSTANTS.save());
        saveButton.addSelectHandler(event -> save());

        CssLayoutContainer footer = new CssLayoutContainer();
        footer.addStyleName("forminput__footer");
        footer.add(cancelButton);
        footer.add(saveButton);

        container.add(new StaticHtml(InputTemplates.TEMPLATES.formHeading(heading)));
        container.add(errorMessage);
        container.add(formPanel);
        container.add(footer);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    @Override
    public void updateField(RecordRef recordRef, ResourceId fieldId, FieldInput value) {
        update(inputModel.update(fieldId, value));
    }

    @Override
    public void touchField(RecordRef recordRef, ResourceId fieldId) {
        update(inputModel.touch(fieldId));
    }

    private void update(FormInputModel updatedModel) {
        if(this.inputModel != updatedModel) {
            this.inputModel = updatedModel;
            this.viewModel = viewModelBuilder.build(inputModel, existingRecord);
            updateView();
        }
    }

    private void updateView() {
        formPanel.updateView(viewModel);
        errorMessage.setVisible(!viewModel.isValid() && inputModel.isValidationRequested());
    }

    public void save() {

        // If the view model is still loading, ignore this click

        if(viewModel == null) {
            return;
        }

        if(!viewModel.isValid()) {
            onInvalidSubmission();
            return;
        }


        // Good to go...

        RecordTransaction tx = viewModel.buildTransaction();

        LOGGER.info("Submitting transaction: " + Json.stringify(tx));


        // Disable the save button while the transaction is in progress

        enableButtons(false);


        formStore.updateRecords(tx).then(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                enableButtons(true);
                Info.display(new ErrorConfig(caught));
            }

            @Override
            public void onSuccess(Void result) {
                enableButtons(true);

                Info.display(new SuccessConfig(I18N.CONSTANTS.changesSaved()));

                closeHandler.onClose(new CloseEvent(null));
            }
        });
    }

    /**
     * The user has tried to save a record that is in an invalid state.
     */
    private void onInvalidSubmission() {

        update(inputModel.validationRequested());

        com.google.gwt.core.client.Scheduler.get().scheduleDeferred(() -> {
            formPanel.scrollToFirstError();
        });

        ErrorConfig errorConfig = new ErrorConfig(I18N.CONSTANTS.pleaseCompleteForm());
        Info.display(errorConfig);
    }


    private void enableButtons(boolean enabled) {
        saveButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
    }
}

