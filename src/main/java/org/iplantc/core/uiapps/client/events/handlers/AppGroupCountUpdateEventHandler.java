package org.iplantc.core.uiapps.client.events.handlers;

import org.iplantc.core.uiapps.client.events.AppGroupCountUpdateEvent;

import com.google.gwt.event.shared.EventHandler;

public interface AppGroupCountUpdateEventHandler extends EventHandler {
    void onGroupCountUpdate(AppGroupCountUpdateEvent event);
}
