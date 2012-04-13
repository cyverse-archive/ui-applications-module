package org.iplantc.core.uiapplications.client.events;

import com.google.gwt.event.shared.GwtEvent;

/**
 * 
 * An event that is fired when an anlysis is deleted
 * 
 * @author sriram
 * 
 */
public class AnalysisDeleteEvent extends GwtEvent<AnalysisDeleteEventHandler> {

    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.uiapplications.client.events.AnalysisSelectEventHandler
     */
    public static final GwtEvent.Type<AnalysisDeleteEventHandler> TYPE = new GwtEvent.Type<AnalysisDeleteEventHandler>();

    private String id;

    /**
     * Create a new instance of AnalysisDeleteEvent
     * 
     * @param id id of the Analysis delete
     */
    public AnalysisDeleteEvent(String id) {
        this.setId(id);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<AnalysisDeleteEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AnalysisDeleteEventHandler handler) {
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
