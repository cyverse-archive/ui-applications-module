package org.iplantc.core.uiapplications.client.views.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AppInfoView extends Composite {

    private static AppInfoViewUiBinder uiBinder = GWT.create(AppInfoViewUiBinder.class);

    interface AppInfoViewUiBinder extends UiBinder<Widget, AppInfoView> {
    }

    public AppInfoView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public AppInfoView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
