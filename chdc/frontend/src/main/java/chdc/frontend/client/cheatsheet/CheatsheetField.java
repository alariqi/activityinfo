package chdc.frontend.client.cheatsheet;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.form.IsField;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.model.type.RecordRef;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;
import org.activityinfo.ui.client.table.view.LabeledReference;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * A field widget for reference fields that shows only the leaf key in the
 * main form, but provides a popup with the complete selection of keys.
 */
public class CheatsheetField implements IsField<LabeledReference> {

    private static final Logger LOGGER = Logger.getLogger(CheatsheetField.class.getName());

    private final SimpleEventBus eventBus = new SimpleEventBus();

    private final ResourceId referencedFormId;
    private final LookupViewModel viewModel;

    private final CheatsheetCombo combo;
    private final SlideoutPanel slideOutPanel;
    private final CheatsheetPanel cheatsheet;

    private LabeledReference value;

    public CheatsheetField(FormSource formSource, String fieldLabel, LookupKeySet lookupKeySet) {
        this.referencedFormId = Iterables.getOnlyElement(lookupKeySet.getRange());
        this.viewModel = new LookupViewModel(formSource, lookupKeySet);

        combo = new CheatsheetCombo(this.viewModel.getLeafLookupKey());

        // Setup the slideout panel with the cheatsheet

        cheatsheet = new CheatsheetPanel(viewModel);

        slideOutPanel = new SlideoutPanel();
        slideOutPanel.setTitleHeading(I18N.MESSAGES.findFieldValue(fieldLabel));
        slideOutPanel.add(cheatsheet);
        slideOutPanel.attach();

        combo.addTriggerHandler(event -> {
            slideOutPanel.show();
            schedulePanelFocus();
        });

        slideOutPanel.addCloseHandler(event -> {
        });

        cheatsheet.getLeafColumn().addSelectionHandler(event -> {

            LabeledReference newValue = getValue();

            LOGGER.info("CheatsheetField: broadcasting new value " + newValue);

            combo.setValue(newValue);

            ValueChangeEvent.fire(this, newValue);

        });
    }

    /**
     * Handle a new selection of a leaf node by the user.
     */
    @Override
    public LabeledReference getValue() {

        LOGGER.info("Cheatsheet: getting value");

        // If the user was able to select something, then everything
        // must be loaded, but just in case...
        if (viewModel.getSelectedRecord().isLoading()) {
            LOGGER.warning("Cheatsheet: still loading, aborting selection.");
            return null;
        }

        // All of the reference fields are required, so an empty selection
        // is not a valid selection.
        Optional<RecordRef> maybeRef = viewModel.getSelectedRecord().get();
        if(!maybeRef.isPresent()) {
            LOGGER.warning("Cheatsheet: empty selection, aborting.");
            return null;
        }

        RecordRef selectedRef = maybeRef.get();

        // Phew, we have a reference id, now we need the label
        // Again, we assume if the user has been able to click on something, then
        // everything is loaded.
        String label = viewModel.getLeafLookupKey().getSelectedKey().get().get();

        return new LabeledReference(selectedRef.getRecordId().asString(), label);
    }

    /**
     * Focus keyboard input on the first column of the cheatsheet view, but only after the panel
     * has slid into view, otherwise we end up with weird layout issues.
     */
    private void schedulePanelFocus() {
        Scheduler.get().scheduleFixedDelay(() -> {
            cheatsheet.focus();
            // Stop repeating...
            return false;
        }, 200);
    }


    /**
     * The {@link com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing} component
     * calls this method to listen for changes to the cell's value.
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<LabeledReference> handler) {
        return eventBus.addHandler(ValueChangeEvent.getType(), handler);
    }

    /**
     * The {@link com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing} component
     * calls this method to listen for users leaving the field. The value for the field will be reverted
     * to either the original value, or to the value broadcast by the {@link ValueChangeEvent}.
     *
     */
    @Override
    public HandlerRegistration addBlurHandler(BlurEvent.BlurHandler handler) {

        HandlerRegistration comboRegistration = combo.addBlurHandler(event -> {
            if (!slideOutPanel.isVisible()) {
                handler.onBlur(event);
            }
        });

        HandlerRegistration panelRegistration = slideOutPanel.addCloseHandler(event ->
                handler.onBlur(new BlurEvent()));

        return () -> {
            comboRegistration.removeHandler();
            panelRegistration.removeHandler();
        };
    }

    @Override
    public void setValue(LabeledReference value) {
        if(value == null) {
            clear();
        } else {
            viewModel.setInitialSelection(
                    Collections.singleton(
                            new RecordRef(referencedFormId, ResourceId.valueOf(value.getRecordId()))));
            this.value = value;
        }
    }


    @Override
    public void clear() {
        viewModel.setInitialSelection(Collections.emptySet());
        value = null;
    }

    @Override
    public void clearInvalid() {
    }

    @Override
    public void finishEditing() {
    }

    @Override
    public List<EditorError> getErrors() {
        return Collections.emptyList();
    }

    @Override
    public boolean isValid(boolean preventMark) {
        // Check to see if a leaf selection has been made
        if (viewModel.getSelectedRecord().isLoading()) {
            return false;
        }
        return viewModel.getSelectedRecord().get().isPresent();
    }

    @Override
    public void reset() {

    }

    @Override
    public boolean validate(boolean preventMark) {
        return true;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        eventBus.fireEvent(event);
    }

    @Override
    public Widget asWidget() {
        return combo.asWidget();
    }


}
