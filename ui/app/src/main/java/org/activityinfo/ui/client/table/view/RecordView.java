package org.activityinfo.ui.client.table.view;

import com.google.common.base.Optional;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.analysis.table.TableViewModel;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.model.form.RecordHistory;
import org.activityinfo.model.formTree.RecordTree;
import org.activityinfo.observable.Observable;
import org.activityinfo.observable.StatefulValue;
import org.activityinfo.ui.client.store.FormStore;
import org.activityinfo.ui.vdom.client.VDomWidget;
import org.activityinfo.ui.vdom.shared.dom.ReactiveComponent;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;
import org.activityinfo.ui.vdom.shared.tree.PropMap;
import org.activityinfo.ui.vdom.shared.tree.VNode;
import org.activityinfo.ui.vdom.shared.tree.VText;
import org.activityinfo.ui.vdom.shared.tree.VTree;

public class RecordView implements IsWidget {

    private enum Mode {
        DETAILS,
        HISTORY
    }

    private final VDomWidget content;

    public RecordView(FormStore formStore, TableViewModel viewModel) {
        content = new VDomWidget();
        content.addStyleName("details");

        StatefulValue<Mode> mode = new StatefulValue<>(Mode.DETAILS);
        Observable<VTree> selectorTree = mode.transform(m -> selector(mode, m));

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

        content.update(new VNode(HtmlTag.DIV, PropMap.withClasses("details"),
                new ReactiveComponent(selectorTree),
                new ReactiveComponent(contentTree)));
    }


    private VNode selector(StatefulValue<Mode> mode, Mode m) {
        return new VNode(HtmlTag.DIV, PropMap.withClasses("tabstrip"),
                new VNode(HtmlTag.BUTTON, PropMap.withClass("active", m == Mode.DETAILS)
                        .onclick(e -> { mode.updateIfNotSame(Mode.DETAILS); }),
                        new VText(I18N.CONSTANTS.details())),
                new VNode(HtmlTag.BUTTON, PropMap.withClass("active", m == Mode.HISTORY)
                        .onclick(e -> { mode.updateIfNotSame(Mode.HISTORY);}),
                        new VText(I18N.CONSTANTS.history())));
    }


    @Override
    public Widget asWidget() {
        return content;
    }
}
