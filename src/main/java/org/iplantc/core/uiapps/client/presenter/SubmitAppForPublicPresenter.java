package org.iplantc.core.uiapps.client.presenter;

import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;


public class SubmitAppForPublicPresenter implements
        org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView.Presenter {

    private SubmitAppForPublicUseView view;

    @Inject
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
