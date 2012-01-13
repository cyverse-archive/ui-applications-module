package org.iplantc.core.uiapplications.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;

public class DeployedComponent extends AnalysisGroupTreeModel {
    private static final long serialVersionUID = 1344660608647253374L;

    public static final String LOCATION = "location"; //$NON-NLS-1$
    public static final String TYPE = "type"; //$NON-NLS-1$
    public static final String VERSION = "version"; //$NON-NLS-1$
    public static final String ATTRIBUTION = "attribution"; //$NON-NLS-1$

    /**
     * Instantiate from a JSONObject.
     * 
     * @param json JSON representation of the Deployed Component, containing keys for "id", "name", and
     *            optional "description", "location", "type", "version", and "attribution".
     */
    public DeployedComponent(JSONObject json) {
        super(json);

        setLocation(JsonUtil.getString(json, LOCATION));
        setType(JsonUtil.getString(json, TYPE));
        setVersion(JsonUtil.getString(json, VERSION));
        setAttribution(JsonUtil.getString(json, ATTRIBUTION));
    }

    /**
     * Sets the path to the directory of this component on the execution server.
     * 
     * @param location The directory of this component on the execution server.
     */
    public void setLocation(String location) {
        set(LOCATION, location);
    }

    /**
     * Retrieves the path of the directory of this component on the execution server.
     * 
     * @return The directory of this component on the execution server.
     */
    public String getLocation() {
        return get(LOCATION);
    }

    /**
     * Sets the type of this deployed component.
     * 
     * @param type
     */
    public void setType(String type) {
        set(TYPE, type);
    }

    /**
     * Retrieves the type of this deployed component.
     * 
     * @return The type as a string, as returned by the service.
     */
    public String getType() {
        return get(TYPE);
    }

    /**
     * Sets the version of this deployed component.
     * 
     * @param version
     */
    public void setVersion(String version) {
        set(VERSION, version);
    }

    /**
     * Retrieves the version of this deployed component.
     * 
     * @return The string representation of the version.
     */
    public String getVersion() {
        return get(VERSION);
    }

    /**
     * Sets the attribution information of this deployed component.
     * 
     * @param attribution
     */
    public void setAttribution(String attribution) {
        set(ATTRIBUTION, attribution);
    }

    /**
     * Retrieves the attribution information of this deployed component.
     * 
     * @return The attribution information for this deployed component.
     */
    public String getAttribution() {
        return get(ATTRIBUTION);
    }
}
