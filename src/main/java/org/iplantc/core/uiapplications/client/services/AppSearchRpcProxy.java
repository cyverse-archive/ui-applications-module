package org.iplantc.core.uiapplications.client.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.extjs.gxt.ui.client.data.BaseListLoadConfig;
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
        BaseListLoadConfig config = (BaseListLoadConfig)loadConfig;

        // Cache the query text.
        lastQueryText = (String)config.get("query"); //$NON-NLS-1$

        // Cache the search text for this callback; used to sort the results.
        final String searchTerm = lastQueryText.toLowerCase();

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

                        boolean app1NameMatches = app1Name.toLowerCase().contains(searchTerm);
                        boolean app2NameMatches = app2Name.toLowerCase().contains(searchTerm);

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
