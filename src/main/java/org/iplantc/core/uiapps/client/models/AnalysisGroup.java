package org.iplantc.core.uiapps.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;

/**
 * Models a tool group
 * 
 * @author sriram
 * 
 */
public class AnalysisGroup extends AnalysisGroupTreeModel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -746475516662261372L;

    public static final String PUBLIC = "is_public"; //$NON-NLS-1$
    public static final String COUNT = "template_count"; //$NON-NLS-1$
    public static final String DISPLAY = "display_name"; //$NON-NLS-1$

    /**
     * Instantiate from id and name.
     * 
     * @param id unique tool group id.
     * @param name tool group name.
     * @param description tool group description
     * @param count no.of items under this group
     * @param is_public flag indicating if the this group falls under public category
     */
    public AnalysisGroup(String id, String name, String description, int count, boolean is_public) {
        super(id, name, description);

       setCount(count);
       setIsPublic(is_public);
    }
    
    public AnalysisGroup(JSONObject json) {
        super(json);

        setCount(JsonUtil.getNumber(json, COUNT));
        setIsPublic(JsonUtil.getBoolean(json, PUBLIC, false));
    }

    public void setIsPublic(boolean is_public) {
        set(PUBLIC, is_public);
    }

    /**
     * get the no. of items in this group
     * @return no.of items in the group
     */
    public int getCount() {
        Number count = get(COUNT);

        return count == null ? 0 : count.intValue();
    }
    
    /**
     * get the display name to be used
     * @return name plus the count appended to the end
     */
    public String getDisplayName() {
        return get(DISPLAY);
    }
    
    /**
     * set the # of analyses under this group
     * 
     * @param count
     */
    public void setCount(Number count) {
        if (count == null) {
            count = new Integer(0);
        }

        set(COUNT, count);

        updateDisplayName();
    }

    @Override
    public void setName(String name) {
        super.setName(name);

        updateDisplayName();
    }

    private void updateDisplayName() {
        set(DISPLAY, getName() + " (" + getCount() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public Boolean isPublic() {
        return get(PUBLIC);
    }

    @Override
    public JSONObject toJson() {
        JSONObject ret = super.toJson();

        ret.put(COUNT, new JSONNumber(getCount()));
        ret.put(PUBLIC, JSONBoolean.getInstance(isPublic()));

        return ret;
    }
}
