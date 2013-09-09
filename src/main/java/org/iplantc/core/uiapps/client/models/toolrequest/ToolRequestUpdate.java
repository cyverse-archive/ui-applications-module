package org.iplantc.core.uiapps.client.models.toolrequest;

import org.iplantc.core.uicommons.client.models.HasId;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * https://github.com/iPlantCollaborativeOpenSource/metadactyl-clj/blob/
 * dfc0b110e73a40229762033ffeb267a9b10373bc
 * /doc/endpoints/app-metadata/tool-requests.md#updating-the-status-of-a-tool-request
 * 
 * Reference the link above for details.
 * 
 * @author jstroot
 * 
 */
public interface ToolRequestUpdate extends HasId {

    @PropertyName("uuid")
    String getId();

    ToolRequestStatus getStatus();

    @PropertyName("username")
    String getUserName();

    String getComments();
}
