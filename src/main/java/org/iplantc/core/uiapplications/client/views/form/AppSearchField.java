package org.iplantc.core.uiapplications.client.views.form;

import java.util.List;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.events.AppSearchResultLoadEvent;
import org.iplantc.core.uiapplications.client.events.AppSearchResultSelectedEvent;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.services.AppSearchRpcProxy;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelKeyProvider;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ListModelPropertyEditor;
import com.google.gwt.event.dom.client.KeyCodes;

/**
 * A ComboBox for the App Catalog main toolbar that performs remote app searches.
 * 
 * @author psarando
 * 
 */
public class AppSearchField extends ComboBox<Analysis> {
    protected String tag;
    private final AppSearchRpcProxy searchProxy;

    public AppSearchField(String tag, AppTemplateServiceFacade templateService) {
        this.tag = tag;
        this.searchProxy = new AppSearchRpcProxy(tag, templateService);

        initComboBox();
        initListeners();
    }

    private void initComboBox() {
        setWidth(255);
        setItemSelector("div.search-item"); //$NON-NLS-1$
        setTemplate(buildTemplate());
        // TODO temp hide trigger until onTriggerClick logic completed
        setHideTrigger(true);
        setTriggerStyle("x-form-search-trigger"); //$NON-NLS-1$
        setEmptyText(I18N.DISPLAY.searchApps());
        setMinChars(3);

        // Create a loader with our custom RpcProxy.
        ListLoader<ListLoadResult<Analysis>> loader = new BaseListLoader<ListLoadResult<Analysis>>(
                searchProxy);

        // Create the store
        final ListStore<Analysis> store = new ListStore<Analysis>(loader);

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

        setStore(store);
        setPropertyEditor(propertyEditor);
    }

    private void initListeners() {
        addKeyListener(new KeyListener() {
            @Override
            public void componentKeyDown(ComponentEvent event) {
                if (event.getKeyCode() == KeyCodes.KEY_ENTER) {
                    collapse();
                    fireSearchLoadedEvent();
                }
            }

            @Override
            public void componentKeyUp(ComponentEvent event) {
                int key = event.getKeyCode();

                if (key == KeyCodes.KEY_BACKSPACE || key == KeyCodes.KEY_DELETE) {
                    String query = getRawValue();

                    if (query == null || query.isEmpty()) {
                        // Fire the search results load event.
                        EventBus.getInstance().fireEvent(new AppSearchResultLoadEvent(tag, null));
                    }
                }
            }
        });

        addSelectionChangedListener(new SelectionChangedListener<Analysis>() {
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
        addListener(Events.Select, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent event) {
                setRawValue(searchProxy.getLastQueryText());
            }
        });
    }

    @Override
    protected void onTriggerClick(ComponentEvent ce) {
        fireEvent(Events.TriggerClick, ce);

        // TODO this logic just fires the search loaded event with the last search results.
        collapse();
        setRawValue(searchProxy.getLastQueryText());
        getInputEl().focus();
        fireSearchLoadedEvent();
    }

    private void fireSearchLoadedEvent() {
        List<Analysis> searchResults = getStore().getModels();

        if (searchResults != null && searchResults.isEmpty()) {
            searchResults = null;
        }

        // Fire the search results load event.
        EventBus.getInstance().fireEvent(new AppSearchResultLoadEvent(tag, searchResults));
    }

    /**
     * @return A string of html for the search ComboBox's list results.
     */
    private String buildTemplate() {
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
    }
}
