package org.iplantc.de.apps.client.models.autobeans;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import java.util.List;

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
