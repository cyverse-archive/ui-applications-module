/**
 * 
 */
package org.iplantc.core.uiapplications.client.events;

import org.iplantc.core.uiapplications.client.events.handlers.AppSelectedEventHandler;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author sriram
 *
 */
public class AppSelectedEvent extends GwtEvent<AppSelectedEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.uiapplications.client.events.handlers.AppSearchResultSelectedEventHandler
     */
    public static final GwtEvent.Type<AppSelectedEventHandler> TYPE = new GwtEvent.Type<AppSelectedEventHandler>();

    private String tag;
    private String categoryId;
    private String appId;

    public AppSelectedEvent(String tag, String categoryId, String appId) {
        this.tag = tag;
        this.categoryId = categoryId;
        this.appId = appId;
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<AppSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AppSelectedEventHandler handler) {
        handler.onSelection(this);
    }

    /**
     * @return the tag
     */
    public String getTag() {
        return tag;
    }

    /**
     * @return the categoryId
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * @return the appId
     */
    public String getAppId() {
        return appId;
    }

}
