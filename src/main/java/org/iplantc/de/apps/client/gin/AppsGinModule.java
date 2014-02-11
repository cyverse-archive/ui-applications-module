package org.iplantc.de.apps.client.gin;

import org.iplantc.de.apps.client.models.autobeans.AppGroup;
import org.iplantc.de.apps.client.presenter.AppsViewPresenter;
import org.iplantc.de.apps.client.presenter.SubmitAppForPublicPresenter;
import org.iplantc.de.apps.client.views.AppsView;
import org.iplantc.de.apps.client.views.AppsViewImpl;
import org.iplantc.de.apps.client.views.SubmitAppForPublicUseView;
import org.iplantc.de.apps.client.views.SubmitAppForPublicUseViewImpl;
import org.iplantc.de.apps.client.views.widgets.AppsViewToolbar;
import org.iplantc.de.apps.client.views.widgets.AppsViewToolbarImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.TypeLiteral;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class AppsGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<TreeStore<AppGroup>>() {}).toProvider(AppGroupTreeStoreProvider.class);

        bind(new TypeLiteral<Tree<AppGroup, String>>() {
        }).toProvider(AppGroupTreeProvider.class);

        bind(AppsView.class).to(AppsViewImpl.class);
        bind(AppsView.Presenter.class).to(AppsViewPresenter.class);
        bind(AppsViewToolbar.class).to(AppsViewToolbarImpl.class);
        bind(SubmitAppForPublicUseView.class).to(SubmitAppForPublicUseViewImpl.class);
        bind(SubmitAppForPublicUseView.Presenter.class).to(SubmitAppForPublicPresenter.class);

    }

}
