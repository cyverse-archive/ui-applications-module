package org.iplantc.core.uiapps.client.models.autobeans;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface AppAutoBeanFactory extends AutoBeanFactory {

    AutoBean<App> app();

    AutoBean<AppFeedback> appFeedback();

    AutoBean<PipelineEligibility> pipelineEligibility();

    AutoBean<AppDataObject> appDataObject();

    AutoBean<DataObject> dataObject();

    AutoBean<AppList> appList();

    AutoBean<AppGroup> appGroup();

    AutoBean<AppGroupList> appGroups();
}
