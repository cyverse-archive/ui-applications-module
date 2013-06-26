package org.iplantc.core.uiapps.client.events;

import org.iplantc.core.uiapps.client.events.AppUpdatedEvent.AppUpdatedEventHandler;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AppUpdatedEvent extends GwtEvent<AppUpdatedEventHandler> {

    public interface AppUpdatedEventHandler extends EventHandler {
        void onAppUpdated(AppUpdatedEvent event);
    }

    public static final GwtEvent.Type<AppUpdatedEventHandler> TYPE = new GwtEvent.Type<AppUpdatedEventHandler>();
    private final HasId app;

    public AppUpdatedEvent(HasId app) {
        this.app = app;
    }

    @Override
    public GwtEvent.Type<AppUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppUpdatedEventHandler handler) {
        handler.onAppUpdated(this);
    }

    public HasId getApp() {
        return app;
    }

}
