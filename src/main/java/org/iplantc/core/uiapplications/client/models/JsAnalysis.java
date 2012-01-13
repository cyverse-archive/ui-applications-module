package org.iplantc.core.uiapplications.client.models;

import com.google.gwt.core.client.JavaScriptObject;

public class JsAnalysis extends JavaScriptObject {
    
    
    protected JsAnalysis() {
        
    }
    
    /**
     * Gets the name value of the Analysis from the object.
     * 
     * @return a string representing the name of the Analysis
     */
    public final native String getName() /*-{
                                         return this.name;
                                         }-*/;

    /**
     * Gets the Integrator's name value of the Analysis from the object.
     * 
     * @return a string representing the Integrator's name
     */
    public final native String getIntegratorName() /*-{
                                         return this.integrator_name;
                                         }-*/;

    /**
     * Gets a user-defined description value for the Analysis from the object.
     * 
     * @return a string representing the user's description of the Analysis
     */
    public final native String getDescription() /*-{
                                                return this.description;
                                                }-*/;

    /**
     * Gets an internal identifier value for the Analysis from the object.
     * 
     * @return a string representing a unique identifier for the Analysis
     */
    public final native String getId() /*-{
                                       return this.id;
                                       }-*/;
    
    /**
     * Gets the email value of the Analysis integrator from the object.
     * 
     * @return a string representing the email id of the Analysis integrator
     */
    public final native String getIntegratorEmail() /*-{
                                         return this.integrator_email;
                                         }-*/;
    
    /**
     * Gets the date value of the Analysis integration from the object.
     * 
     * @return a string representing the date of the Analysis integration
     */
    public final native String getIntegrationDate() /*-{
                                         return this.integration_date;
                                         }-*/;
    
    /**
     * Gets wiki url value for the analysis.
     * 
     * @return a string representing wiki url for the analysis
     */
    public final native String getWikiUrl() /*-{
                                       return this.wiki_url;
                                       }-*/;
}
