package org.iplantc.core.uiapps.client.views.widgets.proxy;

import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

public interface AppLoadConfig extends FilterPagingLoadConfig {
    String getQuery();

    void setQuery(String query);
}