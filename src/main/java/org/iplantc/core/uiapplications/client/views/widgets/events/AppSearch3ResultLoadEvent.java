package org.iplantc.core.uiapplications.client.views.widgets.events;

import java.util.List;

import org.iplantc.core.uiapplications.client.models.autobeans.App;

import com.google.gwt.event.shared.GwtEvent;

public class AppSearch3ResultLoadEvent extends GwtEvent<AppSearch3ResultLoadEventHandler> {

    public static final GwtEvent.Type<AppSearch3ResultLoadEventHandler> TYPE = new GwtEvent.Type<AppSearch3ResultLoadEventHandler>();

    private final List<App> results;

    public AppSearch3ResultLoadEvent(List<App> results) {
        this.results = results;
    }

    @Override
    public GwtEvent.Type<AppSearch3ResultLoadEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppSearch3ResultLoadEventHandler handler) {
        handler.onLoad(this);
    }

    public List<App> getResults() {
        return results;
    }

}
