package org.activityinfo.ui.client.importer.view;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.Event;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.alert.Alerts;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.base.field.RadioButton;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.state.ImportUpdater;
import org.activityinfo.ui.client.importer.viewModel.ImportViewModel;
import org.activityinfo.ui.client.importer.viewModel.SelectedColumnViewModel;
import org.activityinfo.ui.client.importer.viewModel.ValidRowSet;
import org.activityinfo.ui.client.importer.viewModel.fields.ColumnTarget;
import org.activityinfo.ui.client.importer.viewModel.fields.ScoredColumnTarget;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;

import static org.activityinfo.ui.client.importer.view.ImportView.cancelButton;

public class MatchColumnView {


    public static VTree render(ImportViewModel viewModel, ImportUpdater updater) {
        return ImportView.page(viewModel,
                navigation(viewModel, updater),
                pageBody(viewModel, updater));
    }

    private static VTree navigation(ImportViewModel viewModel, ImportUpdater updater) {
        return H.div(cancelButton(),
                ImportView.backButton(ImportState.ImportStep.CHOOSE_SOURCE, updater),
                continueButton(viewModel, updater));
    }

    private static VTree continueButton(ImportViewModel viewModel, ImportUpdater updater) {

        Observable<Boolean> mappingComplete = viewModel.isMappingComplete();
        Observable<ValidRowSet> validRowSet = viewModel.getImportedTable().transform(table -> table.getValidRowSet()).cache();

        VTree loading = disabledContinueButton();

        return new ReactiveComponent(Observable.transform(mappingComplete, validRowSet, (c, v) -> {


            // If the mapping is complete, or if there are now valid rows, then

            if(c == Boolean.FALSE || v.getValidRowCount() == 0) {
                return disabledContinueButton();
            }

            // If there are no *invalid* rows, we can skip the review step

            if(v.getInvalidRowCount() == 0) {
                return ImportView.doneButton(viewModel, updater);
            } else {
                return continueToReviewButton(updater);
            }

        }), loading);

    }


    private static VTree disabledContinueButton() {
        return Buttons.button(I18N.CONSTANTS.continue_())
                .icon(Icon.BUBBLE_ARROWRIGHT)
                .primary()
                .enabled(false)
                .build();
    }


    private static VTree continueToReviewButton(ImportUpdater updater) {
        return Buttons.button(I18N.CONSTANTS.continue_())
                .icon(Icon.BUBBLE_ARROWRIGHT)
                .primary()
                .onSelect(event -> updater.update(s -> s.withStep(ImportState.ImportStep.REVIEW_INVALID)))
                .build();
    }


    private static VTree pageBody(ImportViewModel viewModel, ImportUpdater updater) {
        return H.div("importer",
                H.div("importer__main",
                        ImportView.heading(viewModel, I18N.CONSTANTS.matchFieldStep(),
                                requiredFieldError(viewModel)),
                        H.div("importer__body importer__match",
                                MatchTableView.render(viewModel, updater))),
                columnPanel(viewModel, updater));
    }


    private static VTree requiredFieldError(ImportViewModel viewModel) {

        // Is the mapping complete, or do we lack
        Observable<Boolean> complete = viewModel.getMappedSource().transform(m -> m.isComplete()).cache();

        return new ReactiveComponent(viewModel.isMappingComplete().transform(c -> {
            if(c) {
                return H.div();
            } else {
                return Alerts.error(I18N.CONSTANTS.requiredFieldsNotMapped());
            }
        }));
    }


    private static VTree columnPanel(ImportViewModel viewModel, ImportUpdater updater) {
        return new ReactiveComponent(viewModel.getSelectedColumnView().transform(column ->
                new SidePanel()
                        .hideMode(SidePanel.HideMode.NONE)
                        .header(H.h2(I18N.MESSAGES.columnHeading(column.getLabel())))
                        .content(
                                H.div("importer__choice",
                                        H.div("importer__choice__body", columnTargets(column, updater))))
                        .build()));
    }

    private static VTree columnTargets(SelectedColumnViewModel column, ImportUpdater updater) {

        return new VNode(HtmlTag.DIV, PropMap.EMPTY,
                H.div(H.h3(I18N.CONSTANTS.columnMatching())),
                probableMatchList(column, updater),
                ignoreRadioButton(column, updater),
                remainingFields(column, updater));
    }


    /**
     * Display the best matching fields at the top of list with a
     * "probable matches" heading.
     */
    private static VTree probableMatchList(SelectedColumnViewModel column, ImportUpdater updater) {
        List<ScoredColumnTarget> bestMatches = column.getTargets().getNameMatches();
        if(bestMatches.isEmpty()) {
            return H.div();
        }

        List<VTree> children = new ArrayList<>();
        children.add(H.h4(I18N.CONSTANTS.probableMatches()));
        for (ScoredColumnTarget scoredTarget : bestMatches) {
            children.add(targetRadioButton(column, scoredTarget.getTarget(), updater));
        }
        return new VNode(HtmlTag.FIELDSET,
                Props.withClass("importer__best"), children);
    }


    private static VTree remainingFields(SelectedColumnViewModel column, ImportUpdater updater) {
        List<VTree> radios = new ArrayList<>();
        for (ScoredColumnTarget scoredTarget : column.getTargets().getOther()) {
            radios.add(targetRadioButton(column, scoredTarget.getTarget(), updater));
        }
        return new VNode(HtmlTag.FIELDSET, Props.withClass("importer__other"), radios);
    }

    private static VTree targetRadioButton(SelectedColumnViewModel column, ColumnTarget target, ImportUpdater updater) {
        return new RadioButton()
                .label(target.getLabel())
                .name("radio-" + column.getId())
                .checked(column.isSelected(target))
                .onchange(event -> onTargetSelected(column, target, event, updater))
                .render();
    }


    private static VTree ignoreRadioButton(SelectedColumnViewModel column, ImportUpdater updater) {
        return new RadioButton()
                .label(I18N.CONSTANTS.ignoreColumnAction())
                .name("radio-ignore-column")
                .checked(column.isIgnored())
                .onchange(event -> onIgnoreSelected(column, event, updater))
                .render();
    }

    private static void onTargetSelected(SelectedColumnViewModel column, ColumnTarget target, Event event, ImportUpdater updater) {
        InputElement input = event.getEventTarget().cast();
        if(input.isChecked()) {
            updater.update(s -> s.updateMappings(m -> target.apply(m, column.getId())));
        }
    }

    private static void onIgnoreSelected(SelectedColumnViewModel column, Event event, ImportUpdater updater) {
        InputElement input = event.getEventTarget().cast();
        if(input.isChecked()) {
            updater.update(s -> s.updateMappings(m -> m.withColumnIgnored(column.getId())));
        }
    }
}
