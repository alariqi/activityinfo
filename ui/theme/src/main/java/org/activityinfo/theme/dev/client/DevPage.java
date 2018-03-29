package org.activityinfo.theme.dev.client;

import com.google.gwt.user.client.ui.IsWidget;

public enum DevPage {
    BUTTON {
        @Override
        public IsWidget createWidget() {
            return new ButtonsPage();
        }
    },
    TABLEGRID {
        @Override
        public IsWidget createWidget() {
            return new GridPanel();
        }
    },
    SEARCH {
        @Override
        public IsWidget createWidget() {
            return new SearchPanel();
        }
    };

    public abstract IsWidget createWidget();
}
