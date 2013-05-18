package org.iplantc.core.uiapps.client;

import org.iplantc.core.uiapps.client.services.AppServiceFacade;
import org.iplantc.core.uiapps.client.services.AppUserServiceFacade;

import com.google.gwt.core.client.GWT;

/**
 * A class which contains static references to system services.
 * 
 * @author jstroot
 * 
 */
public class Services {
    public static AppServiceFacade APP_SERVICE = GWT.create(AppServiceFacade.class);
    public static AppUserServiceFacade USER_APP_SERVICE = GWT
            .create(AppUserServiceFacade.class);

}
