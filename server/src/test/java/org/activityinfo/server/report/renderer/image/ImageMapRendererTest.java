package org.activityinfo.server.report.renderer.image;

import org.activityinfo.legacy.shared.reports.content.PieMapMarker;
import org.activityinfo.legacy.shared.reports.content.PieMapMarker.SliceValue;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageMapRendererTest {

    @Test
    public void testPieChartRenderer() throws IOException {

        renderPieChart(5);
        renderPieChart(10);
        renderPieChart(89);
        renderPieChart(90);
        renderPieChart(91);
        renderPieChart(170);
        renderPieChart(180);
        renderPieChart(185);
        renderPieChart(265);
        renderPieChart(270);
        renderPieChart(275);
        renderPieChart(300);
        renderPieChart(350);
        renderPieChart(360);
    }

    private void renderPieChart(double value) throws FileNotFoundException, IOException {
        int radius = 21;


        SliceValue s1 = new SliceValue();
        s1.setColor("4169E1");
        s1.setValue(value);

        SliceValue s2 = new SliceValue();
        s2.setColor("EEEE00");
        s2.setValue(360d - value);

        PieMapMarker pmm = new PieMapMarker();
        pmm.setRadius(21);
        pmm.setColor("FF0000");
        pmm.getSlices().add(s1);
        pmm.getSlices().add(s2);
        pmm.setX(radius);
        pmm.setY(radius);


        BufferedImage icon = new BufferedImage(radius * 2, radius * 2,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();

        g2d.setPaint(new Color(255, 255, 255, 0));
        g2d.fillRect(0, 0, radius * 2, radius * 2);

        ImageMapRenderer.drawPieMarker(g2d, pmm);

        File outputFile = new File("build/report-tests/pieChart-" + ((int) value) + ".png");
        outputFile.getParentFile().mkdirs();

        FileOutputStream fos = new FileOutputStream(outputFile);
        ImageIO.write(icon, "PNG", fos);
        fos.close();
    }

}
