package chdc.frontend.client.entry;

import com.google.common.base.Strings;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.TextInputCell;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.activityinfo.model.form.FormField;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.time.LocalDate;
import org.activityinfo.ui.client.input.model.FieldInput;
import org.activityinfo.ui.client.input.view.field.FieldUpdater;
import org.activityinfo.ui.client.input.view.field.FieldWidget;

public class NativeDateWidget implements FieldWidget {

    private final TextField textField;

    public NativeDateWidget(FormField field, FieldUpdater updater) {
        textField = new TextField(new TextInputCell(new TextAppearance(field.getLabel(), "date")));
        textField.addChangeHandler(event -> updater.update(input()));
    }

    private FieldInput input() {
        String textValue = textField.getValue();
        if(Strings.isNullOrEmpty(textValue)) {
            return FieldInput.EMPTY;
        } else {
            try {
                return new FieldInput(LocalDate.parse(textValue));
            } catch (Exception e) {
                return FieldInput.INVALID_INPUT;
            }
        }
    }

    @Override
    public void init(FieldValue value) {
        if(value instanceof LocalDate) {
            textField.setValue(value.toString());
        }
    }

    @Override
    public void clear() {
        textField.setValue(null);
    }

    @Override
    public void setRelevant(boolean relevant) {
    }

    @Override
    public Widget asWidget() {
        return textField;
    }

}
