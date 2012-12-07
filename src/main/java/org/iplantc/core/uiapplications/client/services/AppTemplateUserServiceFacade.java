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
     * Posts the current user's rating of the given analysis, and adds a comment to the analysis' wiki
     * page. The comment ID generated by Confluence is returned via the callback if the call was
     * successful.
     * 
     * @param analysisId
     * @param rating
     * @param appName name of the app (name of the confluence page to add the comment to)
     * @param comment the comment text
     * @param authorEmail the app author's email address for sending feedback
     * @param callback
     */
    void rateAnalysis(String analysisId, int rating, String appName, String comment,
            final String authorEmail, AsyncCallback<String> callback);

    /**
     * Posts the current user's rating of the given analysis, and changes the comment on the wiki page.
     * 
     * @param analysisId
     * @param rating
     * @param appName name of the app (name of the confluence page that contains the comment)
     * @param commentId Confluence ID of the comment associated with the rating
     * @param comment the comment text
     * @param authorEmail the app author's email address for sending feedback
     * @param callback
     */
    void updateRating(String analysisId, int rating, String appName, Long commentId, String comment,
            String authorEmail, AsyncCallback<String> callback);

    /**
     * Deletes an existing rating for the current user. If a non-null commentId is provided, the comment
     * on the wiki page is also deleted. If the user hasn't rated the application, nothing happens.
     * 
     * @param analysisId
     * @param toolName name of the app (name of the confluence page that contains the comment)
     * @param commentId Confluence comment ID
     * @param callback
     */
    void deleteRating(String analysisId, String toolName, Long commentId,
            AsyncCallback<String> callback);

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