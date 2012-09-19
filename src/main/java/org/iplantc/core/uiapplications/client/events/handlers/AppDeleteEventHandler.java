package org.iplantc.core.uiapplications.client.events.handlers;

import org.iplantc.core.uiapplications.client.events.AppDeleteEvent;

import com.google.gwt.event.shared.EventHandler;

public interface AppDeleteEventHandler extends EventHandler {
    public void onDelete(AppDeleteEvent ade);
}
