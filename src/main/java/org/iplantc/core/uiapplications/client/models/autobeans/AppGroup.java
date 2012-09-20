package org.iplantc.core.uiapplications.client.models.autobeans;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface AppGroup {

    String getName();

    @PropertyName("template_count")
    int getAppCount();

    List<AppGroup> getGroups();

    @PropertyName("is_public")
    boolean isPublic();

    @PropertyName("workspace_id")
    String getWorkspaceId();

    String getId();

    String getDescription();

    void setName(String name);

    @PropertyName("template_count")
    void setTemplateCount(int templateCount);

    void setGroups(List<AppGroup> groups);

    @PropertyName("is_public")
    void setIsPublic(boolean isPublic);

    @PropertyName("workspace_id")
    void setWorkspaceId(String workspaceId);

    void setId(String id);

    void setDescription(String description);

}
