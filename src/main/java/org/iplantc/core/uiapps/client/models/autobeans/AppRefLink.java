package org.iplantc.core.uiapps.client.models.autobeans;

import org.iplantc.core.uicommons.client.models.HasId;

public interface AppRefLink extends HasId {

    void setId(String id);

    void setRefLink(String refLink);

    String getRefLink();

}
