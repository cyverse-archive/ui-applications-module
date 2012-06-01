package org.iplantc.core.uiapplications.client.views.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.events.AppSearchResultSelectedEvent;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseListLoadConfig;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.LoadListener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ListModelPropertyEditor;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.user.client.rpc.AsyncCallback;
/**
 * A ToolBar for the App Catalog main panel that includes a remote search field.
 * 
 * @author psarando
 * 
 */
public class CatalogMainToolBar extends ToolBar {
    protected String tag;
    protected AppTemplateServiceFacade templateService;
    private String lastQueryText = ""; //$NON-NLS-1$

    public CatalogMainToolBar(String tag, AppTemplateServiceFacade templateService) {
        this.tag = tag;
        this.templateService = templateService;

        setHeight(25);
        add(buildSearchField());
    }

    /**
     * Builds a combo box for searching all apps, filtered by the user's combo text, and displayed in the
     * combo's drop-down list.
     * 
     * @return A combo box of Analysis models, remotely loaded and filtered by the user's combo text.
     */
    protected Component buildSearchField() {
        // Create a loader with our custom RpcProxy.
        ListLoader<ListLoadResult<Analysis>> loader = new BaseListLoader<ListLoadResult<Analysis>>(
                buildSearchProxy());

        // Create the store
        final ListStore<Analysis> store = new ListStore<Analysis>(loader);

        // Add a load listener that sorts the store's search results.
        loader.addLoadListener(new LoadListener() {
            @Override
            public void loaderLoad(LoadEvent le) {
                store.sort(Analysis.NAME, SortDir.ASC);
            }
        });

        // Set a custom store sorter to order Apps with names that match the search before description
        // matches.
        store.setStoreSorter(new StoreSorter<Analysis>() {
            @Override
            public int compare(Store<Analysis> store, Analysis app1, Analysis app2, String property) {
                if (app1 != null && app2 != null) {
                    String searchTerm = lastQueryText.toLowerCase();

                    boolean app1NameMatches = app1.getName().toLowerCase().contains(searchTerm);
                    boolean app2NameMatches = app2.getName().toLowerCase().contains(searchTerm);

                    if (app1NameMatches && !app2NameMatches) {
                        // Only app1's name contains the search term, so order it before app2
                        return -1;
                    }
                    if (!app1NameMatches && app2NameMatches) {
                        // Only app2's name contains the search term, so order it before app1
                        return 1;
                    }
                }

                // If both or neither app contains the search term in the app name, order them according
                // to the sort called above (by App name, ascending)
                return super.compare(store, app1, app2, property);
            }
        });

        // We need to use a custom key string that will allow the combobox to find the correct model if 2
        // apps in different groups have the same name, since the combo's SelectionChange event will find
        // the first model that matches the raw text in the combo's text field.
        final ModelKeyProvider<Analysis> storeKeyProvider = new ModelKeyProvider<Analysis>() {
            @Override
            public String getKey(Analysis model) {
                return model.getId() + model.getGroupId();
            }
        };

        store.setKeyProvider(storeKeyProvider);

        // Use the custom key provider for model lookups from the raw text in the combo's text field.
        ListModelPropertyEditor<Analysis> propertyEditor = new ListModelPropertyEditor<Analysis>() {
            @Override
            public String getStringValue(Analysis value) {
                return storeKeyProvider.getKey(value);
            }

            @Override
            public Analysis convertStringValue(String value) {
                return store.findModel(value);
            }
        };

        final ComboBox<Analysis> combo = new ComboBox<Analysis>();
        combo.setWidth(225);
        combo.setItemSelector("div.search-item"); //$NON-NLS-1$
        combo.setTemplate(getTemplate());
        combo.setStore(store);
        combo.setPropertyEditor(propertyEditor);
        combo.setHideTrigger(true);
        combo.setEmptyText(I18N.DISPLAY.searchApps());
        combo.setMinChars(3);

        combo.addSelectionChangedListener(new SelectionChangedListener<Analysis>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<Analysis> se) {
                Analysis app = se.getSelectedItem();

                if (app != null) {
                    // Fire the search item selection event.
                    EventBus.getInstance().fireEvent(
                            new AppSearchResultSelectedEvent(tag, app.getGroupId(), app.getId()));
                }
            }
        });

        // Since we don't want our custom key provider's string to display after a user selects a search
        // result, reset the raw text field to the cached user query string after a selection is made.
        combo.addListener(Events.Select, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent event) {
                combo.setRawValue(lastQueryText);
            }
        });

        return combo;
    }

    /**
     * Builds an RpcProxy for the search ComboBox that will call the searchAnalysis service, then process
     * the JSON results into an Analysis list for the combo's store.
     * 
     * @return An RpcProxy for a ListLoader for the search ComboBox's store.
     */
    private RpcProxy<List<Analysis>> buildSearchProxy() {
        RpcProxy<List<Analysis>> proxy = new RpcProxy<List<Analysis>>() {
            @Override
            protected void load(Object loadConfig, final AsyncCallback<List<Analysis>> callback) {
                if (templateService == null) {
                    callback.onFailure(new Exception("Could not access service"));

                    return;
                }

                // Get the combo's search params.
                BaseListLoadConfig config = (BaseListLoadConfig)loadConfig;

                // Create a callback for the AppTemplateServiceFacade.
                AsyncCallback<String> searchCallback = new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        List<Analysis> analyses = new ArrayList<Analysis>();

                        JSONArray templates = JsonUtil.getArray(JsonUtil.getObject(result), "templates"); //$NON-NLS-1$

                        if (templates != null) {
                            for (int i = 0; i < templates.size(); i++) {
                                analyses.add(new Analysis(JsonUtil.getObjectAt(templates, i)));
                            }
                        }

                        // Pass the Analysis list to the proxy's callback.
                        callback.onSuccess(analyses);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                        callback.onFailure(caught);
                    }
                };

                // cache the query text
                lastQueryText = (String)config.get("query"); //$NON-NLS-1$

                // Call the searchAnalysis service with the combo's query.
                templateService.searchAnalysis(lastQueryText, searchCallback);
            }
        };

        return proxy;
    }

    /**
     * @return An string of html for the search ComboBox's list results.
     */
    private String getTemplate() {
        StringBuilder template = new StringBuilder();

        template.append("<tpl for=\".\"><div class=\"search-item\">"); //$NON-NLS-1$

        template.append("<h3>"); //$NON-NLS-1$

        template.append("<tpl if=\"is_favorite\">"); //$NON-NLS-1$
        template.append("<img src='./images/fav.png'></img> &nbsp;"); //$NON-NLS-1$
        template.append("</tpl>"); //$NON-NLS-1$

        template.append("{name}"); //$NON-NLS-1$

        template.append("<span><b>"); //$NON-NLS-1$
        template.append(I18N.DISPLAY.avgRating());
        template.append(":</b> {average} "); //$NON-NLS-1$
        template.append(I18N.DISPLAY.ratingOutOfTotal());
        template.append("</span>"); //$NON-NLS-1$

        template.append("</h3>"); //$NON-NLS-1$

        template.append("<h4>"); //$NON-NLS-1$
        template.append("<span>{group_name}</span>"); //$NON-NLS-1$
        template.append("<br />{description}"); //$NON-NLS-1$
        template.append("</h4>"); //$NON-NLS-1$

        template.append("</div></tpl>"); //$NON-NLS-1$

        return template.toString();
    };
}
