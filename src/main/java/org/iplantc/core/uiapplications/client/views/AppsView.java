package org.iplantc.core.uiapplications.client.views;

import java.util.List;

import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppGroup;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.grid.Grid;

public interface AppsView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {
        void onAppSelected(final App app);

        void onAppGroupSelected(final AppGroup ag);

        App getSelectedApp();

        AppGroup getSelectedAppGroup();

        void onAppNameSelected(App app);

        void go(HasOneWidget container, HasId selectedAppGroup, HasId selectedApp);
    }

    void setPresenter(final Presenter presenter);

    ListStore<App> getListStore();

    TreeStore<AppGroup> getTreeStore();

    void setListLoader(final ListLoader<ListLoadConfig, ListLoadResult<App>> listLoader);

    void setTreeLoader(final TreeLoader<AppGroup> treeLoader);

    void setCenterPanelHeading(final String name);

    void maskCenterPanel(final String loadingMask);

    void unMaskCenterPanel();

    void maskWestPanel(String loadingMask);

    void unMaskWestPanel();

    void selectApp(String appId);

    void selectAppGroup(String appGroupId);

    App getSelectedApp();

    AppGroup getSelectedAppGroup();

    void setApps(List<App> apps);

    void setNorthWidget(IsWidget widget);

    void setEastWidget(IsWidget widget);

    void selectFirstApp();

    void selectFirstAppGroup();

    void removeApp(App app);

    void deSelectAllAppGroups();

    void updateAppGroup(AppGroup appGroup);

    AppGroup findAppGroup(String id);

    void updateAppGroupAppCount(AppGroup appGroup, int newCount);

    App findApp(String appId);

    void onAppHyperlinkSelected(App app);

    Grid<App> getAppsGrid();
}
