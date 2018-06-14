package org.activityinfo.ui.client.base.info;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.info.InfoConfig;
import org.activityinfo.api.client.ApiException;
import org.activityinfo.i18n.shared.I18N;

public class ErrorConfig extends InfoConfig {

    protected SafeHtml title = SafeHtmlUtils.fromString(I18N.CONSTANTS.error());
    protected SafeHtml message = SafeHtmlUtils.EMPTY_SAFE_HTML;

    public ErrorConfig(Throwable caught) {
        this(messageFromException(caught));
    }

    private static String messageFromException(Throwable caught) {
        if(caught instanceof ApiException && caught.getMessage().equals("0")) {
            return I18N.CONSTANTS.saveFailedConnectionProblem();
        }
        return I18N.CONSTANTS.errorOnServer();
    }

    public ErrorConfig(String message) {
        this(SafeHtmlUtils.fromString(message));
    }

    public ErrorConfig(SafeHtml message) {
        this.message = message;
        setWidth(400);
        setHeight(95);
        setMargin(30);
        setPosition(InfoConfig.InfoPosition.BOTTOM_RIGHT);
    }

    @Override
    protected SafeHtml render(Info info) {
        return InfoTemplates.TEMPLATE.renderError(title, message);
    }
}
