package org.activityinfo.ui.client.base;

import com.sencha.gxt.core.client.resources.ThemeStyles;

public class ThemeAppearance implements ThemeStyles.ThemeAppearance {

    private static final class MyThemeStyles implements ThemeStyles.Styles {

        @Override
        public String border() {
            return "theme__border";
        }

        @Override
        public String borderLeft() {
            return "theme__border-left";

        }

        @Override
        public String borderRight() {
            return "theme__border-right";
        }

        @Override
        public String borderTop() {
            return "theme__border-top";
        }

        @Override
        public String borderBottom() {
            return "theme__border-bottom";
        }

        @Override
        public String disabled() {
            return "theme__border--disabled";
        }

        @Override
        public boolean ensureInjected() {
            return false;
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        public String getName() {
            return "styles";
        }
    }

    private static final MyThemeStyles STYLES = new MyThemeStyles();

    @Override
    public ThemeStyles.Styles style() {
        return STYLES;
    }

    @Override
    public String borderColor() {
        return "fuschia";
    }

    @Override
    public String borderColorLight() {
        return "fuschia";
    }

    @Override
    public String backgroundColorLight() {
        return "fuschia";
    }
}
