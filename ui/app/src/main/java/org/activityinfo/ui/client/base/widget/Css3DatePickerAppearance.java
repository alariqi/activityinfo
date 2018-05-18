
package org.activityinfo.ui.client.base.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.DatePicker.DatePickerAppearance;
import com.sencha.gxt.widget.core.client.DatePicker.DatePickerMessages;
import com.sencha.gxt.widget.core.client.DatePicker.DateState;
import org.activityinfo.ui.client.Icon;

import java.util.List;

/**
 *
 */
public class Css3DatePickerAppearance implements DatePickerAppearance {


    public class Css3DatePickerStyle {

        String date() {
            return "datepicker__date";
        }

        String dateAnchor() {
            return "datepicker__date__anchor";
        }

        String dateActive() {
            return "datepicker__date--active";
        }

        String dateDisabled() {
            return "datepicker__date--disabled";
        }

        String dateNext() {
            return "datepicker__date--next";
        }

        String dateOver() {
            return "datepicker__date--over";
        }

        String datePicker() {
            return "datepicker";
        }

        String datePrevious() {
            return "datepicker__date--previous";
        }

        String dateSelected() {
            return "datepicker__date--selected";
        }

        String dateToday() {
            return "datepicker__date--today";
        }

        String monthSelected() {
            return "datepicker__month--selected";
        }

        String header() {
            return "datepicker__header";
        }

    }

    private final Css3DatePickerStyle style = new Css3DatePickerStyle();

    public Css3DatePickerAppearance() {
    }

    @Override
    public String dateSelector() {
        return "." + style.date();
    }

    @Override
    public String daySelector() {
        return "." + style.dateAnchor();
    }

    @Override
    public NodeList<Element> getDateCells(XElement parent) {
        return parent.select("." + style.date());
    }

    @Override
    public boolean isDisabled(Element cell) {
        return cell.<XElement>cast().hasClassName(style.dateDisabled());
    }

    @Override
    public String leftMonthSelector() {
        return ".datepicker__month--prev";
    }

    @Override
    public String rightMonthSelector() {
        return ".datepicker__month--next";
    }


    @Override
    public String monthButtonSelector() {
        return ".datepicker__month__button";
    }

    @Override
    public String todayButtonSelector() {
        return ".datepicker__footer";
    }


    @Override
    public String leftYearSelector() {
        return ".monthpicker__year--prev";
    }

    @Override
    public String rightYearSelector() {
        return ".monthpicker__year--next";
    }

    @Override
    public String monthPickerCancelSelector() {
        return ".monthpicker__cancel";
    }

    @Override
    public String monthPickerMonthSelector() {
        return ".monthpicker__month";
    }

    @Override
    public String monthPickerOkSelector() {
        return ".monthpicker__ok";
    }

    @Override
    public String monthPickerYearSelector() {
        return ".monthpicker__year";
    }

    @Override
    public void onMonthButtonHtmlChange(XElement parent, SafeHtml html) {
        parent.selectNode(".datepicker__month h3").setInnerSafeHtml(html);
    }

    @Override
    public void onMonthSelected(Element cell, boolean select) {
        cell.<XElement>cast().setClassName("monthpicker__month--selected", select);
    }

    @Override
    public void onHtmlChange(Element cell, SafeHtml html) {
        cell.getFirstChildElement().setInnerSafeHtml(html);
    }

    @Override
    public void onUpdateDateStyle(Element cell, DateState type, boolean add) {

        String cls = "";

        switch (type) {
            case ACTIVE:
                cls = style.dateActive();
                break;
            case DISABLED:
                cls = style.dateDisabled();
                break;
            case NEXT:
                cls = style.dateNext();
                break;
            case PREVIOUS:
                cls = style.datePrevious();
                break;
            case OVER:
                cls = style.dateOver();
                break;
            case SELECTED:
                cls = style.dateSelected();
                break;
            case TODAY:
                cls = style.dateToday();
                break;
        }

        XElement elem = cell.cast();
        elem.setClassName(cls, add);
    }

    @Override
    public void onUpdateDayOfWeeks(XElement parent, List<SafeHtml> days) {
        NodeList<Element> elems = parent.select(".datepicker__body th");
        for (int i = 0; i < elems.getLength(); i++) {
            Element elem = elems.getItem(i);
            SafeHtml day = days.get(i);
            elem.setInnerSafeHtml(day);
        }
    }

    @Override
    public void render(SafeHtmlBuilder sb) {
        sb.appendHtmlConstant("<div class='" + style.datePicker() + "'>");

        /* Month header div */
        sb.appendHtmlConstant("<div class='" + style.header() + "'>");

        /* prev month */
        sb.appendHtmlConstant("<button class='datepicker__month--prev'>");
        sb.append(Icon.BUBBLE_ARROWLEFT.render(16, 16));
        sb.appendHtmlConstant("</button>");

        /* Month name */
        sb.appendHtmlConstant("<div class='datepicker__month'>");
        sb.appendHtmlConstant("<h3></h3>");
        sb.appendHtmlConstant("<button class=\"datepicker__month__button\">");
        sb.append(Icon.OPTIONS_HORIZONTAL.render(18, 5));
        sb.appendHtmlConstant("</button>");
        sb.appendHtmlConstant("</div>");

        /* next month */
        sb.appendHtmlConstant("<button class='datepicker__month--next'>");
        sb.append(Icon.BUBBLE_ARROWRIGHT.render(16, 16));
        sb.appendHtmlConstant("</button>");

        sb.appendHtmlConstant("</div>");


        sb.appendHtmlConstant("<div role=grid class=datepicker__body><table width=100% cellpadding=0 cellspacing=0><thead><tr>");
        for (int i = 0; i < 7; i++) {
            sb.appendHtmlConstant("<th></th>");
        }
        sb.appendHtmlConstant("</tr></thead>");

        sb.appendHtmlConstant("<tbody>");
        for (int i = 0; i < 6; i++) {
            sb.appendHtmlConstant("<tr>");
            for (int j = 0; j < 7; j++) {
                sb.appendHtmlConstant("<td class=" + style.date() + "><a href=# class=" + style.dateAnchor()
                        + "></a></td>");
            }
            sb.appendHtmlConstant("</tr>");
        }
        sb.appendHtmlConstant("</tbody></table></div>");

        sb.appendHtmlConstant("<div class='datepicker__footer'>");
        sb.appendHtmlConstant("</div>");

        sb.appendHtmlConstant("</div>");
    }

    @Override
    public void renderMonthPicker(SafeHtmlBuilder sb, DatePickerMessages messages, String[] monthNames) {
        sb.appendHtmlConstant("<div class=monthpicker>");

        /* Years */

        sb.appendHtmlConstant("<div class='monthpicker__years'>");
        sb.appendHtmlConstant("<button class='monthpicker__year--prev'>");
        sb.append(Icon.BUBBLE_ARROWLEFT.render(16,  16));
        sb.appendHtmlConstant("</button>");

        sb.appendHtmlConstant("<div class='monthpicker__years__body'>");
        for (int i = 0; i < 10; i++) {
            sb.appendHtmlConstant("<div class='monthpicker__year'><span></span></div>");
        }
        sb.appendHtmlConstant("</div>");

        sb.appendHtmlConstant("<button class='monthpicker__year--next'>");
        sb.append(Icon.BUBBLE_ARROWLEFT.render(16,  16));
        sb.appendHtmlConstant("</button>");

        sb.appendHtmlConstant("</div>");


        /* Months */

        sb.appendHtmlConstant("<div class='monthpicker__months'>");
        for (int i = 0; i < 12; i++) {
            sb.appendHtmlConstant("<div class='monthpicker__month'><span>");
            sb.appendEscaped(monthNames[i]);
            sb.appendHtmlConstant("</span></div>");
        }
        sb.appendHtmlConstant("</div>");

        /* Footer */

        sb.appendHtmlConstant("<div class='monthpicker__footer'>");
        sb.appendHtmlConstant("<div class='monthpicker__ok'></div>");
        sb.appendHtmlConstant("<div class='monthpicker__cancel'></div>");
        sb.appendHtmlConstant("</div>");

        sb.appendHtmlConstant("</div>");
    }

    @Override
    public void onMonthPickerSize(XElement monthPickerParent, int width, int height) {
        monthPickerParent.setTop(0);
        monthPickerParent.setLeft(0);
        monthPickerParent.setSize(width, height);
        XElement monthPicker = monthPickerParent.getFirstChildElement().cast();
        monthPicker.setSize(width, height);
    }
}
