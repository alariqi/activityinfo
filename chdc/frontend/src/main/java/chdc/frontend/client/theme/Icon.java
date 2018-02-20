package chdc.frontend.client.theme;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * List of all the icons used in the CHDC application.
 *
 */
public enum Icon {

    BAR_CHART,
    CLOSE,
    HELP,
    TABLE,
    PLUS,
    LIST,
    LOGOUT,
    SAVE,
    SEARCH,
    SHARE,
    PENCIL,
    PRINTER;

    public final SafeHtml small() {
        return svg(IconStyle.SMALL_DIMMED);
    }

    public final SafeHtml normal() {
        return svg(IconStyle.LARGE);
    }

    public final SafeHtml svg(IconStyle size) {
        return SafeHtmlUtils.fromSafeConstant(
                "<svg viewBox=\"0 0 64 64\" class=\"" +
                        ChdcTheme.INSTANCE.style().icon() + " " +
                        (size == IconStyle.SMALL_DIMMED ? ChdcTheme.INSTANCE.style().iconSmall()  : "") +  "\">" +
                        "<use xmlns:xlink=\"http://www.w3.org/1999/xlink\" xlink:href=\"" + symbolName() + "\"></use>" +
                        "</svg>");
    }

    private String symbolName() {
        return "#icon-" + name().toLowerCase().replace('_', '-');
    }
}
