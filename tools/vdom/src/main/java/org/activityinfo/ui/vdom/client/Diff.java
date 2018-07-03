package org.activityinfo.ui.vdom.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Text;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.VComponent;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayDeque;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Updates a real DOM node to match changes between two virtual DOM trees.
 *
 * <p>The implementation below was ported quite literally from the javascript vtree library,
 * was in turn based on the approach taken by Facebook's React library. It has been since updated
 * to use a one-pass algorithm similar to that used by the Preact library.
 *
 *
 * @author Matt-Esch
 * @author Alex Bertram
 *
 * @see <a href="https://github.com/Matt-Esch/virtual-dom">The virtual-dom project</a>
 * @see <a href="https://github.com/Matt-Esch/vtree/blob/5815772fe5b4af971a34bb18610fd0c55fe8d107/diff.js">Original source</a>
 * @see <a href="http://facebook.github.io/react/docs/reconciliation.html">Facebook's description of the algorithm</a>
 */
public class Diff {

    private static final Logger LOGGER = Logger.getLogger(Diff.class.getName());

    private final RenderContext context;

    private ArrayDeque<VTree> unmountQueue = new ArrayDeque<>();

    private DomBuilder domBuilder  = new DomBuilder();

    private boolean isSvgMode = false;

    public Diff(RenderContext context) {
        this.context = context;
    }

    public void init(Element root, VTree vTree) {
        walk(new VNode(HtmlTag.DIV), vTree, root, false);
    }

    public void patch(Element root, VTree previous, VTree updated) {
        LOGGER.info("======= PATCH STARTING =======");
        walk(previous, updated, root, false);
        flushUnmountQueue();
        LOGGER.info("======= PATCH ENDED =======");

    }

    private void walk(VTree previous, VTree updated, Node dom, boolean isSvgMode) {

        assert previous != null;

        if (updated == previous) {

            // We assume that VNode and VText are immutable,
            // so if a == b, we only need to address components
            // which have marked themselves as dirty with refresh()

            if (previous instanceof VComponent) {
                diffComponent((VComponent) previous, dom.cast());

            } else if (previous.hasComponents()) {
                diffChildren(previous, updated, dom.cast(), isSvgMode);
            }

        } else if (updated instanceof VComponent) {
            if (previous instanceof VComponent) {
                diffComponents((VComponent)previous, (VComponent)updated, dom.cast());
            } else {
                // Replacing a non-component with a new component
                replace(previous, updated, dom.cast(), isSvgMode);
            }

        } else if (updated instanceof VNode) {
            if (previous instanceof VNode) {
                diffVNodes((VNode)previous, (VNode)updated, dom.cast());

            } else if(previous instanceof VComponent) {
                walk(((VComponent) previous).vNode, updated, dom, isSvgMode);
                unmountQueue.add(previous);
            } else {
                replace(previous, updated, dom, isSvgMode);
            }

        } else if (updated instanceof VText) {
            if (previous instanceof VText) {
                if(!previous.text().equals(updated.text())) {
                    Text textNode = dom.cast();
                    textNode.setData(((VText) updated).getText());
                }
            } else {
                replace(previous, updated, dom, isSvgMode);
            }
        }
    }

    private void replace(VTree old, VTree updated, Node oldDomNode, boolean isSvgMode) {

        Node newDomNode = domBuilder.render(updated, isSvgMode);

        Element parent = oldDomNode.getParentElement();
        parent.replaceChild(newDomNode, oldDomNode);

        fireMountRecursively(updated, newDomNode);

        if(old.hasComponents()) {
            unmountQueue.add(old);
        }
    }

    private void diffVNodes(VNode previous, VNode updated, Element dom) {
        if (previous.tag.equals(updated.tag) &&
            Objects.equals(previous.key, updated.key)) {

            Properties.diffProperties(dom, updated.properties, previous.properties, isSvgMode);
            diffChildren(previous, updated, dom, isSvgMode);

        } else {
            replace(previous, updated, dom, isSvgMode);
        }
    }


    public void diffChildren(VTree previous, VTree updated, Element parent, boolean isSvgMode) {
        VTree[] prevChildren = previous.children();
        VTree[] updatedChildren = updated.children();

        int prevLen = prevChildren.length;
        int updatedLen = updatedChildren.length;
        int commonLength = Math.min(prevLen, updatedLen);

        // First diff children common to the previous tree
        // and the new tree.
        // For example, if the previous tree had 3 children, and the new tree
        // has 5 children, try to patch only the first three.

        for (int i = 0; i < commonLength; i++) {
            VTree prevNode = prevChildren[i];
            VTree updatedNode = updatedChildren[i];

            walk(prevNode, updatedNode, parent.getChild(i), isSvgMode);
        }

        // If we had more nodes in the previous tree, remove them
        for (int i = commonLength; i < prevLen; i++) {

            parent.removeChild(parent.getLastChild());

            VTree prevChild = prevChildren[i];
            if(prevChild.hasComponents()) {
                unmountQueue.add(prevChild);
            }
        }

        // Add any new children
        for (int i = commonLength; i < updatedLen; i++) {
            VTree updatedNode = updatedChildren[i];
            Node newDomNode = domBuilder.render(updatedNode, isSvgMode);
            parent.appendChild(newDomNode);

            fireMountRecursively(updatedNode, newDomNode);
        }
    }

    /**
     * Updates a single component in place
     */
    private void diffComponent(VComponent component, Element dom) {
        VTree previous = component.vNode;
        if(previous == null) {
            throw new IllegalStateException();
        }

        if(component.isDirty()) {
            VTree updated = component.forceRender();
            walk(previous, updated, dom, isSvgMode);

        } else {

            // if there have been no changes to the component, we
            // still need to look for dirty components within the
            // component's tree
            if(previous.hasComponents()) {
                walk(previous, previous, dom, isSvgMode);
            }
        }
    }


    public void diffComponents(VComponent prevComponent, VComponent updatedComponent, Element dom) {

        assert prevComponent != null;
        assert updatedComponent != null;
        assert prevComponent.isRendered();

        VTree prevTree = prevComponent.vNode;
        VTree updatedTree = updatedComponent.ensureRendered();

        assert prevTree != null;
        assert updatedTree != null;

        walk(prevTree, updatedTree, dom, isSvgMode);

        fireMountRecursively(updatedComponent, dom);

        unmountQueue.add(prevComponent);
    }



    private void fireMountRecursively(VTree vNode, Node node) {
        if(vNode instanceof VComponent) {
            VComponent component = (VComponent) vNode;
            fireMountRecursively(component.vNode, node);

            if(!component.isMounted()) {
                component.fireMounted(context, node.cast());
            }

        } else if(vNode instanceof VNode && vNode.hasComponents()) {
            VNode parent = (VNode) vNode;
            for(int i=0;i!= parent.children.length;++i) {
                fireMountRecursively(parent.children[i], node.getChild(i));
            }
        }
    }

    private void flushUnmountQueue() {

        while(!unmountQueue.isEmpty()) {
            fireUnmountRecursively(unmountQueue.pop());
        }
    }

    private void fireUnmountRecursively(VTree vNode) {
        if(vNode instanceof VComponent) {
            VComponent component = (VComponent) vNode;
            if(component.isMounted()) {
                fireUnmountRecursively(component.vNode);
                component.fireWillUnmount();
            }

        } else if(vNode instanceof VNode && vNode.hasComponents()) {
            VNode parent = (VNode) vNode;
            for(int i=0;i!= parent.children.length;++i) {
                fireUnmountRecursively(parent.children[i]);
            }
        }
    }

}
