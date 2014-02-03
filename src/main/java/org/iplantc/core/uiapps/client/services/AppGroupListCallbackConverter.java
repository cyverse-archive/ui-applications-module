package org.iplantc.core.uiapps.client.services;

import java.util.List;

import org.iplantc.de.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroupList;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.services.AsyncCallbackConverter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class AppGroupListCallbackConverter extends AsyncCallbackConverter<String, List<AppGroup>> {

    private final AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);

    public AppGroupListCallbackConverter(AsyncCallback<List<AppGroup>> callback) {
        super(callback);
    }

    @Override
    protected List<AppGroup> convertFrom(String object) {
        AutoBean<AppGroupList> bean = AutoBeanCodex.decode(factory, AppGroupList.class, object);
        AppGroupList as = bean.as();
        List<AppGroup> groups = as.getGroups();
        return groups;
    }

    @Override
    public void onFailure(Throwable caught) {
        ErrorHandler.post(I18N.ERROR.analysisGroupsLoadFailure(), caught);
        super.onFailure(caught);
    }

}
