package org.iplantc.core.uiapplications.client;

import com.google.gwt.core.client.GWT;

/**
 * Static access to client constants.
 * 
 * @author hariolf
 * 
 */
public class Constants {
    /** CommonConstants, auto-populated from .properties by GWT */
    public static final CommonAppConstants CLIENT = (CommonAppConstants)GWT
            .create(CommonAppConstants.class);
}
