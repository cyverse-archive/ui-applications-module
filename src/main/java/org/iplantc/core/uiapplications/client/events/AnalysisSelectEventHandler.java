package org.iplantc.core.uiapplications.client.events;

import com.google.gwt.event.shared.EventHandler;

/**
 * An EventHandler interface for AnalysisSelectEvents.
 * 
 * @author psarando
 * 
 */
public interface AnalysisSelectEventHandler extends EventHandler {

    void onSelection(AnalysisSelectEvent event);
}
