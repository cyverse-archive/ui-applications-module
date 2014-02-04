package org.iplantc.de.apps.client;

import org.iplantc.core.uicommons.client.DEClientConstants;

import com.google.gwt.core.client.GWT;

/**
 * Static access to client constants.
 * 
 * @author hariolf
 * 
 */
public class Constants {
    /** CommonConstants, auto-populated from .properties by GWT */
    public static final DEClientConstants CLIENT = GWT.create(DEClientConstants.class);
}
