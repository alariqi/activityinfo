package chdc.frontend.client.cheatsheet;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import org.activityinfo.model.expr.ExprNode;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.ReferenceValue;
import org.activityinfo.observable.Observable;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.input.view.field.FieldUpdater;
import org.activityinfo.ui.client.input.view.field.FieldWidget;
import org.activityinfo.ui.client.lookup.viewModel.LookupKeyViewModel;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;

public class CheatsheetField implements FieldWidget {


    private final LookupViewModel viewModel;
    private final CheatsheetCombo combo;
    private final FieldUpdater fieldUpdater;

    public CheatsheetField(FormSource formSource, FormTree formTree, FormField field, FieldUpdater fieldUpdater) {
        this.fieldUpdater = fieldUpdater;
        Observable<Optional<ExprNode>> filter = Observable.just(Optional.absent());
        this.viewModel = new LookupViewModel(formSource, new LookupKeySet(formTree, field), filter);
        LookupKeyViewModel nameKeyViewModel = this.viewModel.getLookupKeys().get(0);

        combo = new CheatsheetCombo(viewModel, nameKeyViewModel);
        combo.addTriggerHandler(new TriggerClickEvent.TriggerClickHandler() {
            @Override
            public void onTriggerClick(TriggerClickEvent event) {
                showDialog();
            }
        });

    }

    private void showDialog() {

        CheatsheetDialog dialog = new CheatsheetDialog(viewModel);
        dialog.show();


    }

    @Override
    public void init(FieldValue value) {
        ReferenceValue referenceValue = (ReferenceValue) value;
        viewModel.setInitialSelection(referenceValue.getReferences());
    }

    @Override
    public void clear() {
        viewModel.clearSelection();
    }

    @Override
    public void setRelevant(boolean relevant) {
    }

    @Override
    public Widget asWidget() {
        return combo.asWidget();
    }
}
