package org.activityinfo.ui.client.importer.view;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.TextAreaElement;
import com.google.gwt.user.client.Event;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.importer.state.ImportSource;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.state.ImportUpdater;
import org.activityinfo.ui.client.importer.viewModel.ImportViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.Optional;

import static org.activityinfo.ui.client.importer.view.ImportView.cancelButton;

/**
 * Renders the "Choose source" step
 */
public class ChooseSourceView {

    public static VTree render(ImportViewModel viewModel, ImportUpdater updater) {
        return ImportView.page(viewModel,
                navigation(viewModel, updater),
                body(viewModel, updater));
    }


    private static VTree navigation(ImportViewModel viewModel, ImportUpdater updater) {
        return H.div(cancelButton(),
                     continueButton(viewModel, updater));
    }

    private static VTree continueButton(ImportViewModel viewModel, ImportUpdater updater) {
        return new ReactiveComponent(viewModel.isSourceValid().transform(source ->
                Buttons.button(I18N.CONSTANTS.continue_())
                        .icon(Icon.BUBBLE_ARROWRIGHT)
                        .primary()
                        .enabled(source)
                        .onSelect(event -> updater.update(s -> s.withStep(ImportState.ImportStep.MATCH_COLUMNS)))
                        .build()));
    }

    private static VTree body(ImportViewModel viewModel, ImportUpdater updater) {
            return H.div("importer",
                    H.div("importer__main",
                            ImportView.heading(viewModel, I18N.CONSTANTS.uploadYourData()),
                            H.div("importer__body importer__source",
                                    pasteTarget(viewModel, updater),
                                    floatingHelp())));
    }


    private static VNode floatingHelp() {
        return H.div("importer__source__buttons",
                H.div(
                        H.h3("Add your data here"),
                        H.p("Paste your copied table in this text field."),
                        H.p("Alternatively, upload a CSV or XLS file."),
                        Buttons.button(I18N.CONSTANTS.upload())
                                .primary()
                                .icon(Icon.BUBBLE_IMPORT)
                                .build()));
    }


    /**
     * Renders a textarea element into which the user can paste a table.
     */
    private static VTree pasteTarget(ImportViewModel viewModel, ImportUpdater updater) {
        Observable<Optional<ImportSource>> sourceText = viewModel
                .getState()
                .transform(s -> s.getSource())
                .cache();

        return new ReactiveComponent(sourceText.transform(s -> {
            PropMap props = Props.create();
            props.set("value", s.map(source -> source.getText()).orElse(""));
            props.oninput(event -> {
                onSourceInput(event, updater);
            });

            return new VNode(HtmlTag.TEXTAREA, props);
        }));

    }

    private static void onSourceInput(Event event, ImportUpdater updater) {
        TextAreaElement textarea = event.getEventTarget().cast();
        if(Strings.isNullOrEmpty(textarea.getValue())) {
            updater.update(s -> s.withSource(Optional.empty()));
        } else {
            updater.update(s -> s.withSource(Optional.of(new ImportSource(textarea.getValue()))));
        }
    }


}
