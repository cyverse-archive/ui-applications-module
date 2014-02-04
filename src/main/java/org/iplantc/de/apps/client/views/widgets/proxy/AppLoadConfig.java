package org.iplantc.de.apps.client.views.widgets.proxy;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

public interface AppLoadConfig extends FilterPagingLoadConfig {
    String getQuery();

    void setQuery(String query);
}