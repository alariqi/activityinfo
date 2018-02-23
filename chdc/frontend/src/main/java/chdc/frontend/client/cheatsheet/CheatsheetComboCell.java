package chdc.frontend.client.cheatsheet;

import com.google.common.collect.Iterables;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.ui.client.lookup.viewModel.KeyMatrix;
import org.activityinfo.ui.client.lookup.viewModel.KeyMatrixColumnSet;
import org.activityinfo.ui.client.lookup.viewModel.LookupViewModel;
import org.activityinfo.ui.client.table.view.LabeledRecordRef;

import java.text.ParseException;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Variant of a ComboBox cell that has a trigger which opens a cheatsheet popup.
 *
 */
public class CheatsheetComboCell extends ComboBoxCell<LabeledRecordRef> {

    private static final Logger LOGGER = Logger.getLogger(CheatsheetComboCell.class.getName());

    private final CheatsheetPanel cheatsheet;
    private final LookupViewModel viewModel;

    private final SlideoutPanel slideOutPanel;


    private final BaseEventPreview eventPreview;
    private final KeyMatrix keyMatrix;

    private SearchFunction searchFunction = new SearchContains();

    private LabeledRecordRef lastSelectionFromCheatsheet;

    public CheatsheetComboCell(LookupViewModel viewModel, String fieldLabel) {
        super(new ListStore<>(ref -> ref.getRecordId()), key -> key.getLabel(), new HelperTriggerAppearance());

        this.viewModel = viewModel;
        keyMatrix = Iterables.getOnlyElement(this.viewModel.getKeyMatrixSet().getMatrices());

        cheatsheet = new CheatsheetPanel(viewModel);
        cheatsheet.getLeafColumn().addSelectionHandler(this::onLeafKeySelected);

        slideOutPanel = new SlideoutPanel();
        slideOutPanel.setTitleHeading(I18N.MESSAGES.findFieldValue(fieldLabel));
        slideOutPanel.add(cheatsheet);
        slideOutPanel.attach();

        viewModel.getSelectedLabeledRecord().subscribe(observable -> {
            if(observable.isLoaded() && observable.get().isPresent()) {
                lastSelectionFromCheatsheet = observable.get().get();
            }
        });

        setPropertyEditor(new PropertyEditor<LabeledRecordRef>() {
            @Override
            public LabeledRecordRef parse(CharSequence text) throws ParseException {
                // This is a workaround for the way the TriggerCell is implemented,
                // which assumes that we can map directly from text to a LabeledValue

                // Instead, when we are called by TriggerField.finishEditing(), we want
                // to return the last values selected either by searching or from cheatsheet

                if(text == null || text.length() == 0) {
                    return null;
                }

                if (lastSelectionFromCheatsheet != null &&
                        text.equals(lastSelectionFromCheatsheet.getLabel())) {
                    return lastSelectionFromCheatsheet;
                }

                return getByValue(text.toString());
            }

            @Override
            public String render(LabeledRecordRef object) {
                return object.getLabel();
            }
        });


        // Show drop downlist 150 ms after the user starts typing.
        setTypeAheadDelay(150);


        eventPreview = new BaseEventPreview() {
            protected boolean onPreview(Event.NativePreviewEvent pe) {
                Element target = pe.getNativeEvent().getEventTarget().cast();

                // pointer event MOUSEDOWN conflicts with scrolling
// TODO: what does this do?
//                if (PointerEventsSupport.impl.isSupported()) {
//                    previewTapGestureRecognizer.handle(pe.getNativeEvent());
//                    return true;
//                }

                switch (pe.getTypeInt()) {
                    case Event.ONSCROLL:
                    case Event.ONMOUSEWHEEL:
                        collapseCheatsheetIf(pe);
                        break;
                    case Event.ONMOUSEDOWN:
                        if (slideOutPanel.isOrHasChild(target)) {
                            // TODO: handle selection change
                        } else {
                            collapseCheatsheetIf(pe);
                        }
                        break;
//                    case Event.ONTOUCHSTART:
//                    case Event.ONTOUCHMOVE:
//                    case Event.ONTOUCHEND:
//                    case Event.ONTOUCHCANCEL:
//                        previewTapGestureRecognizer.handle(pe.getNativeEvent());
//                        break;
                }

                NativeEvent e = pe.getNativeEvent();

                if (pe.getTypeInt() == KeyNav.getKeyEvent() && slideOutPanel.isVisible()) {
                    if (e.getKeyCode() == KeyCodes.KEY_ENTER) {
                        e.stopPropagation();
                        e.preventDefault();
//
                        // TODO: enter?
//                        if (GXT.isIE()) {
//                            ignoreNextEnter = true;
//                        }
//
//                        onViewClick(lastParent, e, false, true);
                    }
                }
                return true;
            }
        };
        eventPreview.setAutoHide(false);

    }


    @Override
    public void doQuery(Context context, XElement parent, ValueUpdater<LabeledRecordRef> updater, LabeledRecordRef value, String query, boolean force) {


        if (query != null && query.length() >= getMinChars()) {

            keyMatrix.getKeyMatrixColumnSet().once().then(new AsyncCallback<KeyMatrixColumnSet>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(KeyMatrixColumnSet result) {
                    store.replaceAll(searchFunction.search(viewModel.getLookupKeySet(), result, query));
                    onResultsLoad(context, parent, updater, value);
                }
            });
        }
    }

    @Override
    public void finishEditing(Element parent, LabeledRecordRef value, Object key, ValueUpdater<LabeledRecordRef> valueUpdater) {
        super.finishEditing(parent, value, key, valueUpdater);
    }

    @Override
    protected void onTriggerClick(Context context, XElement parent, NativeEvent event, LabeledRecordRef value, ValueUpdater<LabeledRecordRef> updater) {
        if(slideOutPanel.isVisible()) {
            collapseCheatsheet(context, parent);
        } else {
            expandCheatsheet(context, parent, updater, value);
        }
    }

    public void expandCheatsheet(final Context context, final XElement parent, final ValueUpdater<LabeledRecordRef> updater, final LabeledRecordRef value) {
        if (slideOutPanel.isVisible()) {
            return;
        }

        // If the drop down is open with search results, then hide it
        if(isExpanded()) {
            collapse(context, parent);
        }

        // expand may be called without the cell being focused
        // saveContext sets focusedCell so we clear if cell
        // not currently focused
        boolean focused = focusedCell != null;
        saveContext(context, parent, null, updater, value);
        if (!focused) {
            focusedCell = null;
        }

        // Update the cheatsheet's model
        if(value == null) {
            viewModel.setInitialSelection(Collections.emptySet());
        } else {
            viewModel.setInitialSelection(Collections.singleton(value.getRecordRef()));
        }
        slideOutPanel.show();

        eventPreview.add();
    }

    private void collapseCheatsheetIf(Event.NativePreviewEvent pe) {
        collapseCheatsheetIf(pe.getNativeEvent());
    }

    private void collapseCheatsheetIf(NativeEvent event) {
        XElement target = event.getEventTarget().cast();
        collapseCheatsheetIf(target);
    }

    private void collapseCheatsheetIf(XElement target) {

        if (lastParent == null || (!slideOutPanel.isOrHasChild(target) && !lastParent.isOrHasChild(target))) {
            collapseCheatsheet(lastContext, lastParent);
        }
    }

    private void collapseCheatsheet(Context context, final XElement parent) {
        if (!slideOutPanel.isVisible()) {
            return;
        }

        eventPreview.remove();

        slideOutPanel.hide();

    }


    private void onLeafKeySelected(SelectionEvent<String> event) {

        // Update the text displayed in the combobox
        getInputElement(lastParent).setValue(event.getSelectedItem());

        // Close the cheatsheet
        collapseCheatsheet(lastContext, lastParent);
    }


    /**
     * This method is called by the focus manager to determine whether this field still
     * has "logical" focus. This may be different than the input element actually having focus --
     * for example, if the drop down list or the slide up panel have the focus, we don't want
     * to blur this field.
     */
    @Override
    protected boolean isFocusedWithTarget(Element parent, Element target) {

        boolean superResult = super.isFocusedWithTarget(parent, target);
        if(superResult) {
            return true;
        }

        if (slideOutPanel.isVisible()) {
            return true;
        }

        return false;
    }
}
