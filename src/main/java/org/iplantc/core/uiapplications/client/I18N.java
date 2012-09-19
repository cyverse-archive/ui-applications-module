package org.iplantc.core.uiapplications.client;

import com.google.gwt.core.client.GWT;

public class I18N {
    /** Strings displayed in the UI */
    public static final CommonAppDisplayStrings DISPLAY = GWT.create(CommonAppDisplayStrings.class);
    public static final CommonAppErrorStrings ERROR = GWT.create(CommonAppErrorStrings.class);
}
