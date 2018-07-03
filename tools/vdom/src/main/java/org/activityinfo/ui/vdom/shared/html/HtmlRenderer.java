package org.activityinfo.ui.vdom.shared.html;

import com.google.common.collect.Sets;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.Set;

public class HtmlRenderer implements VTreeVisitor {

    public static final String QUOTE = "\"";

    private static final Set<String> SINGLETON_TAGS = Sets.newHashSet(
            HtmlTag.META,
            HtmlTag.LINK,
            HtmlTag.HR,
            HtmlTag.BR,
            HtmlTag.WBR,
            HtmlTag.IMG,
            HtmlTag.EMBED,
            HtmlTag.PARAM,
            HtmlTag.SOURCE,
            HtmlTag.COL,
            HtmlTag.INPUT);

    private StringBuilder html;
    private boolean prettyPrint;
    private int currentIndentLevel;

    public static String render(VTree tree) {
        HtmlRenderer renderer = new HtmlRenderer();
        tree.accept(renderer);
        return renderer.getHtml();
    }

    public HtmlRenderer() {
        html = new StringBuilder();
    }

    public void writeDocTypeDeclaration() {
        html.append("<!DOCTYPE html>\n");
    }

    public void visitNode(VNode node) {
        html.append("<").append(node.tag);

        appendProperties(node);
        html.append(">");

        if(SINGLETON_TAGS.contains(node.tag)) {
            // Tags like <input> and <br> are not closed...
            // but they also don't have children
            assert node.children.length == 0 : node.tag + " is a singleton";

        } else {

            appendChildren(node.children);
            html.append("</").append(node.tag).append(">");
        }
    }

    private void appendProperties(VNode node) {
        if(node.properties != null) {
            for(String propName : node.properties.keys()) {
                Object propValue = node.properties.get(propName);
                switch (propName) {
                    case "className":
                        appendProperty("class", (String) propValue);
                        break;

                    case "style":
                        appendStyleProperty((PropMap) propValue);
                        break;

                    default:
                        appendProperty(propName, (String) propValue);
                        break;
                }
            }
        }
    }

    private void appendChildren(VTree[] children) {
        if(children != null) {
            for(int i=0;i!=children.length;++i) {
                children[i].accept(this);
            }
        }
    }

    private void appendProperty(String attributeName, String value) {
        //assert value != null : attributeName;
        if(value != null) {
            html.append(" ")
                    .append(attributeName)
                    .append("=")
                    .append(QUOTE)
                    .append(SafeHtmlUtils.htmlEscape(value))
                    .append(QUOTE);
        }
    }

    private void appendStyleProperty(PropMap styleMap) {
        // NOTE: Styles are assumed to be NOT user provided
        // and so were are not escaping/checking. is that right?
        if(!styleMap.keys().isEmpty()) {
            html.append(" style=\"");
            for(String name : styleMap.keys()) {
                String value = (String) styleMap.get(name);
                html.append(name).append(":").append(value).append(";");
            }
            html.append(QUOTE);
        }
    }
    
    @Override
    public void visitText(VText text) {
        html.append(SafeHtmlUtils.htmlEscape(text.getText()));
    }

    @Override
    public void visitComponent(VComponent vComponent) {
        vComponent.forceRender().accept(this);
    }

    @Override
    public void visitWidget(VWidget widget) {
        // noop??
    }

    public String getHtml() {
        return html.toString();
    }
}
