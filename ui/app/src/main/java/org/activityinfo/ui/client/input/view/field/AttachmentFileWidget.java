package org.activityinfo.ui.client.input.view.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import org.activityinfo.model.type.attachment.Attachment;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.container.StaticHtml;

public class AttachmentFileWidget implements IsWidget {

    interface Templates extends XTemplates {

        @XTemplate(source = "AttachmentFile.html")
        SafeHtml template(String name, String ext);
    }

    private static final Templates TEMPLATES = GWT.create(Templates.class);

    private final StaticHtml html;

    public AttachmentFileWidget(Attachment attachment) {
        String filename = attachment.getFilename();
        int extStart = filename.lastIndexOf('.');
        String base = filename.substring(0, extStart);
        String ext = filename.substring(extStart);

        html = new StaticHtml(TEMPLATES.template(base, ext));
    }

    public Widget asWidget() {
        return html;
    }

    private void setButtonIcon(Icon icon) {
        XElement removeButton = html.getElement().selectNode(".forminput__field__attachment__file__remove");
        removeButton.setInnerSafeHtml(icon.render());
    }

    public void setUploadProgress(double loaded, double total) {
        XElement progress = getProgressElement();
        progress.getStyle().setDisplay(Style.Display.BLOCK);
        DivElement bar = progress.getFirstChildElement().cast();
        bar.getStyle().setWidth((loaded/total)*100, Style.Unit.PCT);
    }

    private XElement getProgressElement() {
        return html.getElement().selectNode(".forminput__field__attachment__file__progress");
    }

    public void setUploadComplete() {
        getProgressElement().getStyle().setDisplay(Style.Display.NONE);
        setButtonIcon(Icon.BUBBLE_CHECKMARK_SUCCESS);
        Scheduler.get().scheduleFixedDelay(() -> {
            setButtonIcon(Icon.BUBBLE_CLOSE);
            return false;
        }, 2500);
    }

    public void setUploadFailed() {
        getProgressElement().getStyle().setDisplay(Style.Display.NONE);
        setButtonIcon(Icon.BUBBLE_CLOSE_ERROR);
    }
}
