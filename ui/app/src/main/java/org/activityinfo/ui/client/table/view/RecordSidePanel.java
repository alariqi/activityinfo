package org.activityinfo.ui.client.table.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.observable.MaybeStale;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.base.tabs.TabItem;
import org.activityinfo.ui.client.base.tabs.Tabs;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.client.table.model.TableUpdater;
import org.activityinfo.ui.client.table.viewModel.TableViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.Optional;
import java.util.stream.Stream;

import static org.activityinfo.ui.client.base.button.Buttons.button;
import static org.activityinfo.ui.vdom.shared.html.H.div;
import static org.activityinfo.ui.vdom.shared.tree.Props.withClass;

public class RecordSidePanel {

    public static VTree render(TableViewModel viewModel, TableUpdater tableUpdater) {
        VTree tree = Tabs.tabPanel(
                new TabItem(I18N.CONSTANTS.details(), details(viewModel, tableUpdater)),
                new TabItem(I18N.CONSTANTS.history(), history(viewModel)));

        return new SidePanel()
                .expandButtonLabel(I18N.CONSTANTS.detailsHistory())
                .header(scrollButton(viewModel))
                .expanded(viewModel.isRecordPanelExpanded(), expanded -> tableUpdater.expandRecordPanel(expanded))
                .content(tree)
                .build();
    }

    private static VTree scrollButton(TableViewModel viewModel) {

        Observable<Boolean> hasSelection = viewModel.hasSelection().optimisticWithDefault(false);

        return new ReactiveComponent("scrollto",
                hasSelection.transform(e ->
                    new VNode(HtmlTag.BUTTON, withClass("details__scrollto").disabled(!e),
                        new VText(I18N.CONSTANTS.scrollToThisRecord()))));
    }

    private static VTree details(TableViewModel viewModel, TableUpdater updater) {

        // Go to additional lengths to avoid flicker when changing record selection.
        // This ensures that the selectionDetails component does not have to be unmounted/mounted every time
        // the selection changes, and can gracefully handle its transitions between records.

        VTree selection = selectionDetails(viewModel, updater);
        VTree noSelection = noSelection();

        Observable<Boolean> hasSelection = viewModel.hasSelection().optimistic();

        return new ReactiveComponent("details",
                hasSelection.transform(b ->
                        new VNode(HtmlTag.DIV, withClass("details"),
                                b ? selection : noSelection)));
    }

    private static VTree selectionDetails(TableViewModel viewModel, TableUpdater tableUpdater) {
        return div("details__record",
                    parentLinks(viewModel),
                    subformLinks(viewModel),
                    recordHeader(tableUpdater),
                    recordDetails(viewModel));
    }

    private static VTree history(TableViewModel viewModel) {
        return div("details__history",
                historyHeader(),
                historyContent(viewModel));
    }


    private static VNode historyHeader() {
        return H.h2(I18N.CONSTANTS.recordHistory());
    }

    private static VTree historyContent(TableViewModel viewModel) {
        return new ReactiveComponent("historyContent", viewModel.getSelectedRecordHistory()
                .transform(HistoryRenderer::render));
    }

    private static VTree parentLinks(TableViewModel viewModel) {
        FormTree tree = viewModel.getFormTree();
        FormClass form = tree.getRootFormClass();
        if (form.isSubForm()) {
            Optional<FormClass> parentForm = tree.getFormClassIfPresent(form.getParentFormId().get()).toJavaUtil();
            if (parentForm.isPresent()) {
                return H.div("details__subforms",
                        H.h2(I18N.CONSTANTS.goBackTo()),
                        parentLink(parentForm.get()));
            }
        }
        return new VNode(HtmlTag.DIV);
    }

    private static VTree subformLinks(TableViewModel viewModel) {

        if(viewModel.getSubForms().isEmpty()) {
            return new VNode(HtmlTag.DIV);
        } else {
            Observable<RecordRef> selectedRecordRef = Observable.flattenUtilOptional(viewModel.getSelectedRecordRef());
            return new ReactiveComponent(selectedRecordRef.transform(ref -> {
                return new VNode(HtmlTag.DIV,
                        withClass("details__subforms"),
                        new VNode(HtmlTag.H2, I18N.CONSTANTS.goToSubforms()),
                        new VNode(HtmlTag.DIV, PropMap.EMPTY, subFormLinks(viewModel, ref)));
            }));

        }
    }

    private static Stream<VTree> subFormLinks(TableViewModel viewModel, RecordRef ref) {

        return viewModel.getSubForms().stream()
                .map(subForm -> {
                    TablePlace place = new TablePlace(subForm.getId(), ref);
                    return subFormLink(subForm, place);
                });
    }

    private static VTree recordHeader(TableUpdater tableUpdater) {
        VNode header = new VNode(HtmlTag.H2, I18N.CONSTANTS.thisRecord());

        VTree editButton = button(I18N.CONSTANTS.edit())
                .icon(Icon.BUBBLE_EDIT)
                .onSelect(event -> tableUpdater.editSelection())
                .build();

        VTree deleteButton = button(I18N.CONSTANTS.delete())
                .icon(Icon.BUBBLE_CLOSE)
                .onSelect(event -> tableUpdater.editSelection())
                .build();

        return new VNode(HtmlTag.DIV, withClass("details__recordheader"),
                header,
                editButton,
                deleteButton);
    }

    private static VTree recordDetails(TableViewModel viewModel) {
        DetailsRenderer renderer = new DetailsRenderer(viewModel.getFormTree());

        Observable<MaybeStale<RecordTree>> selection = Observable
                .flattenUtilOptional(viewModel.getSelectedRecordTree())
                .explicitlyOptimistic();

        Observable<VTree> details = selection.transform(s -> renderer.render(s.getValue(), s.isStale()));

        return new ReactiveComponent("details.values", details, DetailsRenderer.renderPlaceholder());
    }

    private static VTree noSelection() {
        return div("details__empty",
                NonIdeal.empty(),
                H.h2(I18N.CONSTANTS.noRecordSelected()),
                H.p(I18N.CONSTANTS.pleaseSelectARecord()));
    }

    private static VTree parentLink(FormClass parentForm) {
        return Buttons.button(parentForm.getLabel())
                .primary()
                .block()
                .link(new TablePlace(parentForm.getId()).toUri())
                .build();
    }


    private static VTree subFormLink(FormClass subForm, TablePlace place) {
        return Buttons.button(subForm.getLabel())
                .primary()
                .block()
                .icon(Icon.BUBBLE_SUBFORM)
                .link(place.toUri())
                .build();
    }

}
