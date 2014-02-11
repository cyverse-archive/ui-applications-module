package org.iplantc.de.apps.client.models.autobeans;

import org.iplantc.de.client.models.HasId;

public interface AppRefLink extends HasId {

    void setId(String id);

    void setRefLink(String refLink);

    String getRefLink();

}
