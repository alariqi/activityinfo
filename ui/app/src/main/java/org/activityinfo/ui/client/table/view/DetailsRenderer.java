package org.activityinfo.ui.client.table.view;

import com.google.common.base.Optional;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.type.FieldValue;
import org.activityinfo.ui.client.Icon;
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

    private final List<FormTree.Node> subForms = new ArrayList<>();
    private final List<FieldRenderer> fieldRenderers = new ArrayList<>();

    private final VTree emptyState;


    public DetailsRenderer(FormTree formTree) {

        // Find the subforms that belongs to this form
        for (FormTree.Node node : formTree.getRootFields()) {
            if (node.isSubForm() && node.isSubFormVisible()) {
                subForms.add(node);
            }
        }

        // Add renderers for each field

        for (FormTree.Node node : formTree.getRootFields()) {
            ValueRendererFactory.create(formTree, node.getField()).ifPresent(renderer -> {
                fieldRenderers.add(new FieldRenderer(node, renderer));
            });
        }

        // Define an empty state for when there is no record selected
        emptyState = new VNode(HtmlTag.DIV, PropMap.withClasses("details__empty"),
                Icon.NIS_EMPTYSTATE.tree(),
                new VNode(HtmlTag.DIV, "No record selected"),
                new VNode(HtmlTag.DIV, "Please select a record to see its history."));

    }


    public VTree render(Optional<RecordTree> selection) {
        return selection.transform(this::render).or(emptyState);
    }

    public VTree render(RecordTree selection) {

        List<VTree> children = new ArrayList<>();

//        if (!subForms.isEmpty()) {
//            html.appendHtmlConstant("<h3>");
//            html.appendEscaped(I18N.CONSTANTS.subForms());
//            html.appendHtmlConstant("</h3>");
//
//            for (FormTree.Node subForm : subForms) {
//                html.appendHtmlConstant("<a href=\"#\">");
//                html.appendEscaped(subForm.getForm().getSchema().getLabel());
//                html.appendHtmlConstant("</a>");
//            }
//        }
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

        return new VNode(HtmlTag.DIV, PropMap.withClasses("details"), children);
    }
}
