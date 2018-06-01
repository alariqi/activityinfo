package org.activityinfo.ui.client.base.field;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.FileList;

import java.util.logging.Logger;

/**
 * A non-visible file field that can be used to select files.
 */
public class FilePicker extends Widget {

    private static final Logger LOGGER = Logger.getLogger(FilePicker.class.getName());

    public interface FileSelectionHandler {

        void onSelected(FileList fileList);
    }

    private final InputElement input;

    public FilePicker() {
        input = (InputElement) Document.get().createElement("input");
        input.setAttribute("type","file");
        input.getStyle().setVisibility(Style.Visibility.HIDDEN);
        setElement(input);
    }

    public void setMultiple(boolean multiple) {
        if(multiple) {
            input.setAttribute("multiple", "multiple");
        } else {
            input.setAttribute("multiple", "");
        }
    }

    public void browse() {
        click(input);
    }

    public HandlerRegistration addFileSelectedHandler(FileSelectionHandler handler) {
        return addDomHandler(event -> {
            FileList fileList = getFileList(input);
            if(fileList.length > 0) {
                handler.onSelected(fileList);
            }
        }, ChangeEvent.getType());
    }

    private static native FileList getFileList(Element input) /*-{
        return input.files;
    }-*/;

    private static native void click(Element input) /*-{
        input.click();
    }-*/;
}
