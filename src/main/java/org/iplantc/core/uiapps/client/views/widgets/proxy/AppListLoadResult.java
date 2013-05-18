package org.iplantc.core.uiapps.client.views.widgets.proxy;

import java.util.List;

import org.iplantc.core.uiapps.client.models.autobeans.App;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface AppListLoadResult extends PagingLoadResult<App> {
    void setData(List<App> data);

    @Override
    @PropertyName("start")
    public int getOffset();

    @Override
    @PropertyName("start")
    public void setOffset(int offset);

}