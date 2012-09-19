package org.iplantc.core.uiapplications.client.events.handlers;

import com.google.gwt.event.shared.EventHandler;

public interface CreateNewAppEventHandler extends EventHandler {

    /**
     * Fired when a user wants to create a new app.
     */
    void createNewApp();

}
