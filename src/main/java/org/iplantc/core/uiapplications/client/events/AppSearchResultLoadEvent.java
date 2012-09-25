package org.iplantc.core.uiapplications.client.events;

import java.util.List;

import org.iplantc.core.uiapplications.client.events.handlers.AppSearchResultLoadEventHandler;
import org.iplantc.core.uiapplications.client.models.Analysis;

import com.google.gwt.event.shared.GwtEvent;

/**
 * A GwtEvent used to notify listeners that App search results have been loaded from the search service.
 * 
 * @author psarando
 * 
 */
@Deprecated
public class AppSearchResultLoadEvent extends GwtEvent<AppSearchResultLoadEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.uiapplications.client.events.handlers.AppSearchResultSelectedEventHandler
     */
    public static final GwtEvent.Type<AppSearchResultLoadEventHandler> TYPE = new GwtEvent.Type<AppSearchResultLoadEventHandler>();

    private String tag;
    private List<Analysis> results;

    public AppSearchResultLoadEvent(String tag, List<Analysis> results) {
        setTag(tag);
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

    public List<Analysis> getResults() {
        return results;
    }

    public void setResults(List<Analysis> results) {
        this.results = results;
    }
}
