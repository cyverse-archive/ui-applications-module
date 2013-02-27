package org.iplantc.core.uiapps.client.events;


import org.iplantc.core.uiapps.client.events.AppDeleteEvent.AppDeleteEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when an anlysis is deleted
 * 
 * @author sriram
 * 
 */
public class AppDeleteEvent extends GwtEvent<AppDeleteEventHandler> {

    public interface AppDeleteEventHandler extends EventHandler {
        public void onDelete(AppDeleteEvent ade);
    }

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.uiapps.client.events.handlers.AppSearchResultSelectedEventHandler
     */
    public static final GwtEvent.Type<AppDeleteEventHandler> TYPE = new GwtEvent.Type<AppDeleteEventHandler>();

    private String id;

    /**
     * Create a new instance of AppDeleteEvent
     * 
     * @param id id of the App delete
     */
    public AppDeleteEvent(String id) {
        this.setId(id);
    }

    @Override
    public GwtEvent.Type<AppDeleteEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppDeleteEventHandler handler) {
        handler.onDelete(this);

    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

}
