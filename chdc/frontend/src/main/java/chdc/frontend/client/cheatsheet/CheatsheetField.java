package chdc.frontend.client.cheatsheet;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.IsField;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.formTree.LookupKeySet;
import org.activityinfo.model.resource.ResourceId;
import org.activityinfo.store.query.shared.FormSource;
import org.activityinfo.ui.client.lookup.viewModel.LookupKeyViewModel;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;
import org.activityinfo.ui.client.table.view.LabeledRecordRef;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class CheatsheetField implements IsField<LabeledRecordRef> {

    private static final Logger LOGGER = Logger.getLogger(CheatsheetField.class.getName());

    private final ResourceId referencedFormId;

    private LookupKeyViewModel level;

    private final CheatsheetPanel cheatsheet;
    private final SlideoutPanel slideOutPanel;

    private ListStore<LabeledRecordRef> store;
    private ComboBox<LabeledRecordRef> comboBox;

    public CheatsheetField(FormSource formSource, String fieldLabel, LookupKeySet lookupKeySet) {

        this.referencedFormId = Iterables.getOnlyElement(lookupKeySet.getRange());

        LookupViewModel viewModel = new LookupViewModel(formSource, lookupKeySet);

        // Setup the slideout panel with the cheatsheet

        cheatsheet = new CheatsheetPanel(viewModel);
        cheatsheet.getLeafColumn().addSelectionHandler(new SelectionHandler<String>() {
            @Override
            public void onSelection(SelectionEvent<String> event) {
                viewModel.getSelectedLabeledRecord().once().then(new Function<Optional<LabeledRecordRef>, Void>() {
                    @Nullable
                    @Override
                    public Void apply(@Nullable Optional<LabeledRecordRef> input) {
                        if(input.isPresent()) {

                            LOGGER.info("Updating combo box based on cheatsheet value: " + input.get());

                            comboBox.setValue(input.get());
                        }
                        return null;
                    }
                });
            }
        });

        slideOutPanel = new SlideoutPanel();
        slideOutPanel.setTitleHeading(I18N.MESSAGES.findFieldValue(fieldLabel));
        slideOutPanel.add(cheatsheet);
        slideOutPanel.attach();

        store = new ListStore<>(key -> key.getRecordId());

        comboBox = new ComboBox<>(new CheatsheetComboCell(store));
        comboBox.addTriggerClickHandler(new TriggerClickEvent.TriggerClickHandler() {
            @Override
            public void onTriggerClick(TriggerClickEvent event) {
                LabeledRecordRef ref = comboBox.getValue();
                if(ref == null) {
                    viewModel.setInitialSelection(Collections.emptySet());
                } else {
                    viewModel.setInitialSelection(Collections.singleton(ref.getRecordRef()));
                }
                slideOutPanel.show();
            }
        });
    }

    @Override
    public void clear() {
        comboBox.clear();
    }

    @Override
    public void clearInvalid() {
        comboBox.clearInvalid();
    }

    @Override
    public void finishEditing() {
        comboBox.finishEditing();
    }

    @Override
    public List<EditorError> getErrors() {
        return comboBox.getErrors();
    }

    @Override
    public boolean isValid(boolean preventMark) {
        return comboBox.isValid(preventMark);
    }

    @Override
    public void reset() {
        comboBox.reset();
    }

    @Override
    public boolean validate(boolean preventMark) {
        return validate(preventMark);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<LabeledRecordRef> handler) {
        return comboBox.addValueChangeHandler(handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        comboBox.fireEvent(event);
    }

    @Override
    public void setValue(LabeledRecordRef value) {
        comboBox.setValue(value);
    }

    @Override
    public LabeledRecordRef getValue() {
        return comboBox.getValue();
    }

    @Override
    public Widget asWidget() {
        return comboBox;
    }

    @Override
    public HandlerRegistration addBlurHandler(BlurEvent.BlurHandler handler) {
        return comboBox.addBlurHandler(new BlurEvent.BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if(!slideOutPanel.isVisible()) {
                    handler.onBlur(event);
                }
            }
        });
    }
}
