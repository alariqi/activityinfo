package org.activityinfo.ui.client.database;


import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import java.util.logging.Logger;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;

public class ListItemCell extends AbstractCell<ListItem> {

    private static final Logger LOGGER = Logger.getLogger(ListItemCell.class.getName());

    private Menu optionsMenu;

    public ListItemCell() {
        super(CLICK);

        CheckMenuItem offlineMenuItem = new CheckMenuItem("Available offline");
        MenuItem transferMenuItem = new MenuItem("Transfer database");

        optionsMenu = new Menu();
        optionsMenu.add(offlineMenuItem);
        optionsMenu.add(transferMenuItem);
    }

    @Override
    public void render(Context context, ListItem item, SafeHtmlBuilder sb) {
        sb.append(DatabaseTemplates.TEMPLATES.listItem(item));
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, ListItem value, NativeEvent event, ValueUpdater<ListItem> valueUpdater) {
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        if (BrowserEvents.CLICK.equals(event.getType())) {
            Element element = event.getEventTarget().cast();
            Element options = withinClass(element, "page__item__options", 5);
            if(options != null) {
                LOGGER.info("Options clicked!");
                optionsMenu.show(options, new Style.AnchorAlignment(Style.Anchor.TOP, Style.Anchor.BOTTOM));
            }
        }
    }

    private Element withinClass(Element element, String className, int maxParents) {
        while(maxParents > 0) {
            if(hasClassName(element, className)) {
                return element;
            }
            element = element.getParentElement();
            maxParents --;
        }

        return null;
    }

    public static native boolean hasClassName(Element element, String className) /*-{
        return element.classList && element.classList.contains(className);
    }-*/;
}
