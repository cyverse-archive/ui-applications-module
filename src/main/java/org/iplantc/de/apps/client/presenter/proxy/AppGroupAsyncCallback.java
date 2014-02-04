package org.iplantc.de.apps.client.presenter.proxy;

import java.util.List;

import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.de.apps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.de.apps.client.models.autobeans.AppGroup;
import org.iplantc.de.apps.client.models.autobeans.AppGroupList;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class AppGroupAsyncCallback implements AsyncCallback<String> {

    private AsyncCallback<List<AppGroup>> callback;
    private AppAutoBeanFactory factory;

    public AppGroupAsyncCallback(AppAutoBeanFactory factory, AsyncCallback<List<AppGroup>> callback) {
        this.callback = callback;
        this.factory = factory;
    }

    @Override
    public void onSuccess(String result) {
        AutoBean<AppGroupList> bean = AutoBeanCodex.decode(factory, AppGroupList.class, result);
        AppGroupList as = bean.as();
        List<AppGroup> groups = as.getGroups();
        callback.onSuccess(groups);
    }

    @Override
    public void onFailure(Throwable caught) {
        ErrorHandler.post(I18N.ERROR.analysisGroupsLoadFailure(), caught);
        callback.onFailure(caught);
    }

}
