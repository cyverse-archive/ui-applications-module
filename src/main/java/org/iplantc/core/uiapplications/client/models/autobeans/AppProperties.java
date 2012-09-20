package org.iplantc.core.uiapplications.client.models.autobeans;


import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface AppProperties extends PropertyAccess<App>{

    ModelKeyProvider<App> id();

    LabelProvider<App> name();

    ValueProvider<App, String> integratorName();

    ValueProvider<App, AppFeedback> rating();

}
