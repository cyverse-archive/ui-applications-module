package org.iplantc.core.uiapps.client.presenter;

import java.util.List;

import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.presenter.proxy.PublicAppGroupProxy;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.DEProperties;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;


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
        // Fetch AppGroups
        PublicAppGroupProxy appGroupProxy = new PublicAppGroupProxy();
        appGroupProxy.load(null, new AsyncCallback<List<AppGroup>>() {
            @Override
            public void onSuccess(List<AppGroup> result) {
                addAppGroup(null, result);
                view.expandAppGroups();
                // remove workspace node from store
                view.getTreeStore().remove(
                        view.getTreeStore().findModelWithKey(
                                DEProperties.getInstance()
                        .getDefaultBetaCategoryId()));
            }

            private void addAppGroup(AppGroup parent, List<AppGroup> children) {
                if ((children == null) || children.isEmpty()) {
                    return;
                }
                if (parent == null) {
                    view.getTreeStore().add(children);
                } else {
                    view.getTreeStore().add(parent, children);
                }

                for (AppGroup ag : children) {
                    addAppGroup(ag, ag.getGroups());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
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
