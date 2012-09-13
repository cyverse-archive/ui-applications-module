package org.iplantc.core.uiapplications.client.views.widgets.proxy;

import com.sencha.gxt.data.shared.loader.PagingLoadConfig;

public interface AnalysisLoadConfig extends PagingLoadConfig {
    String getQuery();

    void setQuery(String query);
}