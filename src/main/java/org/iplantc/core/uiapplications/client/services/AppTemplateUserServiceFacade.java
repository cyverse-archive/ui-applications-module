package org.iplantc.core.uiapplications.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppTemplateUserServiceFacade extends AppTemplateServiceFacade {

    /**
     * @param workspaceId
     * @param analysisId
     * @param fav
     * @param callback
     */
    void favoriteAnalysis(String workspaceId, String analysisId, boolean fav,
            AsyncCallback<String> callback);

    /**
     * Posts the current user's rating of the given analysis.
     * 
     * @param analysisId
     * @param rating
     * @param callback
     */
    void rateAnalysis(String analysisId, int rating, AsyncCallback<String> callback);

    /**
     * Deletes an existing rating for the current user. If the user hasn't rated the application, nothing
     * happens.
     * 
     * @param analysisId
     * @param callback
     */
    void deleteRating(String analysisId, AsyncCallback<String> callback);

    /**
     * Retrieves the name and a list of inputs and outputs for the given analysis. The response JSON will
     * be formatted as follows:
     * 
     * <pre>
     * {
     *     "id": "analysid-id",
     *     "name": "analysis-name",
     *     "inputs": [{...property-details...},...],
     *     "outputs": [{...property-details...},...]
     * }
     * </pre>
     * 
     * @param analysisId unique identifier of the analysis.
     * @param callback called when the RPC call is complete.
     */
    void getDataObjectsForAnalysis(String analysisId, AsyncCallback<String> callback);

    /**
     * Publishes a workflow / pipeline to user's workspace
     * 
     * @param body post body json
     * @param callback called when the RPC call is complete
     */
    void publishWorkflow(String body, AsyncCallback<String> callback);
}
