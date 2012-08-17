package org.iplantc.core.uiapplications.client.services;


import com.extjs.gxt.ui.client.Style.SortDir;
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
     * Retrieves a paged listing of templates in the given group.
     * @param analysisGroupId unique identifier for the group to search in for analyses.
     * @param limit
     * @param sortField
     * @param offset
     * @param sortDir
     * @param callback called when the RPC call is complete.
     */
    void getPagedAnalysis(String analysisGroupId, int limit, String sortField, int offset,
            SortDir sortDir, AsyncCallback<String> callback);

    /**
     * Retrieves a hierarchy of all App Categories.
     * 
     * @param workspaceId
     * @param callback
     */
    void getAnalysisCategories(String workspaceId, AsyncCallback<String> callback);

    /**
     * Searches for all active Apps with a name or description that contains the given search term.
     * 
     * @param search
     * @param callback called when the RPC call is complete.
     */
    void searchAnalysis(String search, AsyncCallback<String> callback);
}
