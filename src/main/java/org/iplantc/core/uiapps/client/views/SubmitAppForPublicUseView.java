package org.iplantc.core.uiapps.client.views;

import java.util.List;

import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.models.autobeans.AppRefLink;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.TreeStore;

public interface SubmitAppForPublicUseView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {
        void onSubmit();
    }

    void setPresenter(Presenter p);

    TreeStore<AppGroup> getTreeStore();

    void expandAppGroups();

    JSONObject toJson();

    App getSelectedApp();

    boolean validate();

    public void loadReferences(List<AppRefLink> refs);
}
