package org.iplantc.core.uiapplications.client.events.handlers;

import org.iplantc.core.uiapplications.client.events.AppGroupSelectedEvent;

import com.google.gwt.event.shared.EventHandler;

public interface AppGroupSelectedEventHandler extends EventHandler {

    void onSelection(AppGroupSelectedEvent event);
}
