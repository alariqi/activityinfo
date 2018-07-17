package org.activityinfo.ui.client.importer.view;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.Event;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.client.base.avatar.GenericAvatar;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.base.field.RadioButton;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.importer.state.ImportSource;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.state.ImportUpdater;
import org.activityinfo.ui.client.importer.viewModel.ImportViewModel;
import org.activityinfo.ui.client.importer.viewModel.SelectedColumnViewModel;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnTarget;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class ImportView {

    private static final Logger LOGGER = Logger.getLogger(ImportView.class.getName());
    private final FormStore formStore;
    private final ImportViewModel viewModel;
    private final ImportUpdater updater;


    public static VTree render(FormStore formStore, Observable<Maybe<ImportViewModel>> viewModel, ImportUpdater updater) {

        return new ReactiveComponent(viewModel.transform(vm ->
                vm.switch_(new Maybe.Case<ImportViewModel, VTree>() {
                    @Override
                    public VTree visible(ImportViewModel value) {
                        return new ImportView(formStore, value, updater).render();
                    }

                    @Override
                    public VTree forbidden() {
                        return NonIdeal.forbidden();
                    }

                    @Override
                    public VTree deleted() {
                        return NonIdeal.notFound();
                    }

                    @Override
                    public VTree notFound() {
                        return NonIdeal.notFound();
                    }
                })));
    }


    public ImportView(FormStore formStore, ImportViewModel viewModel, ImportUpdater updater) {
        this.formStore = formStore;
        this.viewModel = viewModel;
        this.updater = updater;
    }

    private VTree render() {
        return new PageBuilder()
                .avatar(GenericAvatar.DATABASE)
                .heading(I18N.CONSTANTS.importDataExistingForm())
                .breadcrumbs(viewModel.getBreadcrumbs())
                .body(body())
                .build();
    }

    private VTree body() {
        return new ReactiveComponent(viewModel.getCurrentStep().transform(step -> {
            if(step == ImportState.ImportStep.CHOOSE_SOURCE) {
                return chooseSource();
            } else {
                return matchColumns();
            }
        }));

    }

    private VTree chooseSource() {
        return H.div("importer",
                importHeader(I18N.CONSTANTS.uploadYourData(),
                        cancelButton(),
                        sourceContinueButton()),
                H.div("importer__body importer__source",
                        new VNode(HtmlTag.TEXTAREA, Props.create()
                                .oninput(event -> onSourceInput(event))),
                        H.div("importer__source__buttons",
                            H.div(
                                H.h3("Add your data here"),
                                H.p("Paste your copied table in this text field."),
                                H.p("Alternatively, upload a CSV or XLS file."),
                                Buttons.button(I18N.CONSTANTS.upload())
                                        .primary()
                                        .icon(Icon.BUBBLE_IMPORT)
                                        .build()))));
    }



    private VTree matchColumns() {
        return H.div("importer",
                importHeader(I18N.CONSTANTS.matchFieldStep(),
                        cancelButton(),
                        backButton(),
                        doneButton()),
                H.div("importer__body importer__match",
                        matchDataTable(),
                        columnPanel()));
    }



    private VTree matchDataTable() {
        return MatchTableView.render(viewModel, updater);
    }

    private VTree columnPanel() {
        return new ReactiveComponent(viewModel.getSelectedColumnView().transform(column ->
                new SidePanel()
                        .hideMode(SidePanel.HideMode.NONE)
                        .header(H.h2(I18N.MESSAGES.matchColumnToField(column.getSource().getLabel())))
                        .content(
                                H.div("importer__choice",
                                    H.div("importer__choice__body", columnTargets(column))))
                        .build()));
    }

    private VTree columnTargets(SelectedColumnViewModel column) {
        List<VTree> radioGroup = new ArrayList<>();
        for (ColumnTarget columnTarget : column.getTargets()) {
            radioGroup.add(new RadioButton()
                .label(columnTarget.getLabel())
                .name("radio-" + column.getSource().getId())
                .onchange(event -> onTargetSelected(column, columnTarget, event))
                .render());
        }
        return new VNode(HtmlTag.DIV, PropMap.EMPTY, radioGroup);
    }

    private void onTargetSelected(SelectedColumnViewModel column, ColumnTarget columnTarget, Event event) {
        InputElement input = event.getEventTarget().cast();
        if(input.isChecked()) {

        }
    }

    private VNode importHeader(String heading, VTree... buttons) {
        return H.div("importer__header",
                H.div("importer__header__inner",
                        H.div("surtitle", new VText(viewModel.getFormLabel())),
                        H.h2(heading)),
                H.div("importer__header__actions",
                        buttons));
    }

    private VTree cancelButton() {
        return Buttons.button(I18N.CONSTANTS.cancel())
                .icon(Icon.BUBBLE_CLOSE)
                .build();
    }

    private VTree sourceContinueButton() {
        return new ReactiveComponent(viewModel.isSourceValid().transform(source ->
             Buttons.button(I18N.CONSTANTS.continue_())
                .icon(Icon.BUBBLE_ARROWRIGHT)
                .enabled(source)
                .onSelect(event -> updater.update(s -> s.withStep(ImportState.ImportStep.MATCH_COLUMNS)))
                .build()));
    }


    private VTree backButton() {
        return Buttons.button(I18N.CONSTANTS.back())
                .icon(Icon.BUBBLE_ARROWLEFT)
                .onSelect(event -> updater.update(s -> s.withStep(ImportState.ImportStep.CHOOSE_SOURCE)))
                .build();
    }

    private VTree doneButton() {
        return Buttons.button(I18N.CONSTANTS.done())
                .icon(Icon.BUBBLE_CHECKMARK)
                .primary()
                .build();
    }


    private void onSourceInput(Event event) {
        TextAreaElement textarea = event.getEventTarget().cast();
        if(Strings.isNullOrEmpty(textarea.getValue())) {
            updater.update(s -> s.withSource(Optional.empty()));
        } else {
            updater.update(s -> s.withSource(Optional.of(new ImportSource(textarea.getValue()))));
        }
    }
}
