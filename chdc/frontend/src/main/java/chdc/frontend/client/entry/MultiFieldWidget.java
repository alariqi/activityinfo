package chdc.frontend.client.entry;

import com.google.gwt.user.client.ui.IsWidget;

import java.util.Collection;

/**
 * Interface for FieldWidgets that have multiple fields
 */
public interface MultiFieldWidget {

    Collection<IsWidget> getWidgets();

}
