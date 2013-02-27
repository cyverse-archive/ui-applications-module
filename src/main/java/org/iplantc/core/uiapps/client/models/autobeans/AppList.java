package org.iplantc.core.uiapps.client.models.autobeans;

import java.util.List;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * This is a convenience class for retrieving JSON arrays of apps
 * 
 * @author jstroot
 * 
 */
public interface AppList {
    @PropertyName("templates")
    List<App> getApps();
}
