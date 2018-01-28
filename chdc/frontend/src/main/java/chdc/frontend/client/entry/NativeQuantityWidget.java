package chdc.frontend.client.entry;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.TextInputCell;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.number.Quantity;
import org.activityinfo.ui.client.input.model.FieldInput;
import org.activityinfo.ui.client.input.view.field.FieldUpdater;
import org.activityinfo.ui.client.input.view.field.FieldWidget;

public class NativeQuantityWidget implements FieldWidget {

    private final TextField textField;

    public NativeQuantityWidget(FormField field, FieldUpdater updater) {
        this.textField = new TextField(new TextInputCell(new TextAppearance(field.getLabel(), "number")));
        this.textField.addChangeHandler(event -> updater.update(input()));
    }

    private FieldInput input() {
        if(Strings.isNullOrEmpty(textField.getValue())) {
            return FieldInput.EMPTY;
        } else {
            try {
                return new FieldInput(new Quantity(Double.parseDouble(textField.getValue())));
            } catch (Exception e) {
                return FieldInput.INVALID_INPUT;
            }
        }
    }

    @Override
    public void init(FieldValue value) {
        textField.setValue(null);
    }

    @Override
    public void clear() {
    }

    @Override
    public void setRelevant(boolean relevant) {
    }

    @Override
    public Widget asWidget() {
        return textField;
    }
}
