package org.activityinfo.dev.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.button.IconButton;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.base.info.ErrorConfig;
import org.activityinfo.ui.client.base.info.SuccessConfig;

public class NotificationsMockup implements IsWidget {

    private CssLayoutContainer container = new CssLayoutContainer();

    public NotificationsMockup() {
        IconButton errorButton = new IconButton(Icon.BUBBLE_ADD, "Show error");
        errorButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                ErrorConfig config = new ErrorConfig("Save failed, form contains errors");
                config.setDisplay(1000*60);

                Info.display(config);
            }
        });

        IconButton successButton = new IconButton(Icon.BUBBLE_ADD, "Show success");
        successButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                SuccessConfig config = new SuccessConfig("Your new record has been saved.");
                config.setDisplay(1000*60);

                Info.display(config);
            }
        });

        container.add(errorButton);
        container.add(successButton);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
