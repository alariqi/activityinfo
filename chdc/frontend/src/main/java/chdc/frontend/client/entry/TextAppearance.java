package chdc.frontend.client.entry;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.FieldCell;
import com.sencha.gxt.cell.core.client.form.TextInputCell;
import com.sencha.gxt.core.client.dom.XElement;

public class TextAppearance implements TextInputCell.TextFieldAppearance {

    private final String label;
    private final String type;

    public TextAppearance(String label, String type) {
        this.label = label;
        this.type = type;
    }

    public TextAppearance(String label) {
        this(label, "text");
    }

    @Override
    public void render(SafeHtmlBuilder sb, String type, String value, FieldCell.FieldAppearanceOptions options) {

        //<label>
        //<div>Act</div>
        //<input type="text" value="Air-to-ground attack" placeholder="" name="">
        //</label>

        sb.appendHtmlConstant("<label>");
        sb.appendHtmlConstant("<div>");
        sb.appendEscaped(label);
        sb.appendHtmlConstant("</div>");
        sb.appendHtmlConstant("<input type=\"" + this.type + "\" value=\"");
        sb.appendEscaped(value);
        sb.appendHtmlConstant("\">");
        sb.appendHtmlConstant("</label>");
    }

    @Override
    public XElement getInputElement(Element parent) {
        Element label = parent.getFirstChildElement();
        Element input = label.getChild(1).cast();
        assert input.getTagName().equalsIgnoreCase("input");
        return input.cast();
    }

    @Override
    public void onResize(XElement parent, int width, int height) {
    }

    @Override
    public void onEmpty(Element parent, boolean empty) {
    }

    @Override
    public void onFocus(Element parent, boolean focus) {
    }

    @Override
    public void onValid(Element parent, boolean valid) {
    }

    @Override
    public void setReadOnly(Element parent, boolean readonly) {
    }
}
