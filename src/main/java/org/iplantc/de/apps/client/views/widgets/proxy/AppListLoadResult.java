package org.iplantc.de.apps.client.views.widgets.proxy;

import org.iplantc.de.apps.client.models.autobeans.App;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import com.sencha.gxt.data.shared.loader.PagingLoadResult;

import java.util.List;

public interface AppListLoadResult extends PagingLoadResult<App> {
    void setData(List<App> data);

    @Override
    @PropertyName("start")
    public int getOffset();

    @Override
    @PropertyName("start")
    public void setOffset(int offset);

}