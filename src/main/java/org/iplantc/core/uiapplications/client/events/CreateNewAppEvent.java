package org.iplantc.core.uiapplications.client.events;

import org.iplantc.core.uiapplications.client.events.handlers.CreateNewAppEventHandler;

import com.google.gwt.event.shared.GwtEvent;

public class CreateNewAppEvent extends GwtEvent<CreateNewAppEventHandler> {

    public static final GwtEvent.Type<CreateNewAppEventHandler> TYPE = new GwtEvent.Type<CreateNewAppEventHandler>();

    @Override
    public GwtEvent.Type<CreateNewAppEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CreateNewAppEventHandler handler) {
        handler.createNewApp();
    }

}
