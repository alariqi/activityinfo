package org.activityinfo.ui.client.table.view;

import com.google.common.base.Optional;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.client.base.avatar.GenericAvatar;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.base.toolbar.ToolbarBuilder;
import org.activityinfo.ui.client.fields.view.FieldChoiceView;
import org.activityinfo.ui.client.input.model.FormInputModel;
import org.activityinfo.ui.client.input.view.FormOverlay;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.model.SliderUpdater;
import org.activityinfo.ui.client.table.model.TableUpdater;
import org.activityinfo.ui.client.table.viewModel.TableSliderViewModel;
import org.activityinfo.ui.client.table.viewModel.TableViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TableView {

    private static final Logger LOGGER = Logger.getLogger(TableView.class.getName());

    public static VTree render(FormStore formStore, Observable<Maybe<TableSliderViewModel>> viewModel, SliderUpdater updater) {

        return new ReactiveComponent("tableview.page", viewModel.transform(vm ->
            vm.switch_(new Maybe.Case<TableSliderViewModel, VTree>() {
                @Override
                public VTree visible(TableSliderViewModel value) {
                    return render(formStore, value, updater);
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

    private static VTree render(FormStore formStore, TableSliderViewModel viewModel, SliderUpdater updater) {

        return new PageBuilder()
                .avatar(GenericAvatar.DATABASE)
                .heading(viewModel.getPageTitle())
                .breadcrumbs(viewModel.getBreadcrumbs())
                .body(body(formStore, viewModel, updater))
                .build();
    }

    private static VTree body(FormStore formStore, TableSliderViewModel viewModel, SliderUpdater updater) {

        return H.div("formtable",
                slider(viewModel, updater),
                editForm(formStore, viewModel, updater));
    }

    private static VTree slider(TableSliderViewModel viewModel, SliderUpdater updater) {

        // Make sure we re-use table components to avoid having to create/tear down the grid widgets
        // too often
        List<VTree> slides = slides(viewModel, updater);

        // This renders the child of page__body, which uses flexbox to size it's only child
        // to it size.

        // We will create a single outer "slider" element which will be absolutely positioned based
        // on its depth in the form tree

        return new ReactiveComponent("slider", viewModel.getSliderPosition().transform(pos -> {

            Style sliderStyle = new Style();
            sliderStyle.set("width", (viewModel.getSlideCount() * 100) + "vw");
            sliderStyle.set("transform", "translateX(-" + (pos.getSlideIndex() * 100) + "vw)");

            PropMap sliderProps = Props.create();
            sliderProps.setStyle(sliderStyle);
            sliderProps.setClass("formtable__slider");

            return new VNode(HtmlTag.DIV, sliderProps, slides);

        }));
    }

    private static List<VTree> slides(TableSliderViewModel viewModel, SliderUpdater sliderUpdater) {
        List<VTree> slides = new ArrayList<>();
        for (TableViewModel table : viewModel.getTables()) {

            Style slideStyle = new Style();
            slideStyle.set("left", (table.getSlideIndex() * 100) + "vw");

            PropMap slideProps = Props.create();
            slideProps.setClass("formtable__slide");
            slideProps.setStyle(slideStyle);

            TableUpdater updater = sliderUpdater.getTableUpdater(table.getFormId());

            slides.add(new VNode(HtmlTag.DIV, slideProps,
                    toolbar(table, updater),
                    grid(table, updater),
                    RecordSidePanel.render(table, updater),
                    FieldChoiceView.render(table.getColumnOptions(), updater.fieldChoiceUpdater())));
        }

        return slides;
    }

    private static VTree toolbar(TableViewModel tableViewModel, TableUpdater updater) {

        VTree newButton = Buttons.button(I18N.CONSTANTS.newRecord())
                .primary()
                .icon(Icon.BUBBLE_ADD)
                .onSelect(event -> updater.newRecord())
                .build();

        VTree importButton = Buttons.button(I18N.CONSTANTS.importText())
                .icon(Icon.BUBBLE_IMPORT)
                .build();

        VTree columnsButton = Buttons.button(I18N.CONSTANTS.chooseColumns())
                .icon(Icon.BUBBLE_COLUMNS)
                .onSelect(event -> updater.showColumnOptions(true))
                .build();

        VTree exportButton = Buttons.button(I18N.CONSTANTS.export())
                .icon(Icon.BUBBLE_EXPORT)
                .build();

        VTree fullscreenButton = Buttons.button(I18N.CONSTANTS.fullscreen())
                .icon(Icon.BUBBLE_FULLSCREEN)
                .build();

        return new ToolbarBuilder()
                .group(newButton, importButton, exportButton)
                .group(columnsButton, fullscreenButton)
                .build();
    }



    private static VTree editForm(FormStore formStore, TableSliderViewModel table, SliderUpdater updater) {

        return new ReactiveComponent(table.isInputVisible().transform(visible -> {
            if(visible) {
                Observable<FormInputModel> inputModel = table.getInputModel().transformIf(x -> Optional.fromJavaUtil(x));

                return new FormOverlay(formStore, inputModel, updater.getInputHandler());

            } else {
                return H.div("forminput", VNode.NO_CHILDREN);
            }
        }));
    }

    private static VTree grid(TableViewModel viewModel, TableUpdater updater) {
        return new GridContainer(viewModel, updater);
    }
}