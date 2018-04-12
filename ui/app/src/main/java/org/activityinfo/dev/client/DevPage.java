package org.activityinfo.dev.client;

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
            return new TableMockup();
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
    },
    DATABASE_PAGE {
        @Override
        public IsWidget createWidget() {
            return new DatabaseMockup();
        }
    };

    public abstract IsWidget createWidget();
}
