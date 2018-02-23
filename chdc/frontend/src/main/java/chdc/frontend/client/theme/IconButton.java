package chdc.frontend.client.theme;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.button.CellButtonBase;

import static chdc.frontend.client.theme.ChdcTheme.STYLES;

public class IconButton extends CellButtonBase<String> {


    @UiConstructor
    public IconButton(Icon icon, String label, IconStyle iconStyle) {
        super(new ButtonCell<>(new IconButtonAppearance(icon, iconStyle)), label);
    }

    private static class IconButtonAppearance implements ButtonCell.ButtonCellAppearance<String> {

        private Icon icon;
        private IconStyle style;

        public IconButtonAppearance(Icon icon, IconStyle style) {
            this.icon = icon;
            this.style = style;
        }

        @Override
        public void render(ButtonCell<String> cell, Cell.Context context, String value, SafeHtmlBuilder sb) {
            sb.appendHtmlConstant("<button class=\"" + STYLES.button() + " " + STYLES.buttonIcon() + "\">");
            sb.append(icon.svg(style));
            sb.appendHtmlConstant("<span>");
            sb.appendEscaped(value);
            sb.appendHtmlConstant("</span>");
            sb.appendHtmlConstant("</button>");
        }

        @Override
        public XElement getButtonElement(XElement parent) {
            return (XElement)parent.getFirstChildElement().cast();
        }

        @Override
        public XElement getFocusElement(XElement parent) {
            return (XElement)parent.getFirstChildElement().cast();
        }

        @Override
        public void onFocus(XElement parent, boolean focused) {
        }

        @Override
        public void onOver(XElement parent, boolean over) {
        }

        @Override
        public void onPress(XElement parent, boolean pressed) {
        }

        @Override
        public void onToggle(XElement parent, boolean pressed) {
        }
    }
}
