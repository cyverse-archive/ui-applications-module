package org.iplantc.core.uiapplications.client.views.widgets;

import org.iplantc.core.uiapplications.client.models.autobeans.App;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class AppInfoView implements IsWidget {

    interface AppInfoViewUiBinder extends UiBinder<Widget, AppInfoView> {
    }

    private static AppInfoViewUiBinder BINDER = GWT.create(AppInfoViewUiBinder.class);

    @UiField
    AppFavoriteCellWidget favIcon;

    private final Widget widget;

    public AppInfoView(App app) {
        widget = BINDER.createAndBindUi(this);
        favIcon.setValue(app);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }


}
