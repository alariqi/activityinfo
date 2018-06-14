package org.activityinfo.ui.client.base.info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

interface InfoTemplates extends XTemplates {


    InfoTemplates TEMPLATE = GWT.create(InfoTemplates.class);

    @XTemplate("<div class='info__header'>{title}</div><div class='info__message'>{message}</div>")
    SafeHtml render(SafeHtml title, SafeHtml message);

    @XTemplate("<div class='info__error'></div><div class='info__header'>{title}</div><div class='info__message'>{message}</div>")
    SafeHtml renderError(SafeHtml title, SafeHtml message);

    @XTemplate("<div class='info__success'></div><div class='info__header'>{title}</div><div class='info__message'>{message}</div>")
    SafeHtml renderSuccess(String title, String message);
}
