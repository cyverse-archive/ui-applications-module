package org.iplantc.core.uiapplications.client.models;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * A class that uses native methods to return fields for attributes.
 * 
 * This object simulates a Plain Old Java Object from the Java perspective and use the JavaScript Native
 * Interface to inter-operate with the JSON representation.
 * 
 * @author sriram
 * 
 */
public class JsAnalysisGroup extends JavaScriptObject {

    protected JsAnalysisGroup() {

    }

    /**
     * Gets the name value of the group from the object.
     * 
     * @return a string representing the name of the group
     */
    public final native String getName() /*-{
                                         return this.name;
                                         }-*/;

    /**
     * Gets a user-defined description value for the group from the object.
     * 
     * @return a string representing the user's description of the group
     */
    public final native String getDescription() /*-{
                                                return this.description;
                                                }-*/;

    /**
     * Gets an internal identifier value for the group from the object.
     * 
     * @return a string representing a unique identifier for the group
     */
    public final native String getId() /*-{
                                       return this.id;
                                       }-*/;

}
