package org.iplantc.core.uiapplications.client;

import org.iplantc.core.uicommons.client.CommonUIErrorStrings;

import com.google.gwt.core.client.GWT;

public class I18N {
    /** Strings displayed in the UI */
    public static final CommonAppDisplayStrings DISPLAY = (CommonAppDisplayStrings)GWT
            .create(CommonAppDisplayStrings.class);
    public static final CommonUIErrorStrings ERROR = (CommonUIErrorStrings)GWT
            .create(CommonUIErrorStrings.class);
}
