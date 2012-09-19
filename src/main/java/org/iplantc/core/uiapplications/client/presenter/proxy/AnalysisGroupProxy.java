package org.iplantc.core.uiapplications.client.presenter.proxy;

import java.util.List;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisAutoBeanFactory;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisGroupList;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.models.UserInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.data.client.loader.RpcProxy;

/**
 * @author jstroot
 * 
 */
public class AnalysisGroupProxy extends RpcProxy<AnalysisGroup, List<AnalysisGroup>> {

    private final AnalysisAutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);

    public AnalysisGroupProxy() {
    }

    @Override
    public void load(AnalysisGroup loadConfig, final AsyncCallback<List<AnalysisGroup>> callback) {
        Services.TEMPLATE_SERVICE.getAnalysisCategories(UserInfo.getInstance().getWorkspaceId(),
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        AutoBean<AnalysisGroupList> bean = AutoBeanCodex.decode(factory,
                                AnalysisGroupList.class, result);
                        AnalysisGroupList as = bean.as();
                        List<AnalysisGroup> groups = as.getGroups();
                        callback.onSuccess(groups);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(I18N.ERROR.analysisGroupsLoadFailure(), caught);
                        callback.onFailure(caught);
                    }
                });

    }

}
