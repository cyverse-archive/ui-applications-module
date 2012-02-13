package org.iplantc.core.uiapplications.client.views.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.events.AnalysisSelectEvent;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.data.BaseListLoadConfig;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
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
        ListLoader<ListLoadResult<Analysis>> loader = new BaseListLoader<ListLoadResult<Analysis>>(
                buildSearchProxy());

        // We need to use a custom key string that will allow the combobox to find the correct model if 2
        // apps in different groups have the same name, since the combo's SelectionChange event will find
        // the first model that matches the raw text in the combo's text field.
        final ModelKeyProvider<Analysis> storeKeyProvider = new ModelKeyProvider<Analysis>() {
            @Override
            public String getKey(Analysis model) {
                return model.getId() + model.getGroupId();
            }
        };

        final ListStore<Analysis> store = new ListStore<Analysis>(loader);
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
        combo.setWidth(300);
        combo.setItemSelector("div.app-search-item"); //$NON-NLS-1$
        combo.setTemplate(getTemplate());
        combo.setStore(store);
        combo.setPropertyEditor(propertyEditor);
        combo.setHideTrigger(true);
        combo.setEmptyText(I18N.DISPLAY.searchApps());

        // Since we don't want our custom key provider's string to display after a user selects a search
        // result, cache the user's query string, then reset the raw text field to that value after a
        // selection is made.
        combo.addListener(Events.BeforeSelect, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent event) {
                lastQueryText = event.getField().getRawValue();
            }
        });

        combo.addSelectionChangedListener(new SelectionChangedListener<Analysis>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<Analysis> se) {
                Analysis app = se.getSelectedItem();

                if (app != null) {
                    // Fire the search item selection event.
                    EventBus.getInstance().fireEvent(
                            new AnalysisSelectEvent(tag, app.getGroupId(), app.getId()));
                }
            }
        });

        // Reset the search field to the user's entered text.
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

                // Call the searchAnalysis service with the combo's query.
                templateService.searchAnalysis((String)config.get("query"), searchCallback); //$NON-NLS-1$
            }
        };

        return proxy;
    }

    /**
     * @return An string of html for the search ComboBox's list results.
     */
    private String getTemplate() {
        StringBuilder template = new StringBuilder();

        template.append("<tpl for=\".\"><div class=\"app-search-item\">"); //$NON-NLS-1$

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
