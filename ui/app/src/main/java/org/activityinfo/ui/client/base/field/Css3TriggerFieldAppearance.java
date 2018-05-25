
package org.activityinfo.ui.client.base.field;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.FieldCell.FieldAppearanceOptions;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell.TriggerFieldAppearance;
import com.sencha.gxt.core.client.dom.XElement;
import org.activityinfo.ui.client.Icon;

/**
 *
 */
public class Css3TriggerFieldAppearance extends Css3ValueBaseFieldAppearance implements TriggerFieldAppearance {

    @Override
    public final XElement getInputElement(Element parent) {
        return parent.<XElement>cast().selectNode("input");
    }

    @Override
    public void onResize(XElement parent, int width, int height, boolean hideTrigger) {
        if (width != -1) {
            width = Math.max(0, width);
            parent.getFirstChildElement().getStyle().setPropertyPx("width", width);
        }
    }

    @Override
    public void onTriggerClick(XElement parent, boolean click) {
    }

    @Override
    public void onTriggerOver(XElement parent, boolean over) {
    }

    @Override
    public void render(SafeHtmlBuilder sb, String value, FieldAppearanceOptions options) {
        sb.appendHtmlConstant("<div class='field__wrap'>");
        renderInput(sb, value, options);
        renderTrigger(sb, value, options);
        sb.appendHtmlConstant("</div>");
    }

    @Override
    public final void setEditable(XElement parent, boolean editable) {
        parent.setClassName("field--readonly", !editable);
    }

    protected void renderTrigger(SafeHtmlBuilder sb, String value, FieldAppearanceOptions options) {
        sb.appendHtmlConstant("<div class=\"field__trigger\">");
        sb.append(Icon.EXPAND_DOWN.render());
        sb.appendHtmlConstant("</div>");
    }

    @Override
    public boolean triggerIsOrHasChild(XElement parent, Element target) {
        return parent.isOrHasChild(target) &&
                target.<XElement>cast().findParentElement(".field__trigger", 3) != null;
    }


}
