package org.activityinfo.ui.client.table.view;

import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayList;
import java.util.List;

public class DetailsRenderer {

    private static class FieldRenderer {
        private final FormTree.Node field;
        private final ValueRenderer renderer;

        public FieldRenderer(FormTree.Node fieldNode, ValueRenderer renderer) {
            this.field = fieldNode;
            this.renderer = renderer;
        }
    }

    private final List<FieldRenderer> fieldRenderers = new ArrayList<>();

    public DetailsRenderer(FormTree formTree) {

        for (FormTree.Node node : formTree.getRootFields()) {
            ValueRendererFactory.create(formTree, node.getField()).ifPresent(renderer -> {
                fieldRenderers.add(new FieldRenderer(node, renderer));
            });
        }
    }

    public VTree render(RecordTree selection, boolean loading) {

        List<VTree> children = new ArrayList<>();

        FormInstance record = selection.getRoot();

        for (FieldRenderer fieldRenderer : fieldRenderers) {
            FieldValue fieldValue = record.get(fieldRenderer.field.getFieldId());
            if(fieldValue != null) {
                children.add(new VNode(HtmlTag.DIV, PropMap.withClasses("details__field"),
                        new VNode(HtmlTag.H4, fieldRenderer.field.getField().getLabel()),
                        new VNode(HtmlTag.DIV, PropMap.withClasses("details__value"),
                                fieldRenderer.renderer.render(selection, fieldValue))));
            }
        }

        PropMap props = PropMap.withClasses("details__fields");
        if(loading) {
            props.addClassName("details__fields--loading");
        }
        return new VNode(HtmlTag.DIV, props, children);
    }
}
