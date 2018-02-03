package org.activityinfo.server.report.renderer.itext;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.lowagie.text.*;
import com.lowagie.text.Font;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.legacy.shared.reports.model.PivotChartReportElement;
import org.activityinfo.server.report.renderer.ChartRendererJC;
import org.activityinfo.server.report.renderer.image.ImageCreator;
import org.activityinfo.server.report.renderer.image.ItextGraphic;

import java.awt.*;

public class ItextChartRenderer implements ItextRenderer<PivotChartReportElement> {

    private final ImageCreator imageCreator;
    private final ChartRendererJC chartRenderer = new ChartRendererJC();

    public ItextChartRenderer(ImageCreator imageCreator) {
        super();
        this.imageCreator = imageCreator;
    }

    @Override
    public void render(DocWriter writer, Document doc, PivotChartReportElement element) throws DocumentException {

        doc.add(ThemeHelper.elementTitle(element.getTitle()));
        ItextRendererHelper.addFilterDescription(doc, element.getContent().getFilterDescriptions());
        ItextRendererHelper.addDateFilterDescription(doc, element.getFilter().getEndDateRange());

        if (element.getContent().getData().isEmpty()) {
            Paragraph para = new Paragraph(I18N.CONSTANTS.noData());
            para.setFont(new Font(Font.HELVETICA, 12, Font.NORMAL, new Color(0, 0, 0)));
            doc.add(para);

        } else {
            float width = doc.getPageSize().getWidth() - doc.rightMargin() - doc.leftMargin();
            float height = (doc.getPageSize().getHeight() - doc.topMargin() - doc.bottomMargin()) / 3f;

            renderImage(writer, doc, element, width, height);
        }
    }

    protected void renderImage(DocWriter writer,
                               Document doc,
                               PivotChartReportElement element,
                               float width,
                               float height) throws BadElementException, DocumentException {

        ItextGraphic image = imageCreator.create((int) width, (int) height);

        chartRenderer.render(element, false, image.getGraphics(), (int) width, (int) height, 72);

        doc.add(image.toItextImage());

    }

}
