package org.activityinfo.ui.vdom.shared.tree;

import org.activityinfo.ui.vdom.shared.html.Children;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class VNode extends VTree {

    /**
     * Singleton instance for an empty child list.
     *
     */
    public static final VTree[] NO_CHILDREN = new VTree[0];

    public final Tag tag;
    public final PropMap properties;
    public final VTree[] children;

    @Nullable
    public final String key;

    private boolean hasComponents;

    public VNode(Tag tag, VTree... children) {
        this(tag, null, children, null);
    }

    public VNode(Tag tag, String text) {
        this(tag, null, new VText(text));
    }

    public VNode(Tag tag, Stream<VTree> children) {
        this(tag, null, children.toArray(VTree[]::new));
    }

    public VNode(Tag tag, PropMap propMap, Stream<VTree> children) {
        this(tag, propMap, children.toArray(VTree[]::new));
    }

    public VNode(Tag tag, PropMap propMap) {
        this(tag, propMap, null, null);
    }

    public VNode(Tag tag, PropMap properties, VTree child) {
        this(tag, properties, new VTree[] { child });
    }

    public VNode(Tag tag, PropMap properties, VTree... children) {
        this(tag, properties, children, null);
    }

    public VNode(Tag tag, PropMap properties, List<VTree> children) {
        this(tag, properties, Children.toArray(children), null);
    }

    public VNode(@Nonnull Tag tag,
                 @Nullable PropMap properties,
                 @Nullable VTree[] children,
                 @Nullable String key) {

        this.tag = tag;
        this.properties = properties == null ? PropMap.EMPTY : properties;
        this.children = children == null ? NO_CHILDREN : children;
        this.key = key;
        this.hasComponents = false;

        assert PropMap.EMPTY.isEmpty();

        for (VTree child : this.children) {
            if(child.hasComponents()) {
                hasComponents = true;
                break;
            }
        }
    }


    @Override
    public boolean hasComponents() {
        return hasComponents;
    }

    @Override
    public VTree[] children() {
        return children;
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public void accept(VTreeVisitor visitor) {
        visitor.visitNode(this);
    }

    @Override
    public String debugId() {
        String tag = this.tag.name().toLowerCase();
        Object className = properties.get("className");
        if(className instanceof String) {
            tag += "." + className;
        }
        return tag;
    }

    @Override
    public String toString() {
        String tag = this.tag.name().toLowerCase();
        if(children.length == 1 && children[0] instanceof VText) {
            return "<" + tag + ">" + children[0].text() + "</" + tag + "/>";
        } else if(children.length > 0) {
            return "<" + tag + "> ... </" + tag + ">";
        } else {
            return "<" + tag + "/>";
        }
    }
}
