package org.activityinfo.ui.client.table.view;


import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.core.client.dom.XElement;
import org.activityinfo.ui.client.table.model.TableUpdater;
import org.activityinfo.ui.client.table.viewModel.TableViewModel;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.Props;
import org.activityinfo.ui.vdom.shared.tree.VComponent;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.logging.Logger;

public class GridContainer extends VComponent implements ResizeHandler {

    private final Logger LOGGER = Logger.getLogger(GridContainer.class.getName());
    private final TableViewModel viewModel;
    private final TableUpdater updater;

    private int marginHorizontal;
    private int marginVertical;

    private TableGrid grid;
    private boolean gridAttached;
    private HandlerRegistration windowResizeRegistration;

    public GridContainer(TableViewModel viewModel, TableUpdater updater) {
        this.viewModel = viewModel;
        this.updater = updater;
    }

    @Override
    protected VTree render() {
        return new VNode(HtmlTag.DIV, Props.withClass("formtable__gridcontainer"));
    }

    @Override
    protected void componentDidMount() {
        windowResizeRegistration = Window.addResizeHandler(this);
        tryMeasureAndAttach();
    }

    @Override
    protected void componentWillUnmount() {
        LOGGER.info("GridContainer: Detaching grid...");

        if(windowResizeRegistration != null) {
            windowResizeRegistration.removeHandler();
            windowResizeRegistration = null;
        }

        if (grid != null) {
            getContext().detachWidget(grid.asWidget());
        }
    }

    /**
     * After this element is initially attached to the DOM, measure our size relative to the window
     * size so that we can update it later if the window is resized.
     */
    private void tryMeasureAndAttach() {

        if (!isMounted()) {
            return;
        }

        int offsetWidth = getDomNode().getOffsetWidth();
        int offsetHeight = getDomNode().getOffsetHeight();

        if (offsetHeight == 0) {
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

            createGrid();
            attachGrid();
        }
    }

    private void createGrid() {
        if (grid == null) {

            LOGGER.info("Creating grid...");

            grid = new TableGrid(viewModel, viewModel.getEffectiveTable(), updater);
        }
    }

    private void attachGrid() {

        if (gridAttached) {
            return;
        }

        LOGGER.info("Attaching grid...");

        XElement gridEl = (XElement) grid.asWidget().getElement();

        // Set the grid to "position: absolute" so that it does not
        // affect the size of this container and screw with the flexbox layout.
        gridEl.makePositionable(true);

        applyGridSize();


        getContext().attachWidget(grid.asWidget(), getDomNode());

        gridAttached = true;
    }


    @Override
    public void onResize(ResizeEvent resizeEvent) {
        if (grid != null && isMounted()) {
            applyGridSize();
        }
    }

    private void applyGridSize () {
        grid.setPixelSize(Window.getClientWidth() - marginHorizontal,
                Window.getClientHeight() - marginVertical);
    }

}

