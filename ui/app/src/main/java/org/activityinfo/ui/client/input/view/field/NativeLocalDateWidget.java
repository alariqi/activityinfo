package org.activityinfo.ui.client.input.view.field;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.TextInputCell;
import com.sencha.gxt.widget.core.client.form.TextField;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.time.LocalDate;
import org.activityinfo.ui.client.base.field.Css3TextFieldAppearance;
import org.activityinfo.ui.client.input.model.FieldInput;

public class NativeLocalDateWidget implements FieldWidget {

    private TextField field;

    public NativeLocalDateWidget(FieldUpdater fieldUpdater) {
        this.field = new TextField(new TextInputCell(new Css3TextFieldAppearance("date", null)));
        this.field.addValueChangeHandler(event -> fieldUpdater.update(input()));
        this.field.addBlurHandler(event -> fieldUpdater.touch());
        this.field.setWidth(-1);
    }

    private FieldInput input() {
        if(field.isValid()) {
            if(field.getValue() == null) {
                return FieldInput.EMPTY;
            } else {
                return new FieldInput(LocalDate.parse(field.getValue()));
            }
        } else {
            return FieldInput.INVALID_INPUT;
        }
    }

    @Override
    public void init(FieldValue value) {
        field.setValue(((LocalDate) value).toString());
    }

    @Override
    public void clear() {
        field.clear();
    }

    @Override
    public void setRelevant(boolean relevant) {
        field.setEnabled(relevant);
    }

    @Override
    public void focus() {
        field.focus();
    }

    @Override
    public Widget asWidget() {
        return field;
    }

}
