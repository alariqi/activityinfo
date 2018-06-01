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
package org.activityinfo.ui.client.upload;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.attachment.Attachment;

import javax.annotation.Nullable;

public class Uploader implements IsWidget {

    public enum Status {
        IDLE,
        UPLOADING,
        FAILED
    }

    public interface UploadCallback {
        void onFailure(@Nullable Throwable exception);

        void upload();
    }

    private final FileUpload fileUpload;
    private final FormPanel formPanel;
    private final UploadCallback uploadCallback;

    private Attachment attachment = new Attachment();

    public Uploader(UploadCallback uploadCallback) {

        this.fileUpload = new FileUpload();
        this.fileUpload.addChangeHandler(event -> {
            setAttachment(new Attachment());
            upload();
        });

        this.formPanel = new FormPanel();
        this.formPanel.setEncoding("multipart/form-data");
        this.formPanel.setMethod("post");
        this.formPanel.add(fileUpload);
        this.formPanel.setVisible(false);

        this.uploadCallback = uploadCallback;
    }

    @Override
    public Widget asWidget() {
        return formPanel;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public void upload() {
        uploadViaAppEngine();
    }

    private void uploadViaAppEngine() {
//        hiddenFieldsContainer.clear();
//
//        newAttachment();
//
//        hiddenFieldsContainer.add(new Hidden("blobId", attachment.getBlobId()));
//        hiddenFieldsContainer.add(new Hidden("fileName", attachment.getFilename()));
//        hiddenFieldsContainer.add(new Hidden("mimeType", attachment.getMimeType()));
//        hiddenFieldsContainer.add(new Hidden("resourceId", resourceId.asString()));

        formPanel.setAction("/service/appengine");
        formPanel.setMethod("POST");
        uploadCallback.upload();
    }

    public String getBaseUrl() {
        return getBaseUrl(attachment.getBlobId(), ResourceId.generateId());
    }


    private static native String mimeType(InputElement element) /*-{
        return element.files[0].type;
    }-*/;

    public void browse() {
        triggerUpload(fileUpload.getElement());
    }

    private static native void triggerUpload(Element element) /*-{
        element.click();
    }-*/;

    public static String getBaseUrl(String blobId, ResourceId formId) {
        return "/service/blob/" + blobId + "/" + formId.asString();
    }
}
