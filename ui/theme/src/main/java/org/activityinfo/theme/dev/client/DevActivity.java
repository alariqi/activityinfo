package org.activityinfo.theme.dev.client;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DevActivity implements Activity {

    private static final Logger LOGGER = Logger.getLogger(DevActivity.class.getName());

    private final DevPage page;


    public DevActivity(DevPlace place) {
        this.page = place.getPage();
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        try {
            panel.setWidget(page.createWidget());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to create component panel", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String mayStop() {
        return null;
    }

    @Override
    public void onCancel() {
    }

    @Override
    public void onStop() {
    }

}
