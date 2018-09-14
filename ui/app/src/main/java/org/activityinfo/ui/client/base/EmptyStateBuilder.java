package org.activityinfo.ui.client.base;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Constructs an empty state filler
 */
public class EmptyStateBuilder {

    private String explanationText;
    private List<VTree> buttons = new ArrayList<>();


    /**
     *
     * Set the primary explanation text that appears below the "Bit empty in here" header. This should
     * provide the user with some help in taking the next step.
     *
     * For example,
     *
     * <blockquote>
     *     It appears there's nothing in this form list. Get started by clicking the "Add Form" or "Import data"
     *     buttons that are highlighted.
     * </blockquote>
     *
     */
    public EmptyStateBuilder setExplanationText(String text) {
        this.explanationText = text;
        return this;
    }

    /**
     * Adds a button to the list of actions to take.
     */
    public EmptyStateBuilder addButton(VTree button) {
        buttons.add(button);
        return this;
    }

    /**
     * Set the text that refers the user to the documentation. For example,
     *
     * <blockquote>
     *     If you need help getting started, please read our getting started guide on how
     *     to add a database
     * </blockquote>
     */
    public EmptyStateBuilder setHelpText(String text) {
        return this;
    }

    public VTree build() {
        List<VTree> children = new ArrayList<>();
        children.add(NonIdeal.illustration("#nis_emptystate"));
        children.add(new VNode(HtmlTag.H2, new VText(I18N.CONSTANTS.emptyHeader())));

        children.add(new VNode(HtmlTag.P, new VText(explanationText)));

        if(!buttons.isEmpty()) {
            children.add(new VNode(HtmlTag.DIV, PropMap.EMPTY, buttons));
        }

        return new VNode(HtmlTag.DIV, Props.withClass("nonideal"), children);
    }

}
