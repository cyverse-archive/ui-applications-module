package org.iplantc.core.uiapps.client.models.toolrequest;

import java.util.Date;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface ToolRequestProperties extends PropertyAccess<ToolRequest> {

    ModelKeyProvider<ToolRequest> id();

    ValueProvider<ToolRequest, String> name();

    ValueProvider<ToolRequest, ToolRequestStatus> status();

    ValueProvider<ToolRequest, Date> dateSubmitted();

    ValueProvider<ToolRequest, Date> dateUpdated();

    ValueProvider<ToolRequest, String> updatedBy();

    ValueProvider<ToolRequest, String> version();

}
