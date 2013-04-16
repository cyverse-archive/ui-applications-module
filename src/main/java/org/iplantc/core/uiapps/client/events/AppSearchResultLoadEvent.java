package org.iplantc.core.uiapps.client.events;

import java.util.List;

import org.iplantc.core.uiapps.client.events.handlers.AppSearchResultLoadEventHandler;
import org.iplantc.core.uiapps.client.models.autobeans.App;

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

    private String tag;
    private String searchText;
    private List<App> results;

    public AppSearchResultLoadEvent(String tag, String searchText, List<App> results) {
        setTag(tag);
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

    public String getTag() {
        return tag == null ? "" : tag; //$NON-NLS-1$
    }

    public void setTag(String tag) {
        this.tag = tag;
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
