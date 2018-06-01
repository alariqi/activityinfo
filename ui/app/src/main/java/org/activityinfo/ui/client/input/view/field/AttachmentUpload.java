package org.activityinfo.ui.client.input.view.field;

import com.google.common.base.Strings;
import elemental2.dom.Event;
import elemental2.dom.File;
import elemental2.dom.FormData;
import elemental2.dom.XMLHttpRequest;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.attachment.Attachment;
import org.activityinfo.store.spi.BlobId;

public class AttachmentUpload {

    private final BlobId blobId;
    private final XMLHttpRequest request;

    public AttachmentUpload(ResourceId formId, Attachment attachment, File file, AttachmentFileWidget widget) {

        blobId = BlobId.generate();

        FormData formData = new FormData();
        formData.append("file", file);
        formData.append("blobId", blobId.asString());
        formData.append("mimeType", mimeType(file));
        formData.append("fileName", file.name);
        formData.append("resourceId", formId.asString());

        request = new XMLHttpRequest();
        request.open("POST", "/service/appengine");
        request.upload.onprogress = event -> {
            if(event.lengthComputable) {
                widget.setUploadProgress(event.loaded, event.total);
            }
        };
        request.onreadystatechange = event -> {
            if(request.readyState == XMLHttpRequest.DONE) {
                switch (request.status) {
                    case 200:
                        widget.setUploadComplete();
                        break;
                    default:
                        widget.setUploadFailed();
                        break;
                }
            }
            return true;
        };
        request.onerror = new XMLHttpRequest.OnerrorFn() {
            @Override
            public Object onInvoke(Event event) {
                widget.setUploadFailed();
                return true;
            }
        };
        request.send(formData);

        widget.setUploadProgress(0, file.size);
    }

    private String mimeType(File file) {
        if(Strings.isNullOrEmpty(file.type)) {
            return "application/octet-stream";
        } else {
            return file.type;
        }
    }

    public void cancel() {
        request.abort();
    }
}
