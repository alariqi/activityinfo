package org.activityinfo.ui.vdom.client.render;


import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Text;
import org.activityinfo.ui.vdom.shared.VDomLogger;
import org.activityinfo.ui.vdom.shared.diff.PatchOp;
import org.activityinfo.ui.vdom.shared.diff.VPatchSet;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VComponent;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.gwt.dom.client.Node.TEXT_NODE;

public class DomPatcher implements PatchOpExecutor {

    private static final Logger LOGGER = Logger.getLogger(DomPatcher.class.getName());

    private DomBuilder domBuilder;
    private RenderContext context;

    public DomPatcher(DomBuilder domBuilder, RenderContext context) {
        this.domBuilder = domBuilder;
        this.context = context;
    }

    /*
     * Renders a full dom tree for the given virtual {@code vtree}, using
     * {@code rootElement} as the root dom element. Any existing children
     * of {@code rootElement} will be removed.
     */
    public void init(Element rootElement, VTree vtree) {
        if(vtree instanceof VNode) {
            initRootNode(rootElement, (VNode) vtree);
        } else if(vtree instanceof VComponent) {
            initRootComponent(rootElement, (VComponent)vtree);
        } else {
            throw new IllegalStateException("Root vTree must be an element");
        }
    }

    private void initRootNode(Element rootElement, VNode vNode) {
        if(!rootElement.getTagName().equalsIgnoreCase(vNode.tag.name())) {
            throw new UnsupportedOperationException("Cannot change the tag name of the root element");
        }
        Properties.applyProperties(rootElement, vNode.properties, null);

        rootElement.removeAllChildren();

        appendChildren(rootElement, vNode);

        if(vNode.hasComponents()) {
            fireMountRecursively(context, vNode, rootElement);
        }
    }

    private void initRootComponent(Element rootElement, VComponent component) {
        init(rootElement, component.ensureRendered());

        component.fireMounted(context, rootElement);
    }

    private void appendChildren(Element parentElement, VNode vnode) {
        VTree[] children = vnode.children;
        for (VTree child : children) {
            parentElement.appendChild(domBuilder.render(child));
        }
    }


    public Node patch(Element rootNode, VPatchSet patches) {
        return patchRecursive(rootNode, patches);
    }

    public Node patchRecursive(Node rootNode, VPatchSet patches) {
        int[] indices = patches.patchedIndexArray();
        if(indices.length > 0) {

            Map<Integer, Node> index = DomIndexBuilder.domIndex(rootNode, patches.original, indices);

            for (int i = 0; i < indices.length; ++i) {
                int nodeIndex = indices[i];
                rootNode = applyPatch(rootNode, index.get(nodeIndex), patches.get(nodeIndex));
            }
        }
        return rootNode;
    }

    private Node applyPatch(Node rootNode, Node domNode, List<PatchOp> patchList) {
        if(domNode != null) {
            Node newNode;
            for (int i = 0; i != patchList.size(); ++i) {
                PatchOp op = patchList.get(i);

                VDomLogger.applyPatch(op);

                newNode = op.apply(this, domNode);
                if (domNode == rootNode) {
                    rootNode = newNode;
                }
            }
        }
        return rootNode;
    }

    @Override
    public Node updateProperties(Node domNode, PropMap propPatch, PropMap previous) {
        Properties.applyProperties(domNode.cast(), propPatch, previous);
        return domNode;
    }


    @Override
    public Node removeNode(VTree virtualNode, Node Node) {

        fireUnmountRecursively(virtualNode);

        Node parentNode = Node.getParentNode();
        if (parentNode != null) {
            parentNode.removeChild(Node);
        }
        return null;
    }

    @Override
    public Node insertNode(Node parentNode, VTree newNode) {
        Node domNode = domBuilder.render(newNode);
        parentNode.appendChild(domNode);

        if(newNode.hasComponents()) {
            fireMountRecursively(context, newNode, domNode);
        }

        return parentNode;
    }

    @Override
    public Node patchText(Node domNode, String newText) {
        assert domNode.getNodeType() == TEXT_NODE;

        Text textNode = domNode.cast();
        textNode.setData(newText);

        return domNode;
    }

    @Override
    public Node replaceNode(VTree previousNode, VTree vNode, Node Node) {

        fireUnmountRecursively(previousNode);

        Node parentNode = Node.getParentNode();
        Node newNode = domBuilder.render(vNode);
        if (parentNode != null) {
            parentNode.replaceChild(newNode, Node);
        }
        return newNode;
    }


    private void reorderChildren(Node Node, int[] bIndex) {
        throw new UnsupportedOperationException();
//        int[] children = new int[];
//        var childNodes = Node.childNodes
//        var len = childNodes.length
//        var i
//        var reverseIndex = bIndex.reverse
//        for (i = 0; i < len; i++) {
//            children.push(Node.childNodes[i])
//        }
//        var insertOffset = 0
//        var move
//        var node
//        var insertNode
//        for (i = 0; i < len; i++) {
//            move = bIndex[i]
//            if (move !== undefined && move !== i) {
//                // the element currently at this index will be moved later so increase the insert offset
//                if (reverseIndex[i] > i) {
//                    insertOffset++
//                }
//                node = children[move]
//                insertNode = childNodes[i + insertOffset]
//                if (node !== insertNode) {
//                    Node.insertBefore(node, insertNode)
//                }
//                // the moved element came from the front of the array so reduce the insert offset
//                if (move < i) {
//                    insertOffset--
//                }
//            }
//            // element at this index is scheduled to be removed so increase insert offset
//            if (i in bIndex.removes) {
//                insertOffset++
//            }
//        }
    }

    @Override
    public Node patchComponent(Node rootNode, VComponent previous, VComponent replacement, VPatchSet patch) {

        Node newNode = patch(rootNode.cast(), patch);

        if( previous != replacement) {
            assert previous.isMounted();

            // Avoid firing recursively here as any child components will be
            // unmounted when _patch_ is applied
            try {
                previous.fireWillUnmount();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception thrown during previous.fireWillUnmount()", e);
            }

        } else {
            // same instance, is it being remounted to the
            // same dom node?
            if(replacement.isMounted() &&
               replacement.getDomNode() != newNode) {
                previous.fireWillUnmount();
            }
        }

        rootNode = replaceRoot(rootNode, newNode);

        fireMountRecursively(context, replacement, rootNode);

        return rootNode;
    }

    private void fireMountRecursively(RenderContext context, VTree vNode, Node node) {
        if(vNode instanceof VComponent) {
            VComponent component = (VComponent) vNode;
            fireMountRecursively(context, component.vNode, node);

            if(!component.isMounted()) {
                component.fireMounted(context, node.cast());
            }

        } else if(vNode instanceof VNode && vNode.hasComponents()) {
            VNode parent = (VNode) vNode;
            for(int i=0;i!= parent.children.length;++i) {
                fireMountRecursively(context, parent.children[i], node.getChild(i));
            }
        }
    }

    private void fireUnmountRecursively(VTree vNode) {
        if(vNode instanceof VComponent) {
            VComponent component = (VComponent) vNode;
            fireUnmountRecursively(component.vNode);
            component.fireWillUnmount();

        } else if(vNode instanceof VNode && vNode.hasComponents()) {
            VNode parent = (VNode) vNode;
            for(int i=0;i!= parent.children.length;++i) {
                fireUnmountRecursively(parent.children[i]);
            }
        }
    }

    public Node replaceRoot(Node oldRoot, Node newRoot) {
        if ( (oldRoot != null) && (newRoot != null) &&
             (oldRoot != newRoot) && (oldRoot.getParentNode() != null)) {

            oldRoot.getParentNode().replaceChild(newRoot, oldRoot);
        }
        return newRoot;
    }
}
