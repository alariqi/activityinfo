package org.activityinfo.ui.client.database;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.ModelKeyProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simplified version of a ListView that does not handle selection, mouse over, etc, and is suitable
 * for proper HTML lists that include actual hyperlinks, etc.
 */
public class StaticListView<M> extends Widget {

    private final Cell<M> cell;
    private final ModelKeyProvider<M> keyProvider;

    private final Map<String, M> modelMap = new HashMap<>();

    public StaticListView(Cell<M> cell, ModelKeyProvider<M> keyProvider) {
        this.cell = cell;
        this.keyProvider = keyProvider;
        DivElement divElement = Document.get().createDivElement();
        divElement.addClassName("listview");
        setElement(divElement);

        sinkEvents(Event.ONCLICK);
    }

    public void updateView(List<M> models) {

        modelMap.clear();

        if(models.isEmpty()) {
            getElement().setInnerSafeHtml(DatabaseTemplates.TEMPLATES.emptyList());
            return;
        }

        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        for (int i = 0; i < models.size(); i++) {
            M model = models.get(i);
            String modelKey = keyProvider.getKey(model);

            sb.appendHtmlConstant("<div class=\"listview__item\" data-key=\"" + modelKey + "\">");
            cell.render(new Cell.Context(i, 0, modelKey), model, sb);
            sb.appendHtmlConstant("</div>");

            modelMap.put(modelKey, model);
        }

        getElement().setInnerSafeHtml(sb.toSafeHtml());
    }

    public void clear() {
        getElement().setInnerSafeHtml(SafeHtmlUtils.EMPTY_SAFE_HTML);
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if(cell.getConsumedEvents().contains(event.getType())) {
            XElement element = event.getEventTarget().cast();
            Element parent = findParent(element);

            if(parent != null) {
                String modelKey = parent.getAttribute("data-key");
                M model = modelMap.get(modelKey);
                cell.onBrowserEvent(new Cell.Context(0, 0, modelKey, 0), parent,
                        model, event, null);
            }
        }
    }

    private Element findParent(Element element) {
        while(element != null) {
            String dataKey = element.getAttribute("data-key");
            if(!Strings.isNullOrEmpty(dataKey)) {
                return element;
            }
            element = element.getParentElement();
        }
        return null;
    }
}
