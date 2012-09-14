package org.iplantc.core.uiapplications.client.views.widgets.proxy;

import java.util.List;

import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface AnalysisListLoadResult extends PagingLoadResult<Analysis> {
    void setData(List<Analysis> data);

    @Override
    @PropertyName("start")
    public int getOffset();

    @Override
    @PropertyName("start")
    public void setOffset(int offset);

}