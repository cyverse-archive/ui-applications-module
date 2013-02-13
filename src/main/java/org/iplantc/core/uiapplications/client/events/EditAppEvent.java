package org.iplantc.core.uiapplications.client.events;

import org.iplantc.core.uiapplications.client.events.EditAppEvent.EditAppEventHandler;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.autobean.shared.Splittable;

public class EditAppEvent extends GwtEvent<EditAppEventHandler> {

    public interface EditAppEventHandler extends EventHandler {

        void onEditApp(EditAppEvent event);
    }

    public static final GwtEvent.Type<EditAppEventHandler> TYPE = new GwtEvent.Type<EditAppEventHandler>();
    private final HasId appToEdit;
    private Splittable legacyAppTemplate;

    public EditAppEvent(HasId appToEdit) {
        this.appToEdit = appToEdit;
    }

    public EditAppEvent(HasId appToEdit, Splittable legacyAppTemplate) {
        this(appToEdit);
        this.legacyAppTemplate = legacyAppTemplate;
    }

    @Override
    protected void dispatch(EditAppEventHandler handler) {
        handler.onEditApp(this);
    }

    @Override
    public GwtEvent.Type<EditAppEventHandler> getAssociatedType() {
        return TYPE;
    }

    public HasId getAppToEdit() {
        return appToEdit;
    }

    public Splittable getLegacyAppTemplate() {
        return legacyAppTemplate;
    }
}
