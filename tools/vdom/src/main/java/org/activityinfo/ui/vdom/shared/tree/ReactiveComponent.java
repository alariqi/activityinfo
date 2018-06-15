package org.activityinfo.ui.vdom.shared.tree;

import org.activityinfo.observable.Observable;
import org.activityinfo.observable.Observer;
import org.activityinfo.observable.Subscription;
import org.activityinfo.ui.vdom.shared.html.HtmlTag;

public class ReactiveComponent extends VComponent {
    private Observable<VTree> observable;
    private VTree loading = new VNode(HtmlTag.DIV, "Loading...");

    private Subscription subscription;

    public ReactiveComponent(Observable<VTree> observable) {
        this.observable = observable;
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
    protected void componentWillMount() {
        subscription = observable.subscribe(new Observer<VTree>() {
            @Override
            public void onChange(Observable<VTree> observable) {
                ReactiveComponent.this.refresh();
            }
        });
    }

    @Override
    protected void componentWillUnmount() {
        if(subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }
}
