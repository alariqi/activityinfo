package org.activityinfo.theme.dev.client;

import com.google.gwt.user.client.ui.IsWidget;

public enum DevPage {
    INDEX {
        @Override
        public IsWidget createWidget() {
            return new IndexPage();
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
    },
    DATABASES_PAGE {
        @Override
        public IsWidget createWidget() {
            return new DatabasesMockup();
        }
    };

    public abstract IsWidget createWidget();
}
