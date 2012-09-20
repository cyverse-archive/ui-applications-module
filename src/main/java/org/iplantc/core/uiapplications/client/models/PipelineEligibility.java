package org.iplantc.core.uiapplications.client.models;

import org.iplantc.core.jsonutil.JsonUtil;

import com.google.gwt.json.client.JSONObject;

/**
 * @author psarando
 *
 */
public class PipelineEligibility {
    public static final String IS_VALID = "is_valid"; //$NON-NLS-1$
    public static final String REASON = "reason"; //$NON-NLS-1$
    
    private boolean valid;
    private String reason;

    PipelineEligibility(JSONObject json) {
        setValid(JsonUtil.getBoolean(json, IS_VALID, true));
        setReason(JsonUtil.getString(json, REASON));
    }

    /**
     * Sets pipeline eligibility.
     * 
     * @param valid the eligibility to set.
     */
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Gets pipeline eligibility.
     * 
     * @return pipeline eligibility.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Sets the reason pipeline eligibility is invalid.
     * 
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Gets the invalid pipeline eligibility reason.
     * 
     * @return the reason
     */
    public String getReason() {
        return reason;
    }
}
