package org.activityinfo.ui.vdom.shared;

import org.activityinfo.ui.vdom.shared.tree.VComponent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class VDomLogger {

    public static final String PADDING = "                                  ";
    private static int nextId = 1;

    private static final Logger LOGGER = Logger.getLogger("VDom");

    private static final Level LOG_LEVEL = Level.INFO;

    public static boolean STD_OUT = true;

    public static void event(VComponent component, String eventName) {
       // LOGGER.log(LOG_LEVEL, componentId(component) + "." + eventName);
        if(STD_OUT) {
            System.out.println(padString(eventName, 15) + mountIcon(component) + " " +
                dirtyIcon(component) + " " + component.getDebugId());
        }
    }

    private static String mountIcon(VComponent component) {
        return component.isMounted() ? "m" : " ";
    }

    private static String dirtyIcon(VComponent component) {
        return component.isDirty() ? "d" : " ";
    }

    private static String padString(String s, int len) {
        return s + PADDING.substring(0, Math.max(0, len - s.length()));
    }

    public static int nextDebugId() {
        return nextId++;
    }

    public static void start(String event) {
        if(STD_OUT) {
            System.out.println();
            System.out.println("---- " + event + " -----");
        }
    }

    public static void event(String message) {
        if(STD_OUT) {
            System.out.println();
            System.out.println("#### " + message + " ####");
            System.out.println();
        }
    }

}
