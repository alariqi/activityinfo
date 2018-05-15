package org.activityinfo.ui.client.table.view;


import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.widget.core.client.container.Container;

import java.util.logging.Logger;

class GridContainer extends Container {

    private final Logger LOGGER = Logger.getLogger(GridContainer.class.getName());

    private int marginHorizontal;
    private int marginVertical;

    private Widget grid;
    private boolean gridAttached;

    public GridContainer() {
        setElement(Document.get().createDivElement());
        setMonitorWindowResize(true);
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        tryMeasureAndAttach();
    }

    /**
     * After this element is initially attached to the DOM, measure our size relative to the window
     * size so that we can update it later if the window is resized.
     *
     * <p>This assumes that the </p>
     */
    private void tryMeasureAndAttach() {
        int offsetWidth = getElement().getOffsetWidth();
        int offsetHeight = getElement().getOffsetHeight();

        if(offsetHeight == 0) {
            LOGGER.info("Gridcontainer: height = zero, scheduling recheck");
            Scheduler.get().scheduleDeferred(this::tryMeasureAndAttach);

        } else {

            LOGGER.info("GridContainer: attaching");
            int windowWidth = Window.getClientWidth();
            int windowHeight = Window.getClientHeight();
            marginHorizontal = windowWidth - offsetWidth;
            marginVertical = windowHeight - offsetHeight;

            LOGGER.info("offsetSize = [" + offsetWidth + "x" + offsetHeight + "]," +
                    " marginHorizontal = " + marginHorizontal +
                    " marginVertical = " + marginVertical);

            if (grid != null && !gridAttached) {
                attachGrid();
            }
        }
    }

    public void setGrid(IsWidget newGrid) {
        setGrid(Widget.asWidgetOrNull(newGrid));
    }

    public void setGrid(Widget newGrid) {
        if(grid != null && gridAttached) {
            grid.removeFromParent();
        }
        grid = newGrid;
        gridAttached = false;

        if(isAttached()) {
            attachGrid();
        } else {
            LOGGER.info("Delaying grid attachment until we have measured our size");
        }
    }

    private void attachGrid() {

        LOGGER.info("Attaching grid...");

        XElement gridEl = (XElement) grid.getElement();

        // Set the grid to "position: absolute" so that it does not
        // affect the size of this container and screw with the flexbox layout.
        gridEl.makePositionable(true);

        applyGridSize();

        insert(grid, 0);

        gridAttached = true;
    }

    @Override
    protected void onWindowResize(int width, int height) {
        if(grid != null && isAttached()) {
            applyGridSize();
        }
    }

    private void applyGridSize() {
        grid.setPixelSize(Window.getClientWidth()  - marginHorizontal - sidePanelWidth(),
                          Window.getClientHeight() - marginVertical);
    }

    private int sidePanelWidth() {
        double rem = getRootFontSize();
        int width = (int)(rem * 1.5);
        LOGGER.info("rem = " + rem + "px, width = " + width);
        return width;
    }

    private static native double getRootFontSize() /*-{
        return parseFloat($wnd.getComputedStyle($wnd.document.body).fontSize);
    }-*/;
}
