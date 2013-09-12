package org.iplantc.core.uiapps.client.models.toolrequest;

import java.util.Date;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * A Status History AutoBean for PayloadToolRequest.
 * 
 * @author psarando
 * 
 */
public interface ToolRequestHistory {

    ToolRequestStatus getStatus();

    @PropertyName("updated_by")
    String getUpdatedBy();

    @PropertyName("status_date")
    Date getStatusDate();

    String getComments();
}
