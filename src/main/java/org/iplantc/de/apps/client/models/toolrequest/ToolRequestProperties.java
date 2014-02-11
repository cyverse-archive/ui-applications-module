package org.iplantc.de.apps.client.models.toolrequest;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import java.util.Date;

public interface ToolRequestProperties extends PropertyAccess<ToolRequest> {

    ModelKeyProvider<ToolRequest> id();

    ValueProvider<ToolRequest, String> name();

    ValueProvider<ToolRequest, ToolRequestStatus> status();

    ValueProvider<ToolRequest, Date> dateSubmitted();

    ValueProvider<ToolRequest, Date> dateUpdated();

    ValueProvider<ToolRequest, String> updatedBy();

    ValueProvider<ToolRequest, String> version();

}
