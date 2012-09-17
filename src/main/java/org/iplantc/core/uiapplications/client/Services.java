package org.iplantc.core.uiapplications.client;

import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uiapplications.client.services.AppTemplateUserServiceFacade;

import com.google.gwt.core.client.GWT;

/**
 * A class which contains static references to system services.
 * 
 * @author jstroot
 * 
 */
public class Services {
    public static AppTemplateServiceFacade TEMPLATE_SERVICE = GWT.create(AppTemplateServiceFacade.class);
    public static AppTemplateUserServiceFacade USER_TEMPLATE_SERVICE = GWT
            .create(AppTemplateUserServiceFacade.class);

}
