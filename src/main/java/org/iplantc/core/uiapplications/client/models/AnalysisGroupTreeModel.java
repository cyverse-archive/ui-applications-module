package org.iplantc.core.uiapplications.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.extjs.gxt.ui.client.data.BaseTreeModel;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

public class AnalysisGroupTreeModel extends BaseTreeModel {
 
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String ID = "id"; //$NON-NLS-1$
    public static final String NAME = "name"; //$NON-NLS-1$
    public static final String DESCRIPTION = "description"; //$NON-NLS-1$

    /**
     * Instantiate from id and name.
     * 
     * @param id unique tool group id.
     * @param name tool group name.
     * @param description tool group description
     */
    public AnalysisGroupTreeModel(String id, String name, String description) {
        setId(id);
        setName(name);
        setDescription(description);

    }

    /**
     * Instantiate from a JSONObject.
     * 
     * @param json JSON representation of the App Group, containing keys for "id", "name", and optional
     *            "description".
     */
    public AnalysisGroupTreeModel(JSONObject json) {
        setId(JsonUtil.getString(json, ID));
        setName(JsonUtil.getString(json, NAME));
        setDescription(JsonUtil.getString(json, DESCRIPTION));
    }

    /**
     * set description for this analysis tool group
     * 
     * @param description
     */
    public void setDescription(String description) {
        set(DESCRIPTION, description);

    }

    /**
     * Set unique id.
     * 
     * @param id unique id for this analysis group.
     */
    public void setId(String id) {
        set(ID, id);
    }

    /**
     * Retrieve unique id.
     * 
     * @return analysis group id.
     */
    public String getId() {
        return get(ID);
    }

    /**
     * Set resource name.
     * 
     * @param name name for this analysis group.
     */
    public void setName(String name) {
        set(NAME, name);
    }

    /**
     * Retrieve model's name.
     * 
     * @return resource name.
     */
    public String getName() {
        return get(NAME);
    }

    /**
     * Retrieve model's optional description.
     * 
     * @return description.
     */
    public String getDescription() {
        return get(DESCRIPTION);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * Builds a JSONObject representation of this model.
     * 
     * @return JSONObject representation of this model.
     */
    public JSONObject toJson() {
        JSONObject ret = new JSONObject();

        ret.put(ID, new JSONString(getId()));
        ret.put(NAME, new JSONString(getName()));
        ret.put(DESCRIPTION, new JSONString(getDescription()));

        return ret;
    }
}
