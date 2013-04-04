package org.iplantc.core.uiapps.client.presenter;

import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView;

import com.google.gwt.user.client.ui.HasOneWidget;


public class SubmitAppForPublicPresenter implements
        org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView.Presenter {

    private SubmitAppForPublicUseView view;

    public SubmitAppForPublicPresenter(SubmitAppForPublicUseView view) {
        this.view = view;
    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);

    }

    @Override
    public void onRequestComplete() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestError() {
        // TODO Auto-generated method stub

    }

}
