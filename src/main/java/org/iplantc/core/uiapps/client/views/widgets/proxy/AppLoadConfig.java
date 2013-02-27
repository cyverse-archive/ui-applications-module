package org.iplantc.core.uiapps.client.views.widgets.proxy;

import com.sencha.gxt.data.shared.loader.PagingLoadConfig;

public interface AppLoadConfig extends PagingLoadConfig {
    String getQuery();

    void setQuery(String query);
}