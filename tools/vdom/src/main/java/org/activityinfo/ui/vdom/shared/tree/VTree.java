package org.activityinfo.ui.vdom.shared.tree;

public abstract class VTree {

    public boolean hasComponents() {
        return false;
    }

    public String key() { throw new UnsupportedOperationException(); }

    public String text() { throw new UnsupportedOperationException(); }

    public VTree[] children() {
        return VNode.NO_CHILDREN;
    }

    public VTree childAt(int index) {
        return children()[index];
    }

    public abstract void accept(VTreeVisitor visitor);


    /**
     * Forces a {@link VComponent} to a concrete value if this VTree is a {@link VComponent}, or
     * this VTree itself is a concrete value.
     */
    public VTree force() {
        return this;
    }

    public abstract String debugId();
}
