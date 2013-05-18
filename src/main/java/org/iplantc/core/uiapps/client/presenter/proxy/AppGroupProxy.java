package org.iplantc.core.uiapps.client.presenter.proxy;

import java.util.List;

import org.iplantc.core.uiapps.client.Services;
import org.iplantc.core.uiapps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uicommons.client.models.UserInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;

/**
 * @author jstroot
 *
 */
public class AppGroupProxy extends RpcProxy<AppGroup, List<AppGroup>> {

    private final AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);

    public AppGroupProxy() {
    }

    @Override
    public void load(AppGroup loadConfig, final AsyncCallback<List<AppGroup>> callback) {
        Services.APP_SERVICE.getAppGroups(UserInfo.getInstance().getWorkspaceId(),
                new AppGroupAsyncCallback(factory, callback));
    }

}
