package org.iplantc.core.uiapplications.client.events.handlers;

import org.iplantc.core.uiapplications.client.events.AppGroupCountUpdateEvent;

import com.google.gwt.event.shared.EventHandler;

public interface AppGroupCountUpdateEventHandler extends EventHandler {
    void onGroupCountUpdate(AppGroupCountUpdateEvent event);
}
