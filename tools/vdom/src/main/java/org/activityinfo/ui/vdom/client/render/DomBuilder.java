package org.activityinfo.ui.vdom.client.render;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import org.activityinfo.ui.vdom.shared.tree.*;

/**
 * Builds a DOM element tree from virtual dom tree
 */
public class DomBuilder {

    private RenderContext context;


    public DomBuilder(RenderContext context) {
        this.context = context;
    }

    public Node render(VTree vTree) {

        if(vTree instanceof VComponent) {
            return renderComponent((VComponent) vTree);

        } else if(vTree instanceof VText) {
            return renderText((VText) vTree);

        } else if(vTree instanceof VNode) {
            return renderTree((VNode) vTree);

        } else {
            throw new IllegalArgumentException("Unknown virtual node " + vTree);
        }
    }

    private Element renderTree(VNode vnode) {
        Element Element;
        if(vnode.namespace == null) {
            Element = Document.get().createElement(vnode.tag.name());
        } else {
            Element = createElementNS(vnode.tag.name().toLowerCase(), vnode.namespace);
        }

        PropMap props = vnode.properties;
        if(props != null) {
            try {
                Properties.applyProperties(Element, props, null);
            } catch(Exception e) {
                GWT.log("Exception thrown while setting properties of " + vnode + ": " + e.getMessage(), e);
            }
        }

        return appendChildren(Element, vnode);
    }

    private static native Element createElementNS(String tagName, String namespace) /*-{
        return $wnd.document.createElementNS(namespace, tagName);
    }-*/;


    private Element appendChildren(Element Element, VNode vnode) {
        VTree[] children = vnode.children;
        for (int i = 0; i < children.length; ++i) {
            Element.appendChild(render(children[i]));
        }

        return Element;
    }

    private Node renderComponent(VComponent component) {
        VTree virtualNode = component.ensureRendered();
        Node domNode = render(virtualNode);

        return domNode;
    }

    private Node renderText(VText vTree) {
        return Document.get().createTextNode(vTree.getText());
    }

}
