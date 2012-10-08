package org.iplantc.core.uiapplications.client.views;

import java.util.List;

import org.iplantc.core.uiapplications.client.events.AppSelectedEvent;
import org.iplantc.core.uiapplications.client.events.handlers.AppSelectedEventHandler;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppGroup;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.images.Resources;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.CellClickEvent.CellClickHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeAppearance;

/**
 * 
 * @author jstroot
 * 
 */
public class AppsViewImpl implements AppsView {
    /**
     * FIXME CORE-2992: Add an ID to the Categories panel collapse tool to assist QA.
     */
    private static String WEST_COLLAPSE_BTN_ID = "idCategoryCollapseBtn"; //$NON-NLS-1$
    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("AppsView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, AppsViewImpl> {
    }

    private Presenter presenter;

    @UiField
    Tree<AppGroup, String> tree;

    @UiField
    TreeStore<AppGroup> treeStore;

    @UiField
    Grid<App> grid;

    @UiField
    GridView<App> gridView;

    @UiField
    ListStore<App> listStore;

    @UiField
    ColumnModel<App> cm;

    @UiField
    BorderLayoutContainer con;

    @UiField
    ContentPanel westPanel;
    @UiField
    ContentPanel centerPanel;
    @UiField
    ContentPanel eastPanel;

    @UiField
    BorderLayoutData northData;
    @UiField
    BorderLayoutData eastData;

    private final Widget widget;

    public AppsViewImpl() {
        initHandlers();
        this.widget = uiBinder.createAndBindUi(this);
        grid.addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent arg0) {
                // TODO Auto-generated method stub

            }
        });

        grid.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<App>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<App> event) {
                if ((event.getSelection() != null) && !event.getSelection().isEmpty()) {
                    presenter.onAppSelected(event.getSelection().get(0));
                }
            }
        });

        tree.getSelectionModel().addSelectionChangedHandler(
                new SelectionChangedHandler<AppGroup>() {
                    @Override
                    public void onSelectionChanged(SelectionChangedEvent<AppGroup> event) {
                        if ((event.getSelection() != null) && !event.getSelection().isEmpty()) {
                            presenter.onAppGroupSelected(event.getSelection().get(0));
                    }
                    }
                });
        setTreeIcons();
        new QuickTip(grid).getToolTipConfig().setTrackMouse(true);
    }

    @UiFactory
    TreeStore<AppGroup> createTreeStore() {
        return new TreeStore<AppGroup>(new ModelKeyProvider<AppGroup>() {
            @Override
            public String getKey(AppGroup item) {
                return item.getId();
            }

        });
    }

    @UiFactory
    ListStore<App> createListStore() {
        return new ListStore<App>(new ModelKeyProvider<App>() {
            @Override
            public String getKey(App item) {
                return item.getId();
            }

        });
    }

    @UiFactory
    ColumnModel<App> createColumnModel() {
        return new AppColumnModel();
    }

    private void initHandlers() {
        EventBus.getInstance().addHandler(AppSelectedEvent.TYPE, new AppSelectedEventHandler() {

            @Override
            public void onSelection(AppSelectedEvent event) {
                Cell cell = null;
                // Determine if the event source is our cell
                for (ColumnConfig<App, ?> col : grid.getColumnModel().getColumns()) {
                    if (col.getCell() == event.getSource()) {
                        cell = col.getCell();
                        break;
                    }
                }
                if (cell == null)
                    return;

                // if (event.getSource() == grid.getColumnModel().getCell(1)) {
                    // Retrieve app
                    App app = listStore.findModelWithKey(event.getAppId());
                    if (app != null) {
                        presenter.onAppNameSelected(app);
                    } else {
                        // TODO JDS Determine what error to throw here.
                    }
                // }
            }
        });

    }

    /**
     * FIXME JDS This needs to be implemented in an {@link TreeAppearance}
     */
    private void setTreeIcons() {
        com.sencha.gxt.widget.core.client.tree.TreeStyle style = tree.getStyle();
        style.setNodeCloseIcon(Resources.ICONS.category());
        style.setNodeOpenIcon(Resources.ICONS.category_open());
        style.setLeafIcon(Resources.ICONS.subCategory());
    }

    @UiFactory
    public ValueProvider<AppGroup, String> createValueProvider() {
        return new ValueProvider<AppGroup, String>() {

            @Override
            public String getValue(AppGroup object) {
                return object.getName() + " (" + object.getAppCount() + ")";
            }

            @Override
            public void setValue(AppGroup object, String value) {
            }

            @Override
            public String getPath() {
                return "name";
            }
        };
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ListStore<App> getListStore() {
        return listStore;
    }

    @Override
    public TreeStore<AppGroup> getTreeStore() {
        return treeStore;
    }

    @Override
    public void setTreeLoader(final TreeLoader<AppGroup> treeLoader) {
        this.tree.setLoader(treeLoader);
    }

    @Override
    public void setCenterPanelHeading(final String name) {
        centerPanel.setHeadingText(name);
    }

    @Override
    public void maskCenterPanel(final String loadingMask) {
        centerPanel.mask(loadingMask);
    }

    @Override
    public void unMaskCenterPanel() {
        centerPanel.unmask();
    }

    @Override
    public void maskWestPanel(String loadingMask) {
        westPanel.mask(loadingMask);
    }

    @Override
    public void unMaskWestPanel() {
        westPanel.unmask();
    }

    @Override
    public void setListLoader(ListLoader<ListLoadConfig, ListLoadResult<App>> listLoader) {
        grid.setLoader(listLoader);
    }

    @Override
    public void selectApp(String appId) {
        App app = listStore.findModelWithKey(appId);
        if (app != null) {
            grid.getSelectionModel().select(app, false);
        }
    }

    @Override
    public void selectAppGroup(String appGroupId) {
        AppGroup ag = treeStore.findModelWithKey(appGroupId);
        if (ag != null) {
            tree.getSelectionModel().select(ag, false);
            tree.scrollIntoView(ag);
            // Set heading
            setCenterPanelHeading(ag.getName());
        }
    }

    @Override
    public App getSelectedApp() {
        return grid.getSelectionModel().getSelectedItem();
    }

    @Override
    public AppGroup getSelectedAppGroup() {
        return tree.getSelectionModel().getSelectedItem();
    }

    @Override
    public void setApps(final List<App> apps) {
        listStore.clear();
        listStore.addAll(apps);
    }


	@Override
    public void setNorthWidget(IsWidget widget) {
        northData.setHidden(false);
        con.setNorthWidget(widget, northData);
	}

    @Override
    public void setEastWidget(IsWidget widget) {
        eastData.setHidden(false);
        con.setEastWidget(widget, eastData);
    }

    @Override
    public void selectFirstApp() {
        grid.getSelectionModel().select(0, false);
    }

    @Override
    public void selectFirstAppGroup() {
        AppGroup ag = treeStore.getRootItems().get(0);
        tree.getSelectionModel().select(ag, false);
        tree.scrollIntoView(ag);
    }

    @Override
    public void removeApp(App app) {
        grid.getSelectionModel().deselectAll();
        presenter.onAppSelected(null);
        listStore.remove(app);
    }

    @Override
    public void deSelectAllAppGroups() {
        tree.getSelectionModel().deselectAll();
    }

    @Override
    public void updateAppGroup(AppGroup appGroup) {
        treeStore.update(appGroup);
    }

    @Override
    public AppGroup findAppGroup(String id) {
        return treeStore.findModelWithKey(id);
    }

    @Override
    public void updateAppGroupAppCount(AppGroup appGroup, int newCount) {
        int difference = appGroup.getAppCount() - newCount;

        while (appGroup != null) {
            appGroup.setAppCount(appGroup.getAppCount() - difference);
            updateAppGroup(appGroup);
            appGroup = treeStore.getParent(appGroup);
        }

    }

    @Override
    public App findApp(String appId) {
        return listStore.findModelWithKey(appId);
    }

}
