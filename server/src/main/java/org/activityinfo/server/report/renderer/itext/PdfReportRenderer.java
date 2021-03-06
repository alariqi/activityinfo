/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.server.report.renderer.itext;

import com.google.code.appengine.awt.Graphics2D;
import com.google.inject.Inject;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import org.activityinfo.server.geo.AdminGeometryProvider;
import org.activityinfo.server.report.generator.MapIconPath;
import org.activityinfo.server.report.renderer.image.ImageCreator;
import org.activityinfo.server.report.renderer.image.ItextGraphic;

import java.io.OutputStream;
import java.net.URL;

/**
 * iText ReportRenderer targeting PDF output
 */
public class PdfReportRenderer extends ItextReportRenderer {

    private PdfWriter writer;

    @Inject
    public PdfReportRenderer(AdminGeometryProvider geometryProvider, @MapIconPath String mapIconPath) {
        super(geometryProvider, mapIconPath);
    }

    @Override
    protected DocWriter createWriter(Document document, OutputStream os) throws DocumentException {
        writer = PdfWriter.getInstance(document, os);
        writer.setStrictImageSequence(true);

        return writer;
    }

    @Override
    public String getMimeType() {
        return "application/pdf";
    }

    @Override
    public String getFileSuffix() {
        return ".pdf";
    }

    @Override
    protected void renderFooter(Document document) {
        HeaderFooter footer = new HeaderFooter(new Phrase("Page ", ThemeHelper.footerFont()), true);
        document.setFooter(footer);
    }

    @Override
    protected ImageCreator getImageCreator() {
        return new PdfVectorImageCreator();
    }

    private class PdfVectorImageCreator implements ImageCreator {
        @Override
        public PdfVectorImage create(int width, int height) {
            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate template = cb.createTemplate(width, height);

            return new PdfVectorImage(template, template.createGraphics(width, height), height);
        }

        @Override
        public ItextGraphic createMap(int width, int height) {
            return create(width, height);
        }
    }

    private static class PdfVectorImage implements ItextGraphic {
        private PdfTemplate template;
        private Graphics2D g2d;
        private int height;

        public PdfVectorImage(PdfTemplate template, Graphics2D g2d, int height) {
            super();
            this.template = template;
            this.g2d = g2d;
            this.height = height;
        }

        @Override
        public Graphics2D getGraphics() {
            return g2d;
        }

        @Override
        public Image toItextImage() throws BadElementException {
            g2d.dispose();
            Image image = Image.getInstance(template);
            image.scalePercent(72f / 92f * 100f);

            return image;
        }

        @Override
        public void addImage(String imageUrl, int x, int y, int width, int height) {
            Image image;
            try {
                image = Image.getInstance(new URL(imageUrl));
            } catch (Exception e) {
                // ignore missing tiles
                return;
            }
            try {
                int top = this.height - y - height;
                image.setAbsolutePosition(x, top);
                template.addImage(image, false);

            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
