package org.iplantc.core.uiapplications.client.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uicommons.client.util.DateParser;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Random;

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
    public static final String DISABLED = "disabled";

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

        long timestamp = 0;
        Number num = JsonUtil.getNumber(json, INTEGRATION_DATE);
        if (num != null) {
            timestamp = num.longValue();
        }
        setIntegrationDate(DateParser.parseDate(timestamp));

        setUser_favourite(JsonUtil.getBoolean(json, FAVOURITE, false));
        setPublic(JsonUtil.getBoolean(json, PUBLIC, false));

        setAnalysisFeedback(new AnalysisFeedback(JsonUtil.getObject(json, RATING)));

        set(WIKI_URL, JsonUtil.getString(json, WIKI_URL));

        setDeployedComponents(parseDeployedComponents(JsonUtil.getArray(json, DEPLOYED_COMPONENTS)));

        setSuggestedCategories(parseSuggestedCategories(JsonUtil.getArray(json, SUGGESTED_CATEGORIES)));

        // TODO: get json and parse boolean. randomize for now
        setDisabled(Random.nextBoolean());
    }

    private List<DeployedComponent> parseDeployedComponents(JSONArray deployedComponents) {
        List<DeployedComponent> ret = null;

        if (deployedComponents != null && deployedComponents.size() > 0) {
            ret = new ArrayList<DeployedComponent>();

            for (int i = 0,size = deployedComponents.size(); i < size; i++) {
                ret.add(new DeployedComponent(JsonUtil.getObjectAt(deployedComponents, i)));
            }
        }

        return ret;
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
    public void setIntegrationDate(Date integration_date) {
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
    public Date getIntegrationDate() {
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
     * Sets the list of deployed components used in this App.
     * 
     * @param deployedComponents
     */
    public void setDeployedComponents(List<DeployedComponent> deployedComponents) {
        set(DEPLOYED_COMPONENTS, deployedComponents);
    }

    /**
     * Retrieves the list of deployed components used in this App.
     * 
     * @return A list of DeployedComponent models.
     */
    public List<DeployedComponent> getDeployedComponents() {
        return get(DEPLOYED_COMPONENTS);
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
            integration_date = getIntegrationDate().getTime() / 1000;
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
