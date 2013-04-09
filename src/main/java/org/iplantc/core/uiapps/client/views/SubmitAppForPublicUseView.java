package org.iplantc.core.uiapps.client.views;

import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.TreeStore;

public interface SubmitAppForPublicUseView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {
        void onRequestComplete();

        void onRequestError();
    }

    void setPresenter(Presenter p);

    void onSubmitBtnClick();

    void onCancelBtnClick();

    TreeStore<AppGroup> getTreeStore();

    void expandAppGroups();
}
