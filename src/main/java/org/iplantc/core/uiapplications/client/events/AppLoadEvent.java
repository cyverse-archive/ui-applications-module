package org.iplantc.core.uiapplications.client.events;


import org.iplantc.core.uiapplications.client.events.AppLoadEvent.AppLoadEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when a templated is loaded
 * @author sriram
 * 
 */
public class AppLoadEvent extends GwtEvent<AppLoadEventHandler> {

    /**
     * An event handler for TemplateLoadEvent
     * 
     * @author sriram
     * 
     */
    public interface AppLoadEventHandler extends EventHandler {

        /**
         * invoked when template is loaded
         * 
         * @param event
         */
        void onLoad(AppLoadEvent event);
    }

    public static enum MODE {
        EDIT,
        
        COPY
      };
    
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.tito.client.events.NavigationTreeAddEventHandler
     */
    public static final GwtEvent.Type<AppLoadEventHandler> TYPE = new GwtEvent.Type<AppLoadEventHandler>();

    private final String idTemplate;
    private final MODE mode;

    public AppLoadEvent(String id, MODE mode) {
        this.idTemplate = id;
        this.mode = mode;
    }

    /**
     * @return the idTemplate
     */
    public String getIdTemplate() {
        return idTemplate;
    }

    @Override
    protected void dispatch(AppLoadEventHandler handler) {
        handler.onLoad(this);
    }

    @Override
    public GwtEvent.Type<AppLoadEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the mode
     */
    public MODE getMode() {
        return mode;
    }

}
