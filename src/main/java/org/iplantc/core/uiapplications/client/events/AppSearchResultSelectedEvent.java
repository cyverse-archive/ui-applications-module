package org.iplantc.core.uiapplications.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * A GwtEvent that contains an App ID and its Category ID, used to notify view listeners that the given
 * category and app should be selected.
 * 
 * @author psarando
 * 
 */
public class AppSearchResultSelectedEvent extends GwtEvent<AppSearchResultSelectedEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.uiapplications.client.events.AppSearchResultSelectedEventHandler
     */
    public static final GwtEvent.Type<AppSearchResultSelectedEventHandler> TYPE = new GwtEvent.Type<AppSearchResultSelectedEventHandler>();

    private String tag;
    private String categoryId;
    private String appId;

    public AppSearchResultSelectedEvent(String sourceTag, String categoryId, String appId) {
        setSourceTag(sourceTag);
        setCategoryId(categoryId);
        setAppId(appId);
    }

    @Override
    protected void dispatch(AppSearchResultSelectedEventHandler handler) {
        handler.onSelection(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type<AppSearchResultSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @param tag A Tag identifying the source of the event.
     */
    public void setSourceTag(String tag) {
        this.tag = tag;
    }

    /**
     * @return The Tag identifying the source of the event.
     */
    public String getSourceTag() {
        return tag;
    }

    /**
     * @param categoryId the Category ID to select
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the Category ID to select
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * @param appId the App ID to select
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return the App ID to select
     */
    public String getAppId() {
        return appId;
    }
}
