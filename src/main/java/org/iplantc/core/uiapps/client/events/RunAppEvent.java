package org.iplantc.core.uiapps.client.events;

import org.iplantc.core.uiapps.client.events.RunAppEvent.RunAppEventHandler;
import org.iplantc.core.uiapps.client.models.autobeans.App;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * An event indicating that a "Run App" action has been initiated.
 * 
 * @author jstroot
 * 
 */
public class RunAppEvent extends GwtEvent<RunAppEventHandler> {

    public interface RunAppEventHandler extends EventHandler {
        void onRunAppActionInitiated(RunAppEvent event);
    }

    public static final GwtEvent.Type<RunAppEventHandler> TYPE = new GwtEvent.Type<RunAppEventHandler>();
    private final App app;

    public RunAppEvent(App app) {
        this.app = app;
    }

    @Override
    public GwtEvent.Type<RunAppEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RunAppEventHandler handler) {
        handler.onRunAppActionInitiated(this);
    }

    public App getAppToRun() {
        return app;
    }
}
