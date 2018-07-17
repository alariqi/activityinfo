package org.activityinfo.ui.client.base;

import com.google.gwt.core.client.GWT;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleTimer {

    private static final Logger LOGGER = Logger.getLogger(ConsoleTimer.class.getName());

//    private static Map<String, Long> eventStart;

    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public static void time(String label) {

        if (LOGGER.isLoggable(Level.INFO)) {
            if (GWT.isScript()) {
                consoleTime(label);
            } else {
//                LOGGER.info("Started timing " + label + "...");
//                if(eventStart == null) {
//                    eventStart = new HashMap<>();
//                }
//                eventStart.put(label, System.nanoTime());
            }
        }
    }

    @SuppressWarnings("NonJREEmulationClassesInClientCode")
    public static void timeEnd(String label) {
        if (LOGGER.isLoggable(Level.INFO)) {
            if (GWT.isScript()) {
                consoleTimeEnd(label);
            } else {
//                LOGGER.info("Started timing " + label + "...");
//                if(eventStart == null) {
//                    eventStart = new HashMap<>();
//                }
//                Long startTime = eventStart.get(label);
//                if(startTime != null) {
//                    long duration = System.nanoTime() - startTime;
//                    LOGGER.info("Finished timing " + label + ": " + Duration.ofNanos(duration));
//                }
            }
        }
    }

    private static native void consoleTime(String label) /*-{
        if($wnd.console.time) {
            $wnd.console.time(label);
        }
    }-*/;

    private static native void consoleTimeEnd(String label) /*-{
        if($wnd.console.timeEnd) {
            $wnd.console.timeEnd(label);
        }
    }-*/;

}
