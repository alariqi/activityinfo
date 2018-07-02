package org.activityinfo.ui.vdom.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import org.activityinfo.ui.vdom.shared.html.SvgTag;
import org.activityinfo.ui.vdom.shared.tree.*;

/**
 * Builds a DOM element tree from virtual dom tree
 */
public class DomBuilder {

    public static final String SVG_NS = "http://www.w3.org/2000/svg";

    public DomBuilder() {
    }

    public Node render(VTree vTree, boolean isSvg) {

        if(vTree instanceof VComponent) {
            return renderComponent((VComponent) vTree, isSvg);

        } else if(vTree instanceof VText) {
            return renderText((VText) vTree);

        } else if(vTree instanceof VNode) {
            return renderTree((VNode) vTree, isSvg);

        } else {
            throw new IllegalArgumentException("Unknown virtual node " + vTree);
        }
    }

    private Element renderTree(VNode vnode, boolean isSvg) {
        Element dom;

        if(vnode.tag == SvgTag.SVG) {
            isSvg = true;
        }

        if(isSvg) {
            dom = createElementNS(vnode.tag.name().toLowerCase(), SVG_NS);
        } else {
            dom = Document.get().createElement(vnode.tag.name());
        }

        PropMap props = vnode.properties;
        if(props != null) {
            try {
                Properties.apply(dom, props, isSvg);
            } catch(Exception e) {
                GWT.log("Exception thrown while setting properties of " + vnode + ": " + e.getMessage(), e);
            }
        }

        return appendChildren(dom, vnode, isSvg);
    }

    private static native Element createElementNS(String tagName, String namespace) /*-{
        return $wnd.document.createElementNS(namespace, tagName);
    }-*/;


    private Element appendChildren(Element Element, VNode vnode, boolean isSvg) {
        VTree[] children = vnode.children;
        for (int i = 0; i < children.length; ++i) {
            Element.appendChild(render(children[i], isSvg));
        }

        return Element;
    }

    private Node renderComponent(VComponent component, boolean isSvg) {
        VTree virtualNode = component.ensureRendered();
        Node domNode = render(virtualNode, isSvg);

        return domNode;
    }

    private Node renderText(VText vTree) {
        return Document.get().createTextNode(vTree.getText());
    }

}
