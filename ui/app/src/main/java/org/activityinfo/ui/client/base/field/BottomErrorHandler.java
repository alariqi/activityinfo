package org.activityinfo.ui.client.base.field;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.error.ErrorHandler;

import java.util.List;

public class BottomErrorHandler implements ErrorHandler {

    private Widget target;
    private DivElement message;

    public BottomErrorHandler(Widget target) {
        this.target = target;
    }

    @Override
    public void markInvalid(List<EditorError> errors) {
        if(message == null) {
            message = Document.get().createDivElement();
            message.addClassName("field__errormessage");
            target.getElement().appendChild(message);
        }
        message.setInnerText(errors.get(0).getMessage());
    }

    @Override
    public void clearInvalid() {
    }


    @Override
    public void release() {
    }
}
