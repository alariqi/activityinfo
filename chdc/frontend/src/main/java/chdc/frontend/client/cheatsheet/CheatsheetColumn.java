package chdc.frontend.client.cheatsheet;

import com.google.common.base.Optional;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ListView;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.SubscriptionSet;
import org.activityinfo.ui.client.lookup.viewModel.LookupKeyViewModel;

import java.util.Collections;
import java.util.List;

public class CheatsheetColumn implements IsWidget {

    private final ListView<String, String> listView;
    private final ListStore<String> store;
    private final LookupKeyViewModel viewModel;
    private final SubscriptionSet subscriptions = new SubscriptionSet();

    public CheatsheetColumn(LookupKeyViewModel viewModel) {
        this.viewModel = viewModel;

        store = new ListStore<>(string -> string);
        ListView.ListViewAppearance<String> appearance = new CheatsheetAppearance();
        listView = new ListView<>(store, new IdentityValueProvider<String>(), appearance);
        listView.addAttachHandler(this::onAttach);
    }

    private void onAttach(AttachEvent event) {
        if(event.isAttached()) {
            subscriptions.add(viewModel.getChoices().subscribe(this::onChoicesChanged));
            subscriptions.add(viewModel.getSelectedKey().subscribe(this::onSelectionChanged));
        }
    }

    private void onChoicesChanged(Observable<List<String>> choices) {
        if(choices.isLoaded()) {
            store.replaceAll(choices.get());
        }
    }


    private void onSelectionChanged(Observable<Optional<String>> selectedKey) {
        if(selectedKey.isLoaded()) {
            Optional<String> selection = selectedKey.get();
            if(selection.isPresent()) {
                listView.getSelectionModel().select(selection.get(), false);
            } else {
                listView.getSelectionModel().setSelection(Collections.emptyList());
            }
        }
    }

    @Override
    public Widget asWidget() {
        return listView;
    }
}
