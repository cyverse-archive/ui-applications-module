package org.iplantc.core.uiapps.client.models.toolrequest;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface ToolRequestAutoBeanFactory extends AutoBeanFactory {
    
    AutoBean<ToolRequest> toolRequest();
    
    AutoBean<ToolRequestDetails> details();
    
    AutoBean<ToolRequestHistory> history();
    
    

}
