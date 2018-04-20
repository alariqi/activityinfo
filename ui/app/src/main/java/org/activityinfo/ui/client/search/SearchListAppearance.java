package org.activityinfo.ui.client.search;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchListAppearance implements ListView.ListViewAppearance<SearchResult> {


    @Override
    public void render(SafeHtmlBuilder builder) {
        builder.append(SearchTemplates.TEMPLATES.searchList());
    }

    @Override
    public void renderItem(SafeHtmlBuilder builder, SafeHtml content) {
        builder.append(content);
    }

    @Override
    public void renderEnd(SafeHtmlBuilder builder) {
    }

    public Element findCellParent(XElement item) {
        return item;
    }

    public Element findElement(XElement child) {
        return child.findParentElement(".search__item", 20);
    }

    public List<Element> findElements(XElement parent) {
        NodeList<Element> nodes = parent.select(".search__item");
        List<Element> list = new ArrayList<>();
        for(int i = 0; i < nodes.getLength(); ++i) {
            list.add(nodes.getItem(i));
        }
        return list;
    }


    @Override
    public void onOver(XElement item, boolean over) {
    }

    @Override
    public void onSelect(XElement item, boolean select) {
    }
}
