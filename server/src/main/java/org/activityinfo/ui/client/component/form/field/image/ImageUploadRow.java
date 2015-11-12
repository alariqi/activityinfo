package org.activityinfo.ui.client.component.form.field.image;
/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;
import org.activityinfo.core.shared.util.MimeTypeUtil;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.legacy.shared.Log;
import org.activityinfo.model.resource.Resource;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.resource.Resources;
import org.activityinfo.model.type.image.ImageRowValue;
import org.activityinfo.service.blob.UploadCredentials;
import org.activityinfo.ui.client.component.form.field.FieldWidgetMode;

import java.util.List;
import java.util.Map;

/**
 * @author yuriyz on 8/12/14.
 */
public class ImageUploadRow extends Composite {

    private static final int THUMBNAIL_SIZE = 24;

    public interface ValueChangedCallback {
        void fireValueChanged();
    }

    interface OurUiBinder extends UiBinder<FormPanel, ImageUploadRow> {
    }

    private static OurUiBinder ourUiBinder = GWT.create(OurUiBinder.class);

    private final ImageRowValue value;
    private final ImageUploadRow.ValueChangedCallback valueChangedCallback;

    private boolean readOnly;
    private HandlerRegistration oldHandler;

    @UiField
    FileUpload fileUpload;
    @UiField
    HTMLPanel imageContainer;
    @UiField
    ImageElement loadingImage;
    @UiField
    Image thumbnail;
    @UiField
    Button downloadButton;
    @UiField
    Button removeButton;
    @UiField
    VerticalPanel formFieldsContainer;
    @UiField
    Button addButton;
    @UiField
    FormPanel formPanel;
    @UiField
    HTMLPanel uploadFailed;

    public ImageUploadRow(ImageRowValue value, String fieldId, String resourceId,
                          final FieldWidgetMode fieldWidgetMode, ImageUploadRow.ValueChangedCallback valueChangedCallback) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.value = value;
        this.valueChangedCallback = valueChangedCallback;

        fileUpload.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (fieldWidgetMode == FieldWidgetMode.NORMAL) { // todo uncomment !!!
                    requestUploadUrl();
                } else {
                    Window.alert(I18N.CONSTANTS.uploadIsNotAllowedInDuringDesing());
                }
            }
        });
        downloadButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                download();
            }
        });

        if (value.getBlobId() != null) {
            imageContainer.setVisible(false);
            downloadButton.setVisible(true);
            thumbnail.setVisible(true);
            thumbnail.setUrl(buildThumbnailUrl());
        }
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        fileUpload.setEnabled(!readOnly);
        downloadButton.setEnabled(!readOnly);
        removeButton.setEnabled(!readOnly);
        addButton.setEnabled(!readOnly);
    }

    private String createUploadUrl() {
        String blobId = ResourceId.generateId().asString();
        String fileName = fileName();
        String mimeType = MimeTypeUtil.mimeTypeFromFileName(fileName);
        value.setMimeType(mimeType);
        value.setFilename(fileName);
        value.setBlobId(blobId);
        return "/service/blob/credentials/" + blobId;
    }

    private String fileName() {
        final String filename = fileUpload.getFilename();
        if (Strings.isNullOrEmpty(filename)) {
            return "unknown";
        }

        int i = filename.lastIndexOf("/");
        if (i == -1) {
            i = filename.lastIndexOf("\\");
        }
        if (i != -1 && (i + 1) < filename.length()) {
            return filename.substring(i + 1);
        }
        return filename;
    }

    private void requestUploadUrl() {
        uploadFailed.setVisible(false);
        try {
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, URL.encode(createUploadUrl()));
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {

                    Resource resource = Resources.fromJson(response.getText());
                    UploadCredentials uploadCredentials = UploadCredentials.fromRecord(resource);

                    removeHiddenFieldsFromForm();

                    Map<String, String> formFields = uploadCredentials.getFormFields();
                    for (Map.Entry<String, String> field : formFields.entrySet()) {
                        formFieldsContainer.add(new Hidden(field.getKey(), field.getValue()));
                    }

                    formPanel.setAction(uploadCredentials.getUrl());
                    formPanel.setMethod(uploadCredentials.getMethod());
                    upload();
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Log.error("Failed to send request", exception);
                    uploadFailed.setVisible(true);
                }
            });
        } catch (RequestException e) {
            Log.error("Failed to send request", e);
            uploadFailed.setVisible(true);
        }
    }

    private void removeHiddenFieldsFromForm() {
        List<Hidden> hidden = Lists.newArrayListWithCapacity(formFieldsContainer.getWidgetCount());
        for (int i = 0; i < formFieldsContainer.getWidgetCount(); i++) {
            Widget widget = formFieldsContainer.getWidget(i);
            if (widget instanceof Hidden) {
                hidden.add((Hidden) widget);
            }
        }

        for (Hidden old : hidden) {
            formFieldsContainer.remove(old);
        }
    }

    private void upload() {
        imageContainer.setVisible(true);
        downloadButton.setVisible(false);
        uploadFailed.setVisible(false);

        if (oldHandler != null) {
            oldHandler.removeHandler();
        }

        oldHandler = formPanel.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {

                // in dev mode it is always null. https://code.google.com/p/google-web-toolkit/issues/detail?id=3832
                // it's expected to get something like this in prod mode:
                //<?xml version='1.0' encoding='UTF-8'?><PostResponse><Location>http://commondatastorage.googleapis.com/ai-blob-test/cignpyj1u2</Location><Bucket>ai-blob-test</Bucket><Key>cignpyj1u2</Key><ETag>"c22e1a55397546672130f31761892f55"</ETag></PostResponse>
                //<StringToSign>eyJjb25kaXRpb25zIjpbeyJzdWNjZXNzX2FjdGlvbl9zdGF0dXMiOiIyMDEifSx7ImJ1Y2tldCI6ImFpLWJsb2ItdGVzdCJ9LHsia2V5IjoiY2lnbm4xcHYwMiJ9LFsiY29udGVudC1sZW5ndGgtcmFuZ2UiLDAsMTA0ODU3NjAwXV0sImV4cGlyYXRpb24iOiIyMDE1LTExLTA2VDE0OjM4OjU2LjI4NyswMjowMCJ9</StringToSign></Error>

                Optional<String> location = location(event.getResults());
                if (!GWT.isProdMode() || location.isPresent()) {
                    imageContainer.setVisible(false);
                    downloadButton.setVisible(true);
                    thumbnail.setVisible(true);
                    thumbnail.setUrl(buildThumbnailUrl());

                    valueChangedCallback.fireValueChanged();
                } else {
                    Log.error("Failed to parse response from server: " + event.getResults());
                    uploadFailed.setVisible(true);
                }
            }
        });
        formPanel.submit();
    }

    private Optional<String> location(String xmlResponse) {
        try {
            Log.debug(xmlResponse);
            Document dom = XMLParser.parse(xmlResponse);
            return Optional.of(dom.getElementsByTagName("Location").item(0).getNodeValue());
        } catch (Exception e) {
            return Optional.absent();
        }
    }

    private void download() {
        try {
            RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, buildBaseUrl() + "/image_url");
            requestBuilder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    String imageServingUrl = response.getText();

                    Window.open(imageServingUrl, "_blank", null);
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    Log.error("Failed to send request", exception);
                }
            });
        } catch (RequestException e) {
            Log.error("Failed to send request", e);
        }
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    public ImageRowValue getValue() {
        return value;
    }

    private String buildBaseUrl() {
        return "/service/blob/" + value.getBlobId();
    }

    private String buildThumbnailUrl() {
        return buildBaseUrl() + "/thumbnail?width=" + THUMBNAIL_SIZE + "&height=" + THUMBNAIL_SIZE;
    }
}
