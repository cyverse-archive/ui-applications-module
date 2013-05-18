package org.iplantc.core.uiapps.client.presenter.proxy;

import java.util.List;

import org.iplantc.core.uiapps.client.Services;
import org.iplantc.core.uiapps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;

public class PublicAppGroupProxy extends RpcProxy<AppGroup, List<AppGroup>> {

    private final AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);

    public PublicAppGroupProxy() {
    }

    @Override
    public void load(AppGroup loadConfig, final AsyncCallback<List<AppGroup>> callback) {
        Services.APP_SERVICE.getAppGroups("-1",
                new AppGroupAsyncCallback(factory, callback));
    }
}
