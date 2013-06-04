package org.iplantc.core.uiapps.client.presenter.proxy;

import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.models.autobeans.AppList;
import org.iplantc.core.uiapps.client.services.AppUserServiceFacade;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class AppRpcProxy extends RpcProxy<ListLoadConfig, ListLoadResult<App>> {

    private final AppUserServiceFacade service = GWT.create(AppUserServiceFacade.class);
    private AppGroup currentAg;

    public AppRpcProxy() {
    }

    @Override
    public void load(final ListLoadConfig loadConfig,
            final AsyncCallback<ListLoadResult<App>> callback) {

        service.getApps(currentAg.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);
                AutoBean<AppList> bean = AutoBeanCodex.decode(factory, AppList.class, result);

                ListLoadResultBean<App> result2 = new ListLoadResultBean<App>(bean.as().getApps());
                callback.onSuccess(result2);
            }

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }
        });
    }

    public void setCurrentAnalysisGroup(AppGroup ag) {
        this.currentAg = ag;
    }

}
