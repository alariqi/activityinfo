package org.activityinfo.ui.vdom.shared.tree;

import com.google.gwt.dom.client.Element;
import org.activityinfo.ui.vdom.client.RenderContext;
import org.activityinfo.ui.vdom.shared.VDomLogger;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class VComponent extends VTree {

    private static final Logger LOGGER = Logger.getLogger(VComponent.class.getName());

    protected final int debugIndex = VDomLogger.nextDebugId();

    private RenderContext context = null;

    private Element domNode;

    protected VComponent() {
        VDomLogger.event(this, "constructed");
    }

    public final void fireMounted(RenderContext context, Element domNode) {

        LOGGER.info("fireMounted: " + debugId());

        assert this.domNode != domNode : this + " mounted twice to same dom node";
        assert this.context == null : this + " may only be mounted once";
        this.context = context;
        this.domNode = domNode;

        VDomLogger.event(this, "didMount");

        componentDidMount();
    }

    public final void fireWillUnmount() {
        assert this.context != null : this.getDebugId() +  " must be mounted first";

        VDomLogger.event(this, "willUnmount");

        try {
            componentWillUnmount();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception thrown during componentWillUnmount()", e);
        }

        this.context = null;
        this.domNode = null;
    }

    /**
     * Cached result of the last call to render
     */
    public VTree vNode = null;


    /**
     * True if this thunk needs to be re-rendered on the next pass
     */
    private boolean dirty = false;


    /**
     * Marks this node as dirty
     */
    public final void refresh() {
        //assert context != null : "No render context is set for " + getDebugId();
        dirty = true;
        if(isMounted()) {
            context.fireUpdate(this);
        }
    }

    public final boolean isDirty() {
        return dirty;
    }


    public final boolean isRendered() {
        return vNode != null;
    }


    public final boolean isMounted() { return context != null; }

    @Override
    public boolean hasComponents() {
        return true;
    }

    /**
     * Renders this Thunk to a concrete {@code VTree}. During diffing,
     * a {@code previous} parameter provides the previously rendered thunk
     * from the last {@code VTree}. This thunk can decide whether to reuse
     * the {@code VTree} from {@code previous} or re-render.
     *
     * @return a {@code VTree} node of type {@code VNode}, {@code VText} or {@code VWidget}
     */
    protected abstract VTree render();

    /**
     * Called immediately after the component is newly added to the real
     * DOM tree.
     */
    protected void componentDidMount() {

    }

    protected void componentWillUnmount() {

    }

    public final Element getDomNode() {
        assert domNode != null : "component has not been mounted";
        return domNode;
    }

    protected final RenderContext getContext() {
        assert context != null : "component has not been mounted!";
        return context;
    }

    @Override
    public void accept(VTreeVisitor visitor) {
        visitor.visitComponent(this);
    }

    @Override
    public String debugId() {
        if(vNode != null) {
            return "Component:" + vNode.debugId();
        } else {
            return "Component: <unrendered>";
        }
    }

    public VTree ensureRendered() {
        if(vNode == null) {
            vNode = invokeRender();
            assert vNode != null;
        }
        return vNode;
    }

    public VTree forceRender() {
        vNode = invokeRender();
        dirty = false;
        return vNode;
    }

    private VTree invokeRender() {
        try {
            return render();
        } catch(Throwable caught) {
            LOGGER.log(Level.SEVERE, "Exception thrown while rendering " + getDebugId(), caught);
            return new VNode(HtmlTag.NOSCRIPT);
        }
    }

    public String getPropertiesForDebugging() {
        return "";
    }

    public String getDebugId() {
        return getClass().getSimpleName() + "#" + debugIndex + "[ " + getPropertiesForDebugging() + " ]";
    }

    @Override
    public String toString() {
        return getDebugId();
    }

    /**
     * Attempt to update this component in place, using the previous component.
     */
    public boolean executeUpdate(VComponent prevComponent) {
        return false;
    }
}
