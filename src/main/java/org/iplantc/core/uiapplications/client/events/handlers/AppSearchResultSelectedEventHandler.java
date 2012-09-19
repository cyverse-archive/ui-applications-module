package org.iplantc.core.uiapplications.client.events.handlers;

import org.iplantc.core.uiapplications.client.events.AppSearchResultSelectedEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * An EventHandler interface for AppSelectEvents.
 * 
 * @author psarando
 * 
 */
public interface AppSearchResultSelectedEventHandler extends EventHandler {

    void onSelection(AppSearchResultSelectedEvent event);
}
