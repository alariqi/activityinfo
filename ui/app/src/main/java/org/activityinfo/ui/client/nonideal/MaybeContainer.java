package org.activityinfo.ui.client.nonideal;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.activityinfo.i18n.shared.I18N;
import org.activityinfo.observable.Observable;
import org.activityinfo.promise.Maybe;
import org.activityinfo.ui.client.base.container.CssLayoutContainer;
import org.activityinfo.ui.client.base.container.StaticHtml;

public class MaybeContainer<T> implements IsWidget {

    private enum State {
        LOADING,
        NOT_FOUND,
        FORBIDDEN,
        LOADED
    }

    private CssLayoutContainer container;
    private ViewWidget<T> view;
    private StaticHtml loading;
    private StaticHtml notFound;
    private StaticHtml forbidden;

    private State state;

    public MaybeContainer(ViewWidget<T> view) {
        this.view = view;
        this.loading = new StaticHtml(NonIdealTemplates.TEMPLATES.loading());
        this.notFound = new StaticHtml(NonIdealTemplates.TEMPLATES.notFound(I18N.CONSTANTS));
        this.forbidden = new StaticHtml(NonIdealTemplates.TEMPLATES.forbidden(I18N.CONSTANTS));

        container = new CssLayoutContainer();
        container.add(loading);
        container.add(notFound);
        container.add(forbidden);
        container.add(view);

        updateState(State.LOADING);
    }

    public void updateView(Observable<Maybe<T>> viewModel) {
        if(viewModel.isLoading()) {
            // Only show the loading state if we are transitioning from
            // the
            if(state != State.LOADED) {
                updateState(State.LOADING);
            }
        } else {
            Maybe<T> maybe = viewModel.get();
            switch (maybe.getState()) {
                case VISIBLE:
                    view.updateView(maybe.get());
                    updateState(State.LOADED);
                    break;
                case FORBIDDEN:
                    updateState(State.FORBIDDEN);
                    break;
                case DELETED:
                case NOT_FOUND:
                    updateState(State.NOT_FOUND);
                    break;
            }
        }
    }

    private void updateState(State newState) {
        if(this.state != newState) {
            loading.setVisible(newState == State.LOADING);
            notFound.setVisible(newState == State.NOT_FOUND);
            forbidden.setVisible(newState == State.FORBIDDEN);
            view.setVisible(newState == State.LOADED);
            this.state = newState;
        }
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
