package org.iplantc.core.uiapps.client.views.widgets.events;

import java.util.List;

import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.views.widgets.proxy.AppSearchRpcProxy;

import com.google.gwt.event.shared.GwtEvent;

/**
 * A GwtEvent used to notify listeners that App search results have been loaded from the search service.
 * 
 * @author psarando
 * 
 */
public class AppSearchResultLoadEvent extends GwtEvent<AppSearchResultLoadEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.uiapplications.client.events.AppSearchResultSelectedEventHandler
     */
    public static final GwtEvent.Type<AppSearchResultLoadEventHandler> TYPE = new GwtEvent.Type<AppSearchResultLoadEventHandler>();

    private String searchText;
    private List<App> results;

    public AppSearchResultLoadEvent(AppSearchRpcProxy proxy, String searchText, List<App> results) {
        setSource(proxy);
        setSearchText(searchText);
        setResults(results);
    }

    @Override
    public Type<AppSearchResultLoadEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppSearchResultLoadEventHandler handler) {
        handler.onLoad(this);
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<App> getResults() {
        return results;
    }

    public void setResults(List<App> results) {
        this.results = results;
    }
}
