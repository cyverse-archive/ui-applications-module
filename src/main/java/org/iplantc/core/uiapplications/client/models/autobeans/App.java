package org.iplantc.core.uiapplications.client.models.autobeans;

import java.util.Date;


import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface App {
    
    @PropertyName("is_favorite")
    boolean isFavorite();

    String getName();
    
    @PropertyName("wiki_url")
    String getWikiUrl();
    
    @PropertyName("integrator_name")
    String getIntegratorName();
    
    AppFeedback getRating();
    
    @PropertyName("integration_date")
    Date getIntegrationDate();
    
    @PropertyName("edited_date")
    Date getEditedDate();
    
    @PropertyName("is_public")
    boolean isPublic();
    
    @PropertyName("integrator_email")
    String getIntegratorEmail();
    
    String getId();
    
    boolean isDisabled();

    String getDescription();
    
    @PropertyName("group_id")
    String getGroupId();

    @PropertyName("pipeline_eligibility")
    PipelineEligibility getPipelineEligibility();

    @PropertyName("is_favorite")
    void setFavorite(boolean favorite);

    void setName(String name);

    @PropertyName("wiki_url")
    void setWikiUrl(String wikiUrl);

    @PropertyName("integrator_name")
    void setIntegratorName(String integratorName);

    @PropertyName("integration_date")
    void setIntegrationDate(Date integrationDate);

    @PropertyName("edited_date")
    void setEditedDate(Date editedDate);

    @PropertyName("is_public")
    void setPublic(boolean isPublic);

    @PropertyName("integrator_email")
    void setIntegratorEmail(String integratorEmail);

    void setId(String id);

    void setDisabled(boolean disabled);

    void setDescription(String description);

    @PropertyName("pipeline_eligibility")
    void setPipelineEligibility(PipelineEligibility pipelineEligibility);

    @PropertyName("group_id")
    void setGroupId(String groupId);

    @PropertyName("group_name")
    String getGroupName();

    @PropertyName("group_name")
    void setGroupName(String groupName);
}
