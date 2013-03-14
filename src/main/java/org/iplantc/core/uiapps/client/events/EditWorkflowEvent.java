package org.iplantc.core.uiapps.client.events;

import org.iplantc.core.uiapps.client.events.EditWorkflowEvent.EditWorkflowEventHandler;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * A GwtEvent fired when a user wants to edit an existing workflow.
 * 
 * @author psarando
 * 
 */
public class EditWorkflowEvent extends GwtEvent<EditWorkflowEventHandler> {

    public interface EditWorkflowEventHandler extends EventHandler {
        void onEditWorkflow(EditWorkflowEvent event);
    }

    public static final GwtEvent.Type<EditWorkflowEventHandler> TYPE = new GwtEvent.Type<EditWorkflowEventHandler>();
    private final HasId workflowToEdit;
    private Splittable legacyAppTemplate;

    public EditWorkflowEvent(HasId workflowToEdit) {
        this.workflowToEdit = workflowToEdit;
    }

    public EditWorkflowEvent(HasId workflowToEdit, Splittable legacyAppTemplate) {
        this(workflowToEdit);
        this.legacyAppTemplate = legacyAppTemplate;
    }

    @Override
    protected void dispatch(EditWorkflowEventHandler handler) {
        handler.onEditWorkflow(this);
    }

    @Override
    public GwtEvent.Type<EditWorkflowEventHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * @return the workflowToEdit
     */
    public HasId getWorkflowToEdit() {
        return workflowToEdit;
    }

    /**
     * @return the legacyAppTemplate
     */
    public Splittable getLegacyAppTemplate() {
        return legacyAppTemplate;
    }
}
