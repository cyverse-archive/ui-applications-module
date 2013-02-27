package org.iplantc.core.uiapps.client;

import org.iplantc.core.uicommons.client.CommonUIErrorStrings;

public interface CommonAppErrorStrings extends CommonUIErrorStrings {
    /**
     * Error message displayed when a wiki page for a tool cannot be created.
     * 
     * @param toolName name of the tool to make a page for
     * @return localized error string.
     */
    String cantCreateConfluencePage(String toolName);

    String newToolRequestError();

    String appRemoveFailure();

    String failToRetrieveApp();
}
