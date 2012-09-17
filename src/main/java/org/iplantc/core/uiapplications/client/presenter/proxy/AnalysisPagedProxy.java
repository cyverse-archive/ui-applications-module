package org.iplantc.core.uiapplications.client.presenter.proxy;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
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
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

class AnalysisPagedProxy extends RpcProxy<PagingLoadConfig, PagingLoadResult<Analysis>> {
    private final AppTemplateUserServiceFacade service;
    private AnalysisGroup currentAg;

    public AnalysisPagedProxy() {
        this.service = Services.USER_TEMPLATE_SERVICE;
    }

    @Override
    public void load(PagingLoadConfig loadConfig,
            final AsyncCallback<PagingLoadResult<Analysis>> callback) {

        int limit = loadConfig.getLimit();
        final int offset = loadConfig.getOffset();
        List<? extends SortInfo> sortInfoList = loadConfig.getSortInfo();
        SortInfo sortInfo = null;
        if ((sortInfoList != null) && !sortInfoList.isEmpty()) {
            sortInfo = sortInfoList.get(0);
        }
        // TODO JDS - This type conversion won't be necessary if we can change the type of SortDir in
        // TemplateServiceFacade.getPagedAnalysis(...)
        com.extjs.gxt.ui.client.Style.SortDir sortDir = ((sortInfo != null) && sortInfo.getSortDir()
                .equals(SortDir.ASC)) ? com.extjs.gxt.ui.client.Style.SortDir.ASC
                : com.extjs.gxt.ui.client.Style.SortDir.DESC;

        String sortField = (sortInfo == null) ? "name" : sortInfo.getSortField();
        service.getPagedAnalysis(currentAg.getId(), limit, sortField, offset, sortDir,
                new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        AnalysisAutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);
                        AutoBean<AnalysisList> bean = AutoBeanCodex.decode(factory, AnalysisList.class,
                                result);

                        // Get Total count of paged call
                        int total = bean.as().getAnalyses().size();
                        Number jsonTotal = JsonUtil.getNumber(JsonUtil.getObject(result),
                                "template_count");
                        if (jsonTotal != null) {
                            total = jsonTotal.intValue();
                        }

                        PagingLoadResult<Analysis> callbackResult = new PagingLoadResultBean<Analysis>(
                                bean.as().getAnalyses(), total, offset);
                        callback.onSuccess(callbackResult);
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