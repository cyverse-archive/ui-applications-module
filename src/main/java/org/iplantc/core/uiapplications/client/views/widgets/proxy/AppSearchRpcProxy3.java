package org.iplantc.core.uiapplications.client.views.widgets.proxy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisAutoBeanFactory;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisList;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;

public class AppSearchRpcProxy3 extends RpcProxy<AnalysisLoadConfig, AnalysisListLoadResult> {

    private final AppTemplateServiceFacade templateService;
    private String lastQueryText;

    public AppSearchRpcProxy3() {
        this.templateService = Services.TEMPLATE_SERVICE;
    }

    @Override
    public void load(final AnalysisLoadConfig loadConfig,
            final AsyncCallback<AnalysisListLoadResult> callback) {
        lastQueryText = loadConfig.getQuery();
        templateService.searchAnalysis(lastQueryText.toLowerCase(), new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(String result) {
                AnalysisAutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);
                AutoBean<AnalysisList> bean = AutoBeanCodex.decode(factory, AnalysisList.class, result);

                List<Analysis> analyses = bean.as().getAnalyses();

                Collections.sort(analyses, new AnalysisComparator());
                
                AnalysisListLoadResult searchResult = AppSearchAutoBeanFactory.instance.dataLoadResult()
                        .as();
                searchResult.setData(analyses);
                callback.onSuccess(searchResult);
            }
        });
    }

    public String getLastQueryText() {
        return lastQueryText;
    }

    private final class AnalysisComparator implements Comparator<Analysis> {
        @Override
        public int compare(Analysis app1, Analysis app2) {
            String app1Name = app1.getName();
            String app2Name = app2.getName();

            boolean app1NameMatches = app1Name.toLowerCase().contains(getLastQueryText().toLowerCase());
            boolean app2NameMatches = app2Name.toLowerCase().contains(getLastQueryText().toLowerCase());

            if (app1NameMatches && !app2NameMatches) {
                // Only app1's name contains the search term, so order it before app2
                return -1;
            }
            if (!app1NameMatches && app2NameMatches) {
                // Only app2's name contains the search term, so order it before app1
                return 1;
            }

            return app1Name.compareToIgnoreCase(app2Name);
        }
    }
}
