package org.iplantc.core.uiapplications.client.presenter.proxy;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisAutoBeanFactory;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisList;
import org.iplantc.core.uiapplications.client.services.AppTemplateUserServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class AnalysisRpcProxy extends RpcProxy<ListLoadConfig, ListLoadResult<Analysis>> {

    private final AppTemplateUserServiceFacade service;
    private AnalysisGroup currentAg;

    public AnalysisRpcProxy() {
        this.service = Services.USER_TEMPLATE_SERVICE;
    }

    @Override
    public void load(final ListLoadConfig loadConfig,
            final AsyncCallback<ListLoadResult<Analysis>> callback) {

        service.getAnalysis(currentAg.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                AnalysisAutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);
                AutoBean<AnalysisList> bean = AutoBeanCodex.decode(factory, AnalysisList.class, result);

                ListLoadResultBean<Analysis> result2 = new ListLoadResultBean<Analysis>(bean.as().getAnalyses());
                callback.onSuccess(result2);
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
                callback.onFailure(caught);
            }
        });
    }

    public void setCurrentAnalysisGroup(AnalysisGroup ag) {
        this.currentAg = ag;
    }

}
