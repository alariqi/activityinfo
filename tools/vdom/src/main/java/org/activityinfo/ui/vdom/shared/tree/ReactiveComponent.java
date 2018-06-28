package org.activityinfo.ui.vdom.shared.tree;

import org.activityinfo.observable.Observable;
import org.activityinfo.observable.Observer;
import org.activityinfo.observable.Subscription;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;

import java.util.logging.Logger;

public class ReactiveComponent extends VComponent {

    private static final Logger LOGGER = Logger.getLogger(ReactiveComponent.class.getName());

    private final String debugId;
    private final Observable<VTree> observable;

    private final VTree loading;

    private Subscription subscription;

    public ReactiveComponent(Observable<VTree> observable) {
        this("anonymous", observable);
    }

    public ReactiveComponent(String debugId, Observable<VTree> observable) {
        this(debugId, observable, new VNode(HtmlTag.DIV));
    }

    public ReactiveComponent(String debugId, Observable<VTree> observable, VTree loadingIndicator) {
        this.debugId = debugId + "#" + debugIndex;
        this.observable = observable;
        this.loading = loadingIndicator;
    }

    public ReactiveComponent(Observable<VTree> observable, VTree loadingIndicator) {
        this.observable = observable;
        this.loading = loadingIndicator;
        this.debugId = "anonymous";
    }

    @Override
    protected VTree render() {
        if(observable.isLoaded()) {
            return observable.get();
        } else {
            return loading;
        }
    }

    @Override
    protected void componentDidMount() {

        LOGGER.info("Reactive[" + debugId + "] mounting...");

        subscription = observable.subscribe(new Observer<VTree>() {
            @Override
            public void onChange(Observable<VTree> observable) {
                LOGGER.info("Reactive[" + debugId + "] changed: " +
                        " loaded = " + observable.isLoaded());

                ReactiveComponent.this.refresh();
            }
        });
    }

    @Override
    protected void componentWillUnmount() {

        LOGGER.info("Reactive[" + debugId + "] unmounting...");

        if(subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    @Override
    public String toString() {
        return "Reactive[" + debugId + "]";
    }
}
