package org.activityinfo.ui.client.base.info;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.info.InfoConfig;
import org.activityinfo.i18n.shared.I18N;

public class SuccessConfig extends InfoConfig {

    private String message;

    public SuccessConfig(String message) {
        this.message = message;
        setWidth(400);
        setHeight(95);
        setPosition(InfoConfig.InfoPosition.BOTTOM_RIGHT);
        setDisplay(5000);
    }

    @Override
    protected SafeHtml render(Info info) {
        return InfoTemplates.TEMPLATE.renderSuccess(I18N.CONSTANTS.success(), message);

    }
}
