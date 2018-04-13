/**
 * Sencha GXT 4.0.0 - Sencha for GWT
 * Copyright (c) 2006-2015, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Open Source License
 * ================================================================================
 * This version of Sencha GXT is licensed under the terms of the Open Source GPL v3
 * license. You may use this license only if you are prepared to distribute and
 * share the source code of your application under the GPL v3 license:
 * http://www.gnu.org/licenses/gpl.html
 *
 * If you are NOT prepared to distribute and share the source code of your
 * application under the GPL v3 license, other commercial and oem licenses
 * are available for an alternate download of Sencha GXT.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package org.activityinfo.theme.client.base.listview;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.ListView.ListViewAppearance;

import java.util.ArrayList;
import java.util.List;

public class Css3ListViewAppearance<M> implements ListViewAppearance<M> {

    public Css3ListViewAppearance() {
    }


    @Override
    public void render(SafeHtmlBuilder builder) {
        builder.appendHtmlConstant("<div class='listview'></div>");
    }

    @Override
    public void renderItem(SafeHtmlBuilder sb, SafeHtml content) {
        sb.appendHtmlConstant("<div class='listview__item'>");
        sb.append(content);
        sb.appendHtmlConstant("</div>");
    }
    @Override
    public void renderEnd(SafeHtmlBuilder builder) {
    }

    @Override
    public Element findCellParent(XElement item) {
        return item;
    }

    @Override
    public Element findElement(XElement child) {
        return child.findParentElement(".listview__item", 20);
    }

    @Override
    public List<Element> findElements(XElement parent) {
        NodeList<Element> nodes = parent.select(".listview__item");
        List<Element> temp = new ArrayList<Element>();
        for (int i = 0; i < nodes.getLength(); i++) {
            temp.add(nodes.getItem(i));
        }

        return temp;
    }

    @Override
    public void onOver(XElement item, boolean over) {
    }

    @Override
    public void onSelect(XElement item, boolean select) {
        item.setClassName("listview__item--selected", select);
    }

}