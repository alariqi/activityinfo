package org.activityinfo.ui.client.base.field;

import org.activityinfo.ui.client.base.Svg;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

public class RadioButton {

    private static int NEXT_CHECKBOX_ID = 1;

    private String name;
    private String type = "radio";
    private String label;
    private final PropMap inputProps;
    private final String inputId;

    public RadioButton() {
        inputId = "checkbox" + (NEXT_CHECKBOX_ID++);
        inputProps = Props.create()
                .setId(inputId)
                .set("name", this.name)
                .set("type", this.type);
    }

    public RadioButton label(String label) {
        this.label = label;
        return this;
    }

    public RadioButton name(String groupName) {
        this.name = name;
        return this;
    }

    public RadioButton onchange(EventHandler handler) {
        inputProps.set("onchange", handler);
        return this;
    }

    public VTree render() {
        PropMap fieldSetProps = Props.withClass("fieldset__" + this.type);


        PropMap labelProps = Props.create()
                .set("for", inputId);

        return new VNode(HtmlTag.FIELDSET, fieldSetProps,
                new VNode(HtmlTag.INPUT, inputProps),
                new VNode(HtmlTag.LABEL, labelProps,
                        new VNode(HtmlTag.SPAN, new VText(label))),
                new VNode(HtmlTag.DIV, Props.withClass(type),
                        Svg.svg("icon", "#" + type, "0 0 21 17")));
    }
}
