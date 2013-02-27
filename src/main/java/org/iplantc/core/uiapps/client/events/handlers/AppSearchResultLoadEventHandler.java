package org.iplantc.core.uiapps.client.events.handlers;

import org.iplantc.core.uiapps.client.events.AppSearchResultLoadEvent;

import com.google.gwt.event.shared.EventHandler;

/**
 * An EventHandler interface for AppSearchResultLoadEvents.
 * 
 * @author psarando
 * 
 */
@Deprecated
public interface AppSearchResultLoadEventHandler extends EventHandler {

    void onLoad(AppSearchResultLoadEvent event);
}
