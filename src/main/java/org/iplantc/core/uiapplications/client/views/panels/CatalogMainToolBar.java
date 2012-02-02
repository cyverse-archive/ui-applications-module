package org.iplantc.core.uiapplications.client.views.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;

import com.extjs.gxt.ui.client.data.BaseListLoadConfig;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
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
    protected AppTemplateServiceFacade templateService;

    public CatalogMainToolBar(AppTemplateServiceFacade templateService) {
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
    protected ComboBox<Analysis> buildSearchField() {
        ListLoader<ListLoadResult<Analysis>> loader = new BaseListLoader<ListLoadResult<Analysis>>(
                buildSearchProxy());

        ListStore<Analysis> store = new ListStore<Analysis>(loader);

        ComboBox<Analysis> combo = new ComboBox<Analysis>();
        combo.setWidth(300);
        combo.setDisplayField("name"); //$NON-NLS-1$
        combo.setItemSelector("div.app-search-item"); //$NON-NLS-1$
        combo.setTemplate(getTemplate());
        combo.setStore(store);
        combo.setHideTrigger(true);
        combo.setEmptyText(I18N.DISPLAY.filterDataList());

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
                    callback.onFailure(new Exception("Could not access service")); //$NON-NLS-1$

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
