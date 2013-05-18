package org.iplantc.core.uiapps.client.gin;

import org.iplantc.core.uiapps.client.views.AppsView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(AppsGinModule.class)
public interface AppsInjector extends Ginjector {

    public static final AppsInjector INSTANCE = GWT.create(AppsInjector.class);

    public AppsView.Presenter getAppsViewPresenter();


}
