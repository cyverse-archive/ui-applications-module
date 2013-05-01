package org.iplantc.core.uiapps.client.gin;

import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.presenter.AppsViewPresenter;
import org.iplantc.core.uiapps.client.views.AppsView;
import org.iplantc.core.uiapps.client.views.AppsViewImpl;
import org.iplantc.core.uiapps.client.views.widgets.AppsViewToolbar;
import org.iplantc.core.uiapps.client.views.widgets.AppsViewToolbarImpl;

import com.google.gwt.inject.client.AbstractGinModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class AppsGinModule extends AbstractGinModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<TreeStore<AppGroup>>() {
        }).toProvider(AppGroupTreeStoreProvider.class).in(Singleton.class);

        bind(new TypeLiteral<Tree<AppGroup, String>>() {
        }).toProvider(AppGroupTreeProvider.class);

        bind(AppsView.class).to(AppsViewImpl.class);
        bind(AppsView.Presenter.class).to(AppsViewPresenter.class);
        bind(AppsViewToolbar.class).to(AppsViewToolbarImpl.class);
    }

}
