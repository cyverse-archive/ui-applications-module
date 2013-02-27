package org.iplantc.core.uiapps.client.events.handlers;

import org.iplantc.core.uiapps.client.events.AppGroupSelectedEvent;

import com.google.gwt.event.shared.EventHandler;

public interface AppGroupSelectedEventHandler extends EventHandler {

    void onSelection(AppGroupSelectedEvent event);
}
