package org.iplantc.core.uiapps.client.models;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.util.DateParser;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

/**
 * Models an Analysis tool
 * 
 * @author sriram
 * 
 */
public class Analysis extends AnalysisGroupTreeModel {
    private static final long serialVersionUID = 1L;

    public static final String INTEGRATOR_NAME = "integrator_name"; //$NON-NLS-1$
    public static final String INTEGRATOR_EMAIL = "integrator_email"; //$NON-NLS-1$
    public static final String INTEGRATION_DATE = "integration_date"; //$NON-NLS-1$
    public static final String RATING = "rating"; //$NON-NLS-1$
    public static final String FAVOURITE = "is_favorite"; //$NON-NLS-1$
    public static final String PUBLIC = "is_public"; //$NON-NLS-1$
    public static final String WIKI_URL = "wiki_url"; //$NON-NLS-1$
    public static final String DEPLOYED_COMPONENTS = "deployed_components"; //$NON-NLS-1$
    public static final String SUGGESTED_CATEGORIES = "suggested_categories"; //$NON-NLS-1$
    public static final String DISABLED = "disabled"; //$NON-NLS-1$
    public static final String GROUP_ID = "group_id"; //$NON-NLS-1$
    public static final String GROUP_NAME = "group_name"; //$NON-NLS-1$
    public static final String PIPELINE_ELIGIBILITY = "pipeline_eligibility"; //$NON-NLS-1$

    /**
     * Creates a new App.
     * 
     * @param json JSON representation of the App, containing keys for "id", "name", and optional
     *            "description", "integrator_name", "integrator_email", "integration_date", "rating",
     *            "is_favorite", "is_public", "wiki_url", "deployed_components", "disabled" and
     *            "suggested_categories".
     */
    public Analysis(JSONObject json) {
        super(JsonUtil.getString(json, ID), JsonUtil.getString(json, NAME), JsonUtil.getString(json,
                DESCRIPTION));

        if (getDescription().isEmpty()) {
            setDescription(JsonUtil.getString(json, "desc")); //$NON-NLS-1$
        }

        setIntegratorName(JsonUtil.getString(json, INTEGRATOR_NAME));
        setIntegratorEmail(JsonUtil.getString(json, INTEGRATOR_EMAIL));
        setGroupId(JsonUtil.getString(json, GROUP_ID));
        setGroupName(JsonUtil.getString(json, GROUP_NAME));
        setWikiUrl(JsonUtil.getString(json, WIKI_URL));

        long timestamp = 0;
        Number num = JsonUtil.getNumber(json, INTEGRATION_DATE);
        if (num != null) {
            timestamp = num.longValue();
        }
        if (timestamp != 0) {
            setIntegrationDate(DateTimeFormat
                    .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM)
                .format(DateParser.parseDate(timestamp)));
        }

        setUser_favourite(JsonUtil.getBoolean(json, FAVOURITE, false));
        setPublic(JsonUtil.getBoolean(json, PUBLIC, false));
        setDisabled(JsonUtil.getBoolean(json, DISABLED, false));

        setAnalysisFeedback(new AnalysisFeedback(JsonUtil.getObject(json, RATING)));
        setPipelineEligibility(new PipelineEligibility(JsonUtil.getObject(json, PIPELINE_ELIGIBILITY)));
        setSuggestedCategories(parseSuggestedCategories(JsonUtil.getArray(json, SUGGESTED_CATEGORIES)));
    }

    private List<AnalysisGroup> parseSuggestedCategories(JSONArray suggestedCategories) {
        List<AnalysisGroup> ret = null;

        if (suggestedCategories != null && suggestedCategories.size() > 0) {
            ret = new ArrayList<AnalysisGroup>();

            for (int i = 0,size = suggestedCategories.size(); i < size; i++) {
                ret.add(new AnalysisGroup(JsonUtil.getObjectAt(suggestedCategories, i)));
            }
        }

        return ret;
    }

    private void setAnalysisFeedback(AnalysisFeedback feedback) {
        set(RATING, feedback);
        set(AnalysisFeedback.RATING_AVG, feedback.getAverage_score());
    }

    /**
     * set integration date for this analysis
     * 
     * @param integrators_date integration date
     */
    public void setIntegrationDate(String integration_date) {
        set(INTEGRATION_DATE, integration_date);
    }

    /**
     * set integrators email for this analysis
     * 
     * @param integrators_email integrators email
     */
    public void setIntegratorEmail(String integrator_email) {
        set(INTEGRATOR_EMAIL, integrator_email);
    }

    /**
     * set description for this analysis tool group
     * 
     * @param integrators_name integrators name
     */
    public void setIntegratorName(String integrator_name) {
        set(INTEGRATOR_NAME, integrator_name);
    }

    /**
     * get intergrators name
     * 
     * @return integrators name
     */
    public String getIntegratorsName() {
        return get(INTEGRATOR_NAME);
    }

    /**
     * get integrator email
     * 
     * @return integrators email
     */
    public String getIntegratorsEmail() {
        return get(INTEGRATOR_EMAIL);
    }

    /**
     * get integration date
     * 
     * @return integration date
     */
    public String getIntegrationDate() {
        return get(INTEGRATION_DATE);
    }

    /**
     * @return the feedback
     */
    public AnalysisFeedback getFeedback() {
        return get(RATING);
    }

    /**
     * Sets the Wiki Documentation URL for this App.
     * 
     * @param wiki_url The new Wiki URL.
     */
    public void setWikiUrl(String wiki_url) {
        set(WIKI_URL, wiki_url);
    }

    /**
     * Retrieves the Wiki Documentation URL for this App.
     * 
     * @return A URL string.
     */
    public String getWikiUrl() {
        return get(WIKI_URL);
    }

    /**
     * Sets the list of Suggested Categories selected by this App's integrator.
     * 
     * @param suggestedCategories
     */
    public void setSuggestedCategories(List<AnalysisGroup> suggestedCategories) {
        set(SUGGESTED_CATEGORIES, suggestedCategories);
    }

    /**
     * Gets the list of Suggested Categories selected by this App's integrator.
     * 
     * @return A list of AnalysisGroup models.
     */
    public List<AnalysisGroup> getSuggestedCategories() {
        return get(SUGGESTED_CATEGORIES);
    }

    /**
     * @param user_favourite the user_favourite to set
     */
    public void setUser_favourite(boolean user_favourite) {
        set(FAVOURITE, user_favourite);
    }

    /**
     * @return the user_favourite
     */
    public Boolean isUser_favourite() {
        return get(FAVOURITE);
    }

    /**
     * Sets or unsets this App as public.
     * 
     * @param isPublic
     */
    public void setPublic(boolean isPublic) {
        set(PUBLIC, isPublic);
    }

    /**
     * @return True if this App has been made public.
     */
    public Boolean isPublic() {
        return get(PUBLIC);
    }

    /**
     * set the app to enabled or disabled
     * 
     * @param disabled boolean indicating whether the app is disabled
     */
    public void setDisabled(boolean disabled) {
        set(DISABLED, disabled);
    }

    /**
     * check if the app is disabled
     * 
     * @return a boolean indicating whether the app is disabled
     */
    public Boolean isDisabled() {
        return get(DISABLED);
    }

    /**
     * Sets the Group ID this App is listed in.
     * 
     * @param groupId The Group ID of this App.
     */
    public void setGroupId(String groupId) {
        set(GROUP_ID, groupId);
    }

    /**
     * Retrieves the Group ID this App is listed in.
     * 
     * @return The Group ID of this App.
     */
    public String getGroupId() {
        return get(GROUP_ID);
    }

    /**
     * Sets the Group name this App is listed in.
     * 
     * @param groupName The Group name of this App.
     */
    public void setGroupName(String groupName) {
        set(GROUP_NAME, groupName);
    }

    /**
     * Retrieves the Group name this App is listed in.
     * 
     * @return The Group name of this App.
     */
    public String getGroupName() {
        return get(GROUP_NAME);
    }

    private void setPipelineEligibility(PipelineEligibility eligibility) {
        set(PIPELINE_ELIGIBILITY, eligibility);
    }

    public PipelineEligibility getPipelineEligibility() {
        return get(PIPELINE_ELIGIBILITY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName();
    }

    @Override
    public JSONObject toJson() {
        JSONObject ret = super.toJson();

        ret.put(INTEGRATOR_NAME, new JSONString(getIntegratorsName()));
        ret.put(INTEGRATOR_EMAIL, new JSONString(getIntegratorsEmail()));

        long integration_date = 0;
        if (getIntegrationDate() != null) {
            integration_date = (DateTimeFormat
                    .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM)
                    .parse(getIntegrationDate())).getTime() / 1000;
        }
        ret.put(INTEGRATION_DATE, new JSONNumber(integration_date));

        ret.put(RATING, getFeedback().toJson());
        ret.put(FAVOURITE, JSONBoolean.getInstance(isUser_favourite()));
        ret.put(PUBLIC, JSONBoolean.getInstance(isPublic()));
        ret.put(WIKI_URL, new JSONString(getWikiUrl()));
        ret.put(DISABLED, JSONBoolean.getInstance(isDisabled()));

        return ret;
    }
}
