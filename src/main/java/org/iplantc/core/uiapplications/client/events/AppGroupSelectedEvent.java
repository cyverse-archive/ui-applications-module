package org.iplantc.core.uiapplications.client.events;

import org.iplantc.core.uiapplications.client.events.handlers.AppGroupSelectedEventHandler;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uiapplications.client.views.panels.AbstractCatalogCategoryPanel;

import com.google.gwt.event.shared.GwtEvent;

public class AppGroupSelectedEvent extends GwtEvent<AppGroupSelectedEventHandler> {

    
    /**
     * Defines the GWT Event Type.
     * 
     * @see org.iplantc.core.uiapplications.client.events.handlers.AppGroupSelectedEventHandler.AnalysisCategorySelectedEventHandler
     */
    public static final GwtEvent.Type<AppGroupSelectedEventHandler> TYPE = new GwtEvent.Type<AppGroupSelectedEventHandler>();
    
    
    private AnalysisGroup group;
    private AbstractCatalogCategoryPanel sourcePanel;
    
    public AppGroupSelectedEvent(AnalysisGroup ag, AbstractCatalogCategoryPanel sourcePanel) {
       this.setGroup(ag);
        this.setSourcePanel(sourcePanel);
    }

    @Override
    protected void dispatch(AppGroupSelectedEventHandler handler) {
      handler.onSelection(this);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Type<AppGroupSelectedEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(AnalysisGroup group) {
        this.group = group;
    }

    /**
     * @return the group
     */
    public AnalysisGroup getGroup() {
        return group;
    }

    public void setSourcePanel(AbstractCatalogCategoryPanel sourcePanel) {
        this.sourcePanel = sourcePanel;
    }

    /**
     * Returns the component from which the event originated.
     * 
     * @return the source panel
     */
    public AbstractCatalogCategoryPanel getSourcePanel() {
        return sourcePanel;
    }

}
