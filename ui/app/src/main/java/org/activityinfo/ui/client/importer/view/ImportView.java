package org.activityinfo.ui.client.importer.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.client.base.avatar.GenericAvatar;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.importer.state.ImportState;
import org.activityinfo.ui.client.importer.state.ImportUpdater;
import org.activityinfo.ui.client.importer.viewModel.ImportViewModel;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.tree.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImportView {

    public static VTree render(Observable<Maybe<ImportViewModel>> viewModel, ImportUpdater updater) {

        return new ReactiveComponent(viewModel.transform(vm ->
                vm.switch_(new Maybe.Case<ImportViewModel, VTree>() {
                    @Override
                    public VTree visible(ImportViewModel value) {
                        return render(value, updater);
                    }

                    @Override
                    public VTree forbidden() {
                        return NonIdeal.forbidden();
                    }

                    @Override
                    public VTree deleted() {
                        return NonIdeal.notFound();
                    }

                    @Override
                    public VTree notFound() {
                        return NonIdeal.notFound();
                    }
                })));
    }

    private static VTree render(ImportViewModel viewModel, ImportUpdater updater) {
        return new ReactiveComponent(viewModel.getCurrentStep().cache().transform(step -> {
            switch (step) {
                default:
                case CHOOSE_SOURCE:
                    return ChooseSourceView.render(viewModel, updater);
                case MATCH_COLUMNS:
                    return MatchColumnView.render(viewModel, updater);
                case REVIEW_INVALID:
                    return ReviewInvalidView.render(viewModel, updater);
            }
        }));
    }


    /**
     * Renders the page structure common to all import steps.
     */
    static VTree page(ImportViewModel viewModel, VTree navigation, VTree body) {
        return new PageBuilder()
                .avatar(GenericAvatar.DATABASE)
                .heading(I18N.CONSTANTS.importDataExistingForm())
                .breadcrumbs(viewModel.getBreadcrumbs())
                .headerAction(navigation)
                .body(body)
                .build();
    }


    static VNode heading(ImportViewModel viewModel, String heading, VTree... content) {

        List<VTree> children = new ArrayList<>();
        children.add(H.div("surtitle", new VText(viewModel.getFormLabel())));
        children.add(H.h2(heading));
        children.addAll(Arrays.asList(content));

        return H.div("importer__header",
                H.div("importer__header__inner",
                        children.stream()));
    }

    static VTree cancelButton() {
        return Buttons.button(I18N.CONSTANTS.cancel())
                .icon(Icon.BUBBLE_CLOSE)
                .build();
    }

    static VTree backButton(ImportState.ImportStep previousStep, ImportUpdater updater) {
        return Buttons.button(I18N.CONSTANTS.back())
                .icon(Icon.BUBBLE_ARROWLEFT)
                .onSelect(event -> updater.update(s -> s.withStep(previousStep)))
                .build();
    }

    static VTree doneButton() {
        return Buttons.button(I18N.CONSTANTS.done())
                .icon(Icon.BUBBLE_CHECKMARK)
                .primary()
                .build();
    }


}
