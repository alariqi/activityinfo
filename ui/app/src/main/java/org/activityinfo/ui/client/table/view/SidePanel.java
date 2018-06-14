/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.ui.client.table.view;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.RecordHistory;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.Observer;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.base.button.CloseButton;
import org.activityinfo.ui.client.base.button.PlainTextButton;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.client.VDomWidget;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

/**
 * Sidebar panel containing details, history, etc.
 */
public class SidePanel implements IsWidget {

    private enum Mode {
        DETAILS,
        HISTORY
    }

    private final CssLayoutContainer container;
    private final VDomWidget content;

    private boolean collapsed = true;

    public SidePanel(FormStore formStore, TableViewModel viewModel) {

        PlainTextButton expandButton = new PlainTextButton("Details & History ▲");
        expandButton.addStyleName("sidepanel__expand");
        expandButton.addSelectHandler(e -> expandPanel());

        PlainTextButton scrollButton = new PlainTextButton("Scroll to this record...");
        scrollButton.addStyleName("sidepanel__scrollto");

        CloseButton collapseButton = new CloseButton();
        collapseButton.addStyleName("sidepanel__collapse");
        collapseButton.addSelectHandler(e -> collapsePanel());

        content = new VDomWidget();
        content.addStyleName("details");

        container = new CssLayoutContainer("aside");
        container.addStyleName("sidepanel");
        container.addStyleName("sidepanel--collapsed");
        container.add(expandButton);
        container.add(scrollButton);
        container.add(collapseButton);
        container.add(content);

        StatefulValue<Mode> mode = new StatefulValue<>(Mode.DETAILS);
        Observable<VTree> selectorTree = mode.transform(this::selector);

        Observable<DetailsRenderer> renderer = viewModel.getFormTree().transform(DetailsRenderer::new);
        Observable<Optional<RecordTree>> selection = viewModel.getSelectedRecordTree();
        Observable<Optional<RecordHistory>> history = viewModel.getSelectedRecordRef().join(ref -> {
            if (ref.isPresent()) {
                return formStore.getFormRecordHistory(ref.get()).transform(h -> Optional.of(h));
            } else {
                return Observable.just(Optional.absent());
            }
        });

        Observable<VTree> detailsTree = Observable.transform(renderer, selection, (r, s) -> r.render(s));
        Observable<VTree> historyTree = history.transform(HistoryRenderer::render);
        Observable<VTree> contentTree = mode.join(m -> {
            switch (m) {
                case HISTORY:
                    return historyTree;
                default:
                case DETAILS:
                    return detailsTree;
            }
        });

        Observable<VTree> tree = Observable.transform(selectorTree, contentTree, (s, c) ->
                new VNode(HtmlTag.DIV, PropMap.withClasses("details"), s, c));


        tree.subscribe(new Observer<VTree>() {
            @Override
            public void onChange(Observable<VTree> observable) {
                if(observable.isLoading()) {
                    content.addStyleName("details--loading");
                } else {
                    content.removeStyleName("details--loading");
                    content.update(observable.get());
                }
            }
        });
    }

    private VNode selector(Mode m) {
        return new VNode(HtmlTag.DIV, PropMap.withClasses("tabstrip"),
                new VNode(HtmlTag.BUTTON, PropMap.withClass("active", m == Mode.DETAILS),
                        new VText(I18N.CONSTANTS.details())),
                new VNode(HtmlTag.BUTTON, PropMap.withClass("active", m == Mode.HISTORY),
                        new VText(I18N.CONSTANTS.history())));
    }

    public void expandPanel() {
        if(collapsed) {
            container.removeStyleName("sidepanel--collapsed");
            collapsed = false;
        }
    }

    private void collapsePanel() {
        container.addStyleName("sidepanel--collapsed");
        collapsed = true;
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
