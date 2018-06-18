package org.activityinfo.ui.client.table.view;

import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.analysis.table.TableUpdater;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormClass;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.type.subform.SubFormReferenceType;
import org.activityinfo.observable.MaybeStale;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.client.base.tabs.TabItem;
import org.activityinfo.ui.client.base.tabs.Tabs;
import org.activityinfo.ui.vdom.client.VDomWidget;
import org.activityinfo.ui.vdom.shared.html.CssClass;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;

import static org.activityinfo.ui.client.base.button.Buttons.button;
import static org.activityinfo.ui.vdom.shared.html.H.div;

public class RecordView implements IsWidget {


    private final VDomWidget content;

    public RecordView(TableViewModel viewModel, TableUpdater tableUpdater) {
        content = new VDomWidget();
        content.addStyleName("details");

        VTree tree = Tabs.tabPanel(
                new TabItem(I18N.CONSTANTS.details(), details(viewModel, tableUpdater)),
                new TabItem(I18N.CONSTANTS.history(), history(viewModel)));

        content.update(tree);
    }

    private VTree details(TableViewModel viewModel, TableUpdater updater) {

        // Go to additional lengths to avoid flicker when changing record selection.
        // This ensures that the selectionDetails component does not have to be unmounted/mounted every time
        // the selection changes, and can gracefully handle its transitions between records.

        VTree selection = selectionDetails(viewModel, updater);
        VTree noSelection = noSelection();

        Observable<Boolean> hasSelection = viewModel.hasSelection().optimistic();

        return new ReactiveComponent("details",
                hasSelection.transform(b -> b ? selection : noSelection));
    }

    private VTree selectionDetails(TableViewModel viewModel, TableUpdater tableUpdater) {
        return div("details__record",
                subFormNavigation(viewModel),
                recordHeader(tableUpdater),
                recordDetails(viewModel));
    }

    private VTree history(TableViewModel viewModel) {
        return div(CssClass.valueOf("details__history"),
                historyHeader(),
                historyContent(viewModel));
    }


    private VNode historyHeader() {
        return H.h2(I18N.CONSTANTS.recordHistory());
    }

    private VTree historyContent(TableViewModel viewModel) {
        return new ReactiveComponent("historyContent", viewModel.getSelectedRecordHistory()
                .transform(HistoryRenderer::render));
    }

    private VTree subFormNavigation(TableViewModel viewModel) {
        return new ReactiveComponent("subFormNavigation", viewModel.getFormTree().transform(tree -> {

            List<VTree> subFormLinks = new ArrayList<>();

            for (FormTree.Node node : tree.getRootFields()) {
                if(node.isSubForm() && node.isSubFormVisible()) {
                    SubFormReferenceType subFormType = (SubFormReferenceType) node.getType();
                    FormClass subForm = tree.getFormClass(subFormType.getClassId());

                    subFormLinks.add(subFormLink(subForm));
                }
            }

            if(subFormLinks.isEmpty()) {
                return new VNode(HtmlTag.DIV);
            } else {
                return new VNode(HtmlTag.DIV,
                        new VNode(HtmlTag.H3, "Go to subform"),
                        new VNode(HtmlTag.DIV, PropMap.EMPTY, subFormLinks));

            }
        }));
    }

    private VTree recordHeader(TableUpdater tableUpdater) {
        VNode header = new VNode(HtmlTag.H2, I18N.CONSTANTS.thisRecord());

        VTree editButton = button(I18N.CONSTANTS.edit())
                .icon(Icon.BUBBLE_EDIT)
                .onSelect(event -> tableUpdater.editSelection())
                .build();

        VTree deleteButton = button(I18N.CONSTANTS.delete())
                .icon(Icon.BUBBLE_CLOSE)
                .onSelect(event -> tableUpdater.editSelection())
                .build();

        return new VNode(HtmlTag.DIV, PropMap.withClasses("details__recordheader"),
                header,
                editButton,
                deleteButton);
    }

    private VTree recordDetails(TableViewModel viewModel) {

        Observable<DetailsRenderer> renderer = viewModel.getFormTree().transform(DetailsRenderer::new);

        Observable<MaybeStale<RecordTree>> selection = Observable
                .flattenOptional(viewModel.getSelectedRecordTree())
                .explicitlyOptimistic();

        Observable<VTree> details = Observable.transform(renderer, selection, (r, s) -> r.render(s.getValue(), s.isStale()));

        return new ReactiveComponent("details.values", details);
    }

    private VTree noSelection() {
        return div("details__empty",
                NonIdeal.empty(),
                H.h2(I18N.CONSTANTS.noRecordSelected()),
                H.p(I18N.CONSTANTS.pleaseSelectARecord()));
    }


    private VTree subFormLink(FormClass subForm) {
        SafeUri link = UriUtils.fromSafeConstant("#");
        return new VNode(HtmlTag.A, PropMap.withClasses("button button--primary button--subform").href(link),
                Icon.BUBBLE_ARROWRIGHT.tree(),
                new VNode(HtmlTag.SPAN, subForm.getLabel()),
                new VNode(HtmlTag.SPAN, PropMap.withClasses("button__recordcount"),
                        new VText("... records")));
    }

    @Override
    public Widget asWidget() {
        return content;
    }
}
