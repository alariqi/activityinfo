package org.activityinfo.dev.client;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import org.activityinfo.ui.client.BackgroundStyle;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.button.IconButton;
import org.activityinfo.ui.client.base.button.IconButtonStyle;
import org.activityinfo.ui.client.base.button.MenuButton;
import org.activityinfo.ui.client.base.button.PlainTextButton;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.base.container.StaticHtml;

public class ButtonMockup implements IsWidget {

    private final CssLayoutContainer container = new CssLayoutContainer();

    public ButtonMockup(BackgroundStyle backgroundStyle) {

        if(backgroundStyle == BackgroundStyle.DARK) {
            container.addStyleName("dark");
        }

        container.add(new StaticHtml(SafeHtmlUtils.fromSafeConstant("<h1>Buttons</h1>")));

        container.add(new StaticHtml(SafeHtmlUtils.fromSafeConstant("<h2>IconButton</h2>")));
        container.add(new IconButton(Icon.BUBBLE_ADD, IconButtonStyle.PRIMARY, "Add something"));
        container.add(new IconButton(Icon.BUBBLE_ADD, IconButtonStyle.SECONDARY, "Add something else"));

        container.add(new StaticHtml(SafeHtmlUtils.fromSafeConstant("<h2>Plain Text Button</h2>")));
        container.add(new PlainTextButton("Scroll to this record"));

        container.add(new StaticHtml(SafeHtmlUtils.fromSafeConstant("<h2>Menu Button</h2>")));
        container.add(new MenuButton("Open menu", createMenu()));

        container.add(new StaticHtml(SafeHtmlUtils.fromSafeConstant("<h2>Button group</h2>")));

        CssLayoutContainer group = new CssLayoutContainer();
        group.add(new ToggleButton("Details"));
        group.add(new ToggleButton("History"));

        container.add(group);

    }

    private Menu createMenu() {
        Menu menu = new Menu();
        menu.add(new MenuItem("First Menu Item"));
        menu.add(new MenuItem("Next menu item..."));

        return menu;
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
