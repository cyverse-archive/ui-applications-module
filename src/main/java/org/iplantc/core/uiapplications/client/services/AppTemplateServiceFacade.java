package org.iplantc.core.uiapplications.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * An interface that provides access to remote services related to analyses.
 */
public interface AppTemplateServiceFacade {

    /**
     * Retrieves list of templates in the given group.
     * 
     * @param analysisGroupId unique identifier for the group to search in for analyses.
     * @param callback called when the RPC call is complete.
     */
    void getAnalysis(String analysisGroupId, AsyncCallback<String> callback);

    /**
     * Retrieves a hierarchy of all App Categories.
     * 
     * @param workspaceId
     * @param callback
     */
    void getAnalysisCategories(String workspaceId, AsyncCallback<String> callback);
}
