package org.iplantc.core.uiapplications.client.views.widgets.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * A GwtEvent used to notify handlers that an App was selected from an App search result list.
 * 
 * @author jstroot
 * 
 */
public class AppSearch3ResultSelectedEvent extends GwtEvent<AppSearch3ResultSelectedEventHandler> {

    public static final GwtEvent.Type<AppSearch3ResultSelectedEventHandler> TYPE = new GwtEvent.Type<AppSearch3ResultSelectedEventHandler>();
    private final String appId;
    private final String appGroupId;

    public AppSearch3ResultSelectedEvent(String appId, String appGroupId) {
        this.appId = appId;
        this.appGroupId = appGroupId;
    }

    @Override
    public GwtEvent.Type<AppSearch3ResultSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppSearch3ResultSelectedEventHandler handler) {
        handler.onSelect(this);
    }

    public String getAppId() {
        return appId;
    }

    public String getAppGroupId() {
        return appGroupId;
    }
}
