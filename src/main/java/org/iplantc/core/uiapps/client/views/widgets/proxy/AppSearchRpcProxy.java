package org.iplantc.core.uiapps.client.views.widgets.proxy;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iplantc.core.uiapps.client.Services;
import org.iplantc.core.uiapps.client.events.AppSearchResultLoadEvent;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.autobeans.AppList;
import org.iplantc.core.uiapps.client.services.AppServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.data.FilterConfig;
import com.extjs.gxt.ui.client.data.FilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * An RpcProxy for a ListLoader that will call the searchApp service, then process the JSON results into
 * an App list.
 * 
 * @author psarando
 * 
 */
public class AppSearchRpcProxy extends RpcProxy<List<App>> {
    protected String tag;
    protected AppServiceFacade templateService;
    private String lastQueryText = ""; //$NON-NLS-1$

    public AppSearchRpcProxy(String tag) {
        this.tag = tag;
        this.templateService = Services.APP_SERVICE;
    }

    public String getLastQueryText() {
        return lastQueryText;
    }

    @Override
    protected void load(Object loadConfig, final AsyncCallback<List<App>> callback) {
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
                AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);
                List<App> apps = AutoBeanCodex.decode(factory, AppList.class, result).as().getApps();

                Collections.sort(apps, new Comparator<App>() {
                    @Override
                    public int compare(App app1, App app2) {
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

                // Pass the App list to this proxy's load callback.
                callback.onSuccess(apps);

                // Fire the search results load event.
                EventBus eventBus = EventBus.getInstance();
                eventBus.fireEvent(new AppSearchResultLoadEvent(tag, searchText, apps));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
                callback.onFailure(caught);
            }
        };

        // Call the searchApp service with this proxy's query.
        templateService.searchApp(lastQueryText, templateServiceCallback);
    }
}
