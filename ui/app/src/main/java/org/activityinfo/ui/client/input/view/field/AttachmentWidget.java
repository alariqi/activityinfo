/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.input.view.field;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import elemental2.dom.File;
import elemental2.dom.FileList;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.model.type.attachment.Attachment;
import org.activityinfo.model.type.attachment.AttachmentValue;
import org.activityinfo.store.spi.BlobId;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.button.IconButton;
import org.activityinfo.ui.client.base.button.IconButtonStyle;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.base.field.FileDropZone;
import org.activityinfo.ui.client.base.field.FilePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AttachmentWidget implements FieldWidget {

    private static final Logger LOGGER = Logger.getLogger(AttachmentWidget.class.getName());

    private CssLayoutContainer container;

    private final Label emptyText;
    private final FieldUpdater valueUpdater;
    private final ResourceId formId;
    private final IconButton browseButton;
    private final CssLayoutContainer fileContainer;
    private final FilePicker filePicker;

    private List<AttachmentUpload> uploads = new ArrayList<>();
    private List<AttachmentFileWidget> files = new ArrayList<>();

    public AttachmentWidget(ResourceId formId, final FieldUpdater valueUpdater) {
        this.formId = formId;
        this.valueUpdater = valueUpdater;

        this.emptyText = new Label(I18N.CONSTANTS.emptyAttachmentText());
        this.emptyText.addStyleName("forminput__field__attachment__empty");

        filePicker = new FilePicker();
        filePicker.setMultiple(true);
        filePicker.addFileSelectedHandler(this::startUpload);

        fileContainer = new CssLayoutContainer();

        browseButton = new IconButton(Icon.BUBBLE_ATTACHMENT, IconButtonStyle.PRIMARY, I18N.CONSTANTS.browse());
        browseButton.addStyleName("forminput__field__attachment__browse");
        browseButton.addSelectHandler(event -> filePicker.browse());
        browseButton.addSelectHandler(event -> valueUpdater.touch());

        CssLayoutContainer buttons = new CssLayoutContainer();
        buttons.addStyleName("forminput__field__attachment__browse");
        buttons.add(browseButton);

        container = new CssLayoutContainer();
        container.addStyleName("forminput__field__attachment");
        container.add(emptyText);
        container.add(fileContainer);
        container.add(buttons);
        container.add(filePicker);

        FileDropZone dropZone = new FileDropZone(container);
        dropZone.addFileSelectedHandler(this::startUpload);
    }

    private void startUpload(FileList fileList) {
        for (int i = 0; i < fileList.length; i++) {
            File item = fileList.item(i);
            Attachment attachment = new Attachment(item.type, item.name, BlobId.generate().asString());
            AttachmentFileWidget widget = new AttachmentFileWidget(attachment);
            AttachmentUpload upload = new AttachmentUpload(formId, attachment, item, widget);

            files.add(widget);
            fileContainer.add(widget);
            uploads.add(upload);
        }
        emptyText.setVisible(false);
    }

    @Override
    public Widget asWidget() {
        return container;
    }


    @Override
    public void init(FieldValue value) {
        if(value instanceof AttachmentValue) {
            AttachmentValue attachmentValue = (AttachmentValue) value;
            for (Attachment attachment : attachmentValue.getValues()) {
                AttachmentFileWidget widget = new AttachmentFileWidget(attachment);
                files.add(widget);
                fileContainer.add(widget);
            }
        }
    }

    @Override
    public void setRelevant(boolean relevant) {

    }

    @Override
    public void clear() {
        fileContainer.clear();
        files.clear();
        for (AttachmentUpload upload : uploads) {
            upload.cancel();
        }
    }

}
