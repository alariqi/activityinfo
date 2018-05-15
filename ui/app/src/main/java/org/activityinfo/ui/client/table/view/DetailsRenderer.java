package org.activityinfo.ui.client.table.view;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.XElement;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.FormInstance;
import org.activityinfo.model.formTree.FormTree;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.FieldValue;

import java.util.ArrayList;
import java.util.List;

public class DetailsRenderer {

    private static class FieldRenderer {
        private ResourceId fieldId;
        private ValueRenderer renderer;

        public FieldRenderer(ResourceId fieldId, ValueRenderer renderer) {
            this.fieldId = fieldId;
            this.renderer = renderer;
        }
    }

    private final FormTree formTree;
    private final List<FormTree.Node> subForms = new ArrayList<>();
    private final List<FieldRenderer> fieldRenderers = new ArrayList<>();


    public DetailsRenderer(FormTree formTree) {
        this.formTree = formTree;

        // Find the subforms that belongs to this form
        for (FormTree.Node node : formTree.getRootFields()) {
            if(node.isSubForm() && node.isSubFormVisible()) {
                subForms.add(node);
            }
        }
    }

    public SafeHtml renderSkeleton() {
        SafeHtmlBuilder html = new SafeHtmlBuilder();
        html.appendHtmlConstant("<h2>");
        html.appendEscaped(I18N.CONSTANTS.recordDetails());
        html.appendHtmlConstant("</h2>");
//
//        getParentForm().ifPresent(parent -> {
//            html.appendHtmlConstant("<h4>");
//            html.appendEscaped(I18N.CONSTANTS.goBackTo());
//            html.appendHtmlConstant("</h4>");
//            html.appendHtmlConstant("<a href=\"#\">");
//            html.appendEscaped(parent.getLabel());
//            html.appendHtmlConstant("</a>");
//        });

        if (!subForms.isEmpty()) {
            html.appendHtmlConstant("<h3>");
            html.appendEscaped(I18N.CONSTANTS.subForms());
            html.appendHtmlConstant("</h3>");

            for (FormTree.Node subForm : subForms) {
                html.appendHtmlConstant("<a href=\"#\">");
                html.appendEscaped(subForm.getForm().getSchema().getLabel());
                html.appendHtmlConstant("</a>");
            }
        }

        for (FormTree.Node node : formTree.getRootFields()) {
            ValueRendererFactory.create(formTree, node.getField()).ifPresent(renderer -> {
                html.appendHtmlConstant("<div class=\"sidepanel__details__field\" data-field=\"" + node.getFieldId() + "\">");
                html.appendHtmlConstant("<h4>");
                html.appendEscaped(node.getField().getLabel());
                html.appendHtmlConstant("</h4>");
                html.appendHtmlConstant("<div class=\"sidepanel__details__value\"></div></div>");

                fieldRenderers.add(new FieldRenderer(node.getFieldId(), renderer));
            });
        }

        return html.toSafeHtml();
    }


    public void update(XElement parent, RecordTree recordTree) {
        FormInstance record = recordTree.getRoot();

        for (FieldRenderer fieldRenderer : fieldRenderers) {
            FieldValue fieldValue = record.get(fieldRenderer.fieldId);
            XElement fieldElement = parent.selectNode("div[data-field=\"" + fieldRenderer.fieldId.asString() + "\"]");
            if(fieldValue == null) {
                fieldElement.setVisible(false);
            } else {
                fieldElement.setVisible(true);
                XElement valueElement = fieldElement.selectNode(".sidepanel__details__value");
                valueElement.setInnerSafeHtml(fieldRenderer.renderer.render(recordTree, fieldValue));
            }
        }
    }
}
