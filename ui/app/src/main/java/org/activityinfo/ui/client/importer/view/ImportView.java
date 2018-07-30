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
import org.activityinfo.ui.client.importer.viewModel.ImportedTable;
import org.activityinfo.ui.client.importer.viewModel.SelectedColumnViewModel;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnTarget;
import org.activityinfo.ui.client.importer.viewModel.fields.ScoredColumnTarget;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.Arrays;
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
                .headerAction(navigation())
                .body(body())
                .build();
    }

    /**
     * Renders the Cancel, Back, and continue buttons
     */
    private VTree navigation() {
        return new ReactiveComponent(viewModel.getCurrentStep().transform(step -> {
            List<VTree> buttons = new ArrayList<>();
            buttons.add(cancelButton());
            if(step.ordinal() > 0) {
                buttons.add(backButton());
            }
            if(step == ImportState.ImportStep.CHOOSE_SOURCE) {
                buttons.add(sourceContinueButton());
            } else if(step == ImportState.ImportStep.MATCH_COLUMNS) {
                buttons.add(matchingContinueButton());
            } else {
                buttons.add(doneButton());
            }
            return new VNode(HtmlTag.DIV, buttons.stream());
        }));
    }

    private VTree body() {
        return new ReactiveComponent(viewModel.getCurrentStep().transform(step -> {
            switch (step) {
                default:
                case CHOOSE_SOURCE:
                    return chooseSource();
                case MATCH_COLUMNS:
                    return matchColumns();
                case REVIEW_INVALID:
                    return verifyRecords();
            }
        }));
    }


    private VTree chooseSource() {
        return H.div("importer",
                H.div("importer__main",
                importHeader(I18N.CONSTANTS.uploadYourData()),
                H.div("importer__body importer__source",
                        pasteTarget(),
                        H.div("importer__source__buttons",
                            H.div(
                                H.h3("Add your data here"),
                                H.p("Paste your copied table in this text field."),
                                H.p("Alternatively, upload a CSV or XLS file."),
                                Buttons.button(I18N.CONSTANTS.upload())
                                        .primary()
                                        .icon(Icon.BUBBLE_IMPORT)
                                        .build())))));
    }


    private VTree matchColumns() {
        return H.div("importer",
                H.div("importer__main",
                        importHeader(I18N.CONSTANTS.matchFieldStep(),
                                requiredFieldError()),
                        H.div("importer__body importer__match",
                                matchDataTable())),
                columnPanel());
    }

    private VTree verifyRecords() {
        return new ReactiveComponent(viewModel.getImportedTable().transform(table -> {
            return H.div("importer",
                H.div("importer__main",
                        importHeader(I18N.CONSTANTS.reviewInvalidRecords(),
                                validCountHeader(table),
                                invalidCountHeader(table),
                                downloadInvalidButton(table)),
                        H.div("importer__body importer__verify",
                                InvalidDataTable.render(table))));
        }));

    }



    private VTree validCountHeader(ImportedTable table) {
        return H.p(Props.withClass("importer__valid-count"),
                new VText(I18N.MESSAGES.validRecordCount(table.getValidRecordCount())));
    }

    private VTree invalidCountHeader(ImportedTable table) {
        return H.p(Props.withClass("importer__invalid-count"),
                new VText(I18N.MESSAGES.invalidRecordCount(table.getInvalidRecordCount())));
    }

    private VTree downloadInvalidButton(ImportedTable table) {
        return H.div(Buttons
                .button(I18N.CONSTANTS.downloadInvalidRecordsAsCSV())
                .primary()
                .icon(Icon.BUBBLE_EXPORT)
                .build());
    }


    private VTree pasteTarget() {
        Observable<Optional<ImportSource>> sourceText = viewModel
                .getState()
                .transform(s -> s.getSource())
                .cache();

        return new ReactiveComponent(sourceText.transform(s -> {
            PropMap props = Props.create();
            props.set("value", s.map(source -> source.getText()).orElse(""));
            props.oninput(event -> {
                onSourceInput(event);
            });

            return new VNode(HtmlTag.TEXTAREA, props);
        }));

    }

    private void onSourceInput(Event event) {
        TextAreaElement textarea = event.getEventTarget().cast();
        if(Strings.isNullOrEmpty(textarea.getValue())) {
            updater.update(s -> s.withSource(Optional.empty()));
        } else {
            updater.update(s -> s.withSource(Optional.of(new ImportSource(textarea.getValue()))));
        }
    }


    private VTree requiredFieldError() {
        Observable<Boolean> complete = viewModel.getMappedSource().transform(m -> m.isComplete()).cache();
        return new ReactiveComponent(complete.transform(c -> {
            if(c) {
                return H.div();
            } else {
                return H.div("alert alert--error", H.t(I18N.CONSTANTS.requiredFieldsNotMapped()));
            }
        }));
    }

    private VTree matchDataTable() {
        return MatchTableView.render(viewModel, updater);
    }

    private VTree columnPanel() {
        return new ReactiveComponent(viewModel.getSelectedColumnView().transform(column ->
                new SidePanel()
                        .hideMode(SidePanel.HideMode.NONE)
                        .header(H.h2(I18N.MESSAGES.columnHeading(column.getLabel())))
                        .content(
                                H.div("importer__choice",
                                    H.div("importer__choice__body", columnTargets(column))))
                        .build()));
    }

    private VTree columnTargets(SelectedColumnViewModel column) {

        return new VNode(HtmlTag.DIV, PropMap.EMPTY,
                H.div(H.h3(I18N.CONSTANTS.columnMatching())),
                bestTargetList(column),
                ignoreRadioButton(column),
                remainingFields(column));
    }


    /**
     * Display the best matching fields at the top of list with a
     * "probable matches" heading.
     */
    private VTree bestTargetList(SelectedColumnViewModel column) {
        List<ScoredColumnTarget> bestMatches = column.getTargets().getNameMatches();
        if(bestMatches.isEmpty()) {
            return H.div();
        }

        List<VTree> children = new ArrayList<>();
        children.add(H.h4(I18N.CONSTANTS.probableMatches()));
        for (ScoredColumnTarget scoredTarget : bestMatches) {
            children.add(targetRadioButton(column, scoredTarget.getTarget()));
        }
        return new VNode(HtmlTag.FIELDSET,
                Props.withClass("importer__best"), children);
    }


    private VTree remainingFields(SelectedColumnViewModel column) {
        List<VTree> radios = new ArrayList<>();
        for (ScoredColumnTarget scoredTarget : column.getTargets().getOther()) {
            radios.add(targetRadioButton(column, scoredTarget.getTarget()));
        }
        return new VNode(HtmlTag.FIELDSET, Props.withClass("importer__other"), radios);
    }

    private VTree targetRadioButton(SelectedColumnViewModel column, ColumnTarget target) {
        return new RadioButton()
            .label(target.getLabel())
            .name("radio-" + column.getId())
            .checked(column.isSelected(target))
            .onchange(event -> onTargetSelected(column, target, event))
            .render();
    }


    private VTree ignoreRadioButton(SelectedColumnViewModel column) {
        return new RadioButton()
                .label(I18N.CONSTANTS.ignoreColumnAction())
                .name("radio-ignore-column")
                .checked(column.isIgnored())
                .onchange(event -> onIgnoreSelected(column, event))
                .render();
    }

    private void onTargetSelected(SelectedColumnViewModel column, ColumnTarget target, Event event) {
        InputElement input = event.getEventTarget().cast();
        if(input.isChecked()) {
            updater.update(s -> s.updateMappings(m -> target.apply(m, column.getId())));
        }
    }

    private void onIgnoreSelected(SelectedColumnViewModel column, Event event) {
        InputElement input = event.getEventTarget().cast();
        if(input.isChecked()) {
            updater.update(s -> s.updateMappings(m -> m.withColumnIgnored(column.getId())));
        }
    }

    private VNode importHeader(String heading, VTree... content) {

        List<VTree> children = new ArrayList<>();
        children.add(H.div("surtitle", new VText(viewModel.getFormLabel())));
        children.add(H.h2(heading));
        children.addAll(Arrays.asList(content));

        return H.div("importer__header",
                H.div("importer__header__inner",
                        children.stream()));
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
                .primary()
                .enabled(source)
                .onSelect(event -> updater.update(s -> s.withStep(ImportState.ImportStep.MATCH_COLUMNS)))
                .build()));
    }

    private VTree matchingContinueButton() {
        return new ReactiveComponent(viewModel.isMappingComplete().transform(source ->
                Buttons.button(I18N.CONSTANTS.continue_())
                        .icon(Icon.BUBBLE_ARROWRIGHT)
                        .primary()
                        .enabled(source)
                        .onSelect(event -> updater.update(s -> s.withStep(ImportState.ImportStep.REVIEW_INVALID)))
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



}
