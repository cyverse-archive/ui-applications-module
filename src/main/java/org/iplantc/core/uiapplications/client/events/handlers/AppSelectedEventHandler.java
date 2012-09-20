package org.iplantc.core.uiapplications.client.events.handlers;

import org.iplantc.core.uiapplications.client.events.AppSelectedEvent;

import com.google.gwt.event.shared.EventHandler;

public interface AppSelectedEventHandler extends EventHandler {

    public void onSelection(AppSelectedEvent event);
        
}
