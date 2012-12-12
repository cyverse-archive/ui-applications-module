package org.iplantc.core.uiapplications.client.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.events.AppSearchResultLoadEvent;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.data.FilterConfig;
import com.extjs.gxt.ui.client.data.FilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * An RpcProxy for a ListLoader that will call the searchAnalysis service, then process the JSON results
 * into an Analysis list.
 * 
 * @author psarando
 * 
 */
public class AppSearchRpcProxy extends RpcProxy<List<Analysis>> {
    protected String tag;
    protected AppTemplateServiceFacade templateService;
    private String lastQueryText = ""; //$NON-NLS-1$

    public AppSearchRpcProxy(String tag, AppTemplateServiceFacade templateService) {
        this.tag = tag;
        this.templateService = templateService;
    }

    public String getLastQueryText() {
        return lastQueryText;
    }

    @Override
    protected void load(Object loadConfig, final AsyncCallback<List<Analysis>> callback) {
        if (templateService == null) {
            Exception error = new Exception("Could not access service");

            ErrorHandler.post(error);
            callback.onFailure(error);

            return;
        }

        // Get the proxy's search params.
        FilterPagingLoadConfig config = (FilterPagingLoadConfig)loadConfig;

        // Cache the query text.
        lastQueryText = ""; //$NON-NLS-1$

        List<FilterConfig> filterConfigs = config.getFilterConfigs();
        if (filterConfigs != null && !filterConfigs.isEmpty()) {
            lastQueryText = (String)filterConfigs.get(0).getValue();
        }

        if (lastQueryText == null || lastQueryText.isEmpty()) {
            // nothing to search
            return;
        }

        // Cache the search text for this callback; used to sort the results.
        final String searchText = lastQueryText;

        // Create a callback for the AppTemplateServiceFacade.
        AsyncCallback<String> templateServiceCallback = new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<Analysis> analyses = new ArrayList<Analysis>();

                JSONArray templates = JsonUtil.getArray(JsonUtil.getObject(result), "templates"); //$NON-NLS-1$

                if (templates != null) {
                    for (int i = 0; i < templates.size(); i++) {
                        analyses.add(new Analysis(JsonUtil.getObjectAt(templates, i)));
                    }
                }

                Collections.sort(analyses, new Comparator<Analysis>() {
                    @Override
                    public int compare(Analysis app1, Analysis app2) {
                        String app1Name = app1.getName();
                        String app2Name = app2.getName();

                        String lowerSearchText = searchText.toLowerCase();
                        boolean app1NameMatches = app1Name.toLowerCase().contains(lowerSearchText);
                        boolean app2NameMatches = app2Name.toLowerCase().contains(lowerSearchText);

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
                });

                // Pass the Analysis list to this proxy's load callback.
                callback.onSuccess(analyses);

                // Fire the search results load event.
                EventBus eventBus = EventBus.getInstance();
                eventBus.fireEvent(new AppSearchResultLoadEvent(tag, searchText, analyses));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                callback.onFailure(caught);
            }
        };

        // Call the searchAnalysis service with this proxy's query.
        templateService.searchAnalysis(lastQueryText, templateServiceCallback);
    }
}
