package org.iplantc.core.uiapplications.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * An EventHandler interface for AnalysisSelectEvents.
 * 
 * @author psarando
 * 
 */
public interface AppSearchResultSelectedEventHandler extends EventHandler {

    void onSelection(AppSearchResultSelectedEvent event);
}
