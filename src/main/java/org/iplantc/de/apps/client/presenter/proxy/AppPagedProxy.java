package org.iplantc.de.apps.client.presenter.proxy;

import java.util.List;

import org.iplantc.de.jsonutil.client.JsonUtil;
import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.de.apps.client.models.autobeans.App;
import org.iplantc.de.apps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.de.apps.client.models.autobeans.AppGroup;
import org.iplantc.de.apps.client.models.autobeans.AppList;
import org.iplantc.de.apps.client.services.AppUserServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

class AppPagedProxy extends RpcProxy<PagingLoadConfig, PagingLoadResult<App>> {
    private final AppUserServiceFacade service = GWT.create(AppUserServiceFacade.class);
    private AppGroup currentAg;

    public AppPagedProxy() {
    }

    @Override
    public void load(PagingLoadConfig loadConfig,
            final AsyncCallback<PagingLoadResult<App>> callback) {

        int limit = loadConfig.getLimit();
        final int offset = loadConfig.getOffset();
        List<? extends SortInfo> sortInfoList = loadConfig.getSortInfo();
        SortInfo sortInfo = null;
        if ((sortInfoList != null) && !sortInfoList.isEmpty()) {
            sortInfo = sortInfoList.get(0);
        }

        String sortField = (sortInfo == null) ? "name" : sortInfo.getSortField();
        service.getPagedApps(currentAg.getId(), limit, sortField, offset, sortInfo.getSortDir(),
                new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);
                        AutoBean<AppList> bean = AutoBeanCodex.decode(factory, AppList.class,
                                result);

                        // Get Total count of paged call
                        int total = bean.as().getApps().size();
                        Number jsonTotal = JsonUtil.getNumber(JsonUtil.getObject(result),
                                "template_count");
                        if (jsonTotal != null) {
                            total = jsonTotal.intValue();
                        }

                        PagingLoadResult<App> callbackResult = new PagingLoadResultBean<App>(
                                bean.as().getApps(), total, offset);
                        callback.onSuccess(callbackResult);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.retrieveAppListingFailed(), caught);
                        callback.onFailure(caught);
                    }
                });
    }

    public void setCurrentAnalysisGroup(AppGroup ag) {
        this.currentAg = ag;
    }

}