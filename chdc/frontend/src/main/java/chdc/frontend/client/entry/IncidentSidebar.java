package chdc.frontend.client.entry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.activityinfo.ui.client.input.viewModel.FormInputViewModel;

public class IncidentSidebar extends Composite {


    interface IncidentSidebarUiBinder extends UiBinder<HTMLPanel, IncidentSidebar> {
    }

    private static IncidentSidebarUiBinder ourUiBinder = GWT.create(IncidentSidebarUiBinder.class);

    private final FormInputViewModel viewModel;

    public IncidentSidebar(FormInputViewModel viewModel) {
        this.viewModel = viewModel;
        initWidget(ourUiBinder.createAndBindUi(this));

    }

}