package org.activityinfo.ui.client.base.field;

import com.google.gwt.dom.client.DataTransfer;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.FileList;

public class FileDropZone {

    private final Widget widget;

    public FileDropZone(Widget widget) {
        this.widget = widget;
    }

    public void addFileSelectedHandler(FilePicker.FileSelectionHandler handler) {

        HandlerRegistration overRegistration = widget.addDomHandler(new DragOverHandler() {
            @Override
            public void onDragOver(DragOverEvent event) {
                event.stopPropagation();
                event.preventDefault();

                event.getDataTransfer().setDropEffect(DataTransfer.DropEffect.COPY);
            }
        }, DragOverEvent.getType());

        HandlerRegistration dropRegistration = widget.addDomHandler(new DropHandler() {
            @Override
            public void onDrop(DropEvent event) {
                event.stopPropagation();
                event.preventDefault();

                handler.onSelected(getFileList(event.getDataTransfer()));
            }
        }, DropEvent.getType());
    }

    private static native FileList getFileList(DataTransfer transfer) /*-{
        return transfer.files;
    }-*/;
}
