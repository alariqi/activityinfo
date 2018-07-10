package org.activityinfo.ui.client.table.view;

import org.activityinfo.analysis.table.EffectiveTableColumn;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.analysis.view.FieldListView;
import org.activityinfo.ui.client.analysis.viewModel.FieldListViewModel;
import org.activityinfo.ui.client.analysis.viewModel.SelectedFieldViewModel;
import org.activityinfo.ui.client.base.side.SidePanel;
import org.activityinfo.ui.client.table.model.TableUpdater;
import org.activityinfo.ui.client.table.viewModel.TableViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.stream.Collectors;

import static org.activityinfo.ui.vdom.shared.html.H.div;
import static org.activityinfo.ui.vdom.shared.html.H.h3;

public class ColumnOptionsPanel {

    public static VTree render(TableViewModel tableViewModel, TableUpdater tableUpdater) {

        return new ReactiveComponent(tableViewModel.isColumnOptionsVisible().transform(visible -> {
            if(visible) {
                return renderOptions(tableViewModel, tableUpdater);
            } else {
                return H.div();
            }
        }));
    }

    private static VTree renderOptions(TableViewModel tableViewModel, TableUpdater updater) {

        Observable<FieldListViewModel> listViewModel = tableViewModel
                .getFormTree()
                .transform(FieldListViewModel::formFields);

        VTree fieldList = div("fieldlist",
                div("fieldlist__header",
                        h3(I18N.CONSTANTS.fields()),
                        FieldListView.fieldList(listViewModel)));

        VTree selected = div("dimensions",
                H.h3(I18N.CONSTANTS.selectedColumns()),
                selectedList(tableViewModel));

        return new SidePanel()
                .expanded(true, expanded -> updater.showColumnOptions(expanded))
                .title(new VText(I18N.CONSTANTS.columns()))
                .header(H.h2(I18N.CONSTANTS.columns()))
                .expandedWidth(2)
                .leftSide()
                .content(div("pivotdesign", fieldList, selected))
                .build();
    }

    private static VTree selectedList(TableViewModel tableViewModel) {
        return FieldListView.fieldList(tableViewModel.getEffectiveTable().transform(
                table -> new FieldListViewModel(table.getColumns()
                        .stream()
                        .map(ColumnOptionsPanel::asField)
                        .collect(Collectors.toList()))));
    }

    private static SelectedFieldViewModel asField(EffectiveTableColumn column) {
        String form = I18N.CONSTANTS.thisForm();

        if (column.getFormula().isSimpleReference()) {
            return new SelectedFieldViewModel(form, column.getType(), column.getLabel());
        } else {
            return new SelectedFieldViewModel(form, column.getType(), column.getLabel());
        }
    }
}
