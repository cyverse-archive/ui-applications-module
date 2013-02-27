package org.iplantc.core.uiapps.client.events.handlers;

import org.iplantc.core.uiapps.client.events.AppSelectedEvent;

import com.google.gwt.event.shared.EventHandler;

public interface AppSelectedEventHandler extends EventHandler {

    public void onSelection(AppSelectedEvent event);
        
}
