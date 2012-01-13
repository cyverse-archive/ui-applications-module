package org.iplantc.core.uiapplications.client.events;

import com.google.gwt.event.shared.GwtEvent;

public class AnalysisGroupCountUpdateEvent extends GwtEvent<AnalysisGroupCountUpdateEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.de.client.events.AnalysisGroupCountUpdateEventHandler
     */
    public static final GwtEvent.Type<AnalysisGroupCountUpdateEventHandler> TYPE = new GwtEvent.Type<AnalysisGroupCountUpdateEventHandler>();

    /**
     * Describes the count update type
     * 
     * How the widget treats the enum values:
     * <dl>
     * <dt>FAVORITES</dt>
     * <dd>Informs the event handler to update the Favorites category count.</dd>
     * <dt>BETA</dt>
     * <dd>Informs the event handler to also update the Beta category count.</dd>
     * </dl>
     * 
     * @author psarando
     * 
     */
    public enum AnalysisGroupType {
        /**
         * The favorites category should be incremented instead of the user apps category.
         */
        FAVORITES,

        /**
         * The beta category should also be incremented.
         */
        BETA
    }

    private boolean increment;
    private AnalysisGroupType groupType;
  
    public AnalysisGroupCountUpdateEvent(boolean inc, AnalysisGroupType groupType) {
        setIncrement(inc);
        setAnalysisGroupType(groupType);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<AnalysisGroupCountUpdateEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnalysisGroupCountUpdateEventHandler handler) {
        handler.onGroupCountUpdate(this);
    }

    /**
     * @param inc boolean suggesting if its an increment
     */
    public void setIncrement(boolean inc) {
        this.increment = inc;
    }

    /**
     * @return the boolean suggesting if its an increment
     */
    public boolean isIncrement() {
        return increment;
    }

    /**
     * @param groupType the fav_event to set
     */
    public void setAnalysisGroupType(AnalysisGroupType groupType) {
        this.groupType = groupType;
    }

    /**
     * @return the GroupType
     */
    public AnalysisGroupType getAnalysisGroupType() {
        return groupType;
    }

}
