package org.activityinfo.ui.client.table.view;

import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.Icon;
import org.activityinfo.ui.client.base.NonIdeal;
import org.activityinfo.ui.client.base.avatar.GenericAvatar;
import org.activityinfo.ui.client.base.button.Buttons;
import org.activityinfo.ui.client.base.toolbar.ToolbarBuilder;
import org.activityinfo.ui.client.page.PageBuilder;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.client.table.TablePlace;
import org.activityinfo.ui.client.table.model.TableModelStore;
import org.activityinfo.ui.client.table.model.TableUpdater;
import org.activityinfo.ui.client.table.viewModel.TableSliderViewModel;
import org.activityinfo.ui.client.table.viewModel.TableViewModel;
import org.activityinfo.ui.vdom.shared.html.H;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.*;

import java.util.ArrayList;
import java.util.List;

public class TableView {

    public static VTree render(FormStore formStore, TablePlace place) {

        Observable<Maybe<TableSliderViewModel>> viewModel =
                TableSliderViewModel.compute(formStore, TableModelStore.STORE, place);

        return new ReactiveComponent(viewModel.transform(vm ->
            vm.switch_(new Maybe.Case<TableSliderViewModel, VTree>() {
                @Override
                public VTree visible(TableSliderViewModel value) {
                    return render(formStore, value);
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

    private static VTree render(FormStore formStore, TableSliderViewModel viewModel) {

        return new PageBuilder()
                .avatar(GenericAvatar.DATABASE)
                .heading(viewModel.getPageTitle())
                .breadcrumbs(viewModel.getBreadcrumbs())
                .body(body(formStore, viewModel))
                .build();
    }



    private static VTree body(FormStore formStore, TableSliderViewModel viewModel) {
        // This renders the child of page__body, which uses flexbox to size it's only child
        // to it size.

        // We will create a single outer "slider" element which will be absolutely positioned based
        // on its depth in the form tree

        Style sliderStyle = new Style();
        sliderStyle.set("width", (viewModel.getSlideCount() * 100) + "vw");
        sliderStyle.set("transform", "translateX(-" + (viewModel.getSlideIndex() * 100) + "vw)");

        PropMap sliderProps = new PropMap();
        sliderProps.setStyle(sliderStyle);
        sliderProps.setClass("formtable__slider");

        return H.div("formtable",
                new VNode(HtmlTag.DIV, sliderProps, slides(viewModel)));
    }

    private static List<VTree> slides(TableSliderViewModel viewModel) {
        List<VTree> slides = new ArrayList<>();
        for (TableViewModel table : viewModel.getTables()) {

            Style slideStyle = new Style();
            slideStyle.set("left", (table.getDepth() * 100) + "vw");

            PropMap slideProps = new PropMap();
            slideProps.setClass("formtable__slide");
            slideProps.setStyle(slideStyle);

            TableUpdater updater = TableModelStore.STORE.getTableUpdater(table.getFormId());

            slides.add(new VNode(HtmlTag.DIV, slideProps,
                    toolbar(table),
                    grid(table, updater),
                    RecordSidePanel.render(table, updater)));
        }

        return slides;
    }


    private static VTree toolbar(TableViewModel tableViewModel) {

        VTree newButton = Buttons.button(I18N.CONSTANTS.newRecord())
                .primary()
                .icon(Icon.BUBBLE_ADD)
                .build();

        VTree importButton = Buttons.button(I18N.CONSTANTS.importText())
                .icon(Icon.BUBBLE_IMPORT)
                .build();

        VTree columnsButton = Buttons.button(I18N.CONSTANTS.chooseColumns())
                .icon(Icon.BUBBLE_COLUMNS)
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
    private static VTree grid(TableViewModel viewModel, TableUpdater updater) {
        return new GridContainer(viewModel, updater);
    }

}