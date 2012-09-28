package org.iplantc.core.uiapplications.client.events.handlers;

import org.iplantc.core.uiapplications.client.events.AppSearchResultSelectedEvent;
import org.iplantc.core.uiapplications.client.views.form.AppSearchField;

import com.google.gwt.event.shared.EventHandler;

/**
 * An EventHandler interface for AppSelectEvents.
 * 
 * @author psarando
 * @deprecated this event will become obsolete when {@link AppSearchField} is deleted.
 * 
 */
@Deprecated
public interface AppSearchResultSelectedEventHandler extends EventHandler {

    void onSelection(AppSearchResultSelectedEvent event);
}
