package org.iplantc.core.uiapplications.client.presenter.proxy;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisAutoBeanFactory;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisList;
import org.iplantc.core.uiapplications.client.services.AppTemplateUserServiceFacade;
import org.iplantc.core.uiapplications.client.views.AppsView;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.de.client.CommonDisplayStrings;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;

public class AnalysisRpcProxy extends RpcProxy<ListLoadConfig, ListLoadResult<Analysis>> {

    private final AppTemplateUserServiceFacade service;
    private final AppsView view;
    private final CommonDisplayStrings displayStrings;
    private AnalysisGroup currentAg;

    public AnalysisRpcProxy(final AppTemplateUserServiceFacade service, final AppsView view,
            final CommonDisplayStrings displayStrings) {
        this.service = service;
        this.view = view;
        this.displayStrings = displayStrings;
    }

    @Override
    public void load(final ListLoadConfig loadConfig,
            final AsyncCallback<ListLoadResult<Analysis>> callback) {
        view.maskMainPanel(displayStrings.loadingMask());

        service.getAnalysis(currentAg.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                AnalysisAutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);
                AutoBean<AnalysisList> bean = AutoBeanCodex.decode(factory, AnalysisList.class, result);

                ListLoadResultBean<Analysis> result2 = new ListLoadResultBean<Analysis>(bean.as().getAnalyses());
                callback.onSuccess(result2);
                view.unMaskMainPanel();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
                callback.onFailure(caught);
                view.unMaskMainPanel();
            }
        });
    }

    public void setCurrentAnalysisGroup(AnalysisGroup ag) {
        this.currentAg = ag;
    }

}
