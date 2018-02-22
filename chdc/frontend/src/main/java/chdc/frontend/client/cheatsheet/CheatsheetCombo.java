package chdc.frontend.client.cheatsheet;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.Subscription;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.ui.client.lookup.viewModel.LookupKeyViewModel;
import org.activityinfo.ui.client.table.view.LabeledReference;

import java.util.Objects;
import java.util.logging.Logger;

public class CheatsheetCombo implements IsWidget {

    private static final Logger LOGGER = Logger.getLogger(CheatsheetCombo.class.getName());

    private LookupKeyViewModel level;

    private ListStore<LabeledReference> store;
    private ComboBox<LabeledReference> comboBox;

    private Subscription choiceSubscription;
    private SubscriptionSet subscriptionSet = new SubscriptionSet();

    private boolean relevant = true;

    public CheatsheetCombo(LookupKeyViewModel level) {
        this.level = level;

        store = new ListStore<>(key -> key.getRecordId());

        comboBox = new ComboBox<>(new CheatsheetCell(store));
        comboBox.setWidth(300);

        comboBox.addAttachHandler(this::onAttach);
//
//        comboBox.addSelectionHandler(new SelectionHandler<LabeledReference>() {
//            @Override
//            public void onSelection(SelectionEvent<String> event) {
//                selectViewModel.select(level.getLookupKey(), event.getSelectedItem());
//            }
//        });
    }

    public HandlerRegistration addTriggerHandler(TriggerClickEvent.TriggerClickHandler handler) {
        return comboBox.addTriggerClickHandler(handler);
    }

    public HandlerRegistration addAttachHandler(AttachEvent.Handler handler) {
        return comboBox.addAttachHandler(handler);
    }

    private void onAttach(AttachEvent attachEvent) {
        if(attachEvent.isAttached()) {
            subscriptionSet.add(level.getSelectedKey().debounce(50).subscribe(this::updateSelectionView));
            subscriptionSet.add(level.isEnabled().subscribe(enabled -> updateEnabledView()));
        } else {
            subscriptionSet.unsubscribeAll();
        }
    }

    private void updateSelectionView(Observable<Optional<String>> selection) {
        if(selection.isLoading()) {
            comboBox.setText(I18N.CONSTANTS.loading());
            comboBox.disable();
        } else {
            String newText = selection.get().orNull();
            if(!Objects.equals(comboBox.getText(), newText)) {
                comboBox.setText(newText);
            }
            updateEnabledView();
        }
    }

    @Override
    public Widget asWidget() {
        return comboBox;
    }

    private void updateEnabledView() {

        LOGGER.info("relevant = " + relevant +
            ", selectedKey.loaded = " + level.getSelectedKey().isLoaded() +
            ", level.enabled = " + (level.isEnabled().isLoaded() && level.isEnabled().get()));

        this.comboBox.setEnabled(relevant && level.getSelectedKey().isLoaded());
    }

    public HandlerRegistration addBlurHandler(BlurEvent.BlurHandler handler) {
        return comboBox.addBlurHandler(handler);
    }

    public void setValue(LabeledReference value) {
        comboBox.setValue(value);
    }

    public void refocus() {
        comboBox.getCell().getFocusElement(comboBox.getElement()).focus();
    }
}
