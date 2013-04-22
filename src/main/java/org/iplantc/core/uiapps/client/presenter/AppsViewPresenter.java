package org.iplantc.core.uiapps.client.presenter;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.Services;
import org.iplantc.core.uiapps.client.events.AppDeleteEvent;
import org.iplantc.core.uiapps.client.events.AppFavoritedEvent;
import org.iplantc.core.uiapps.client.events.AppFavoritedEventHander;
import org.iplantc.core.uiapps.client.events.AppGroupCountUpdateEvent;
import org.iplantc.core.uiapps.client.events.CreateNewAppEvent;
import org.iplantc.core.uiapps.client.events.CreateNewWorkflowEvent;
import org.iplantc.core.uiapps.client.events.EditAppEvent;
import org.iplantc.core.uiapps.client.events.EditWorkflowEvent;
import org.iplantc.core.uiapps.client.events.RunAppEvent;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.models.autobeans.AppList;
import org.iplantc.core.uiapps.client.presenter.proxy.AppGroupProxy;
import org.iplantc.core.uiapps.client.views.AppsView;
import org.iplantc.core.uiapps.client.views.dialogs.NewToolRequestDialog;
import org.iplantc.core.uiapps.client.views.dialogs.SubmitAppForPublicDialog;
import org.iplantc.core.uiapps.client.views.widgets.AppInfoView;
import org.iplantc.core.uiapps.client.views.widgets.AppsViewToolbar;
import org.iplantc.core.uiapps.client.views.widgets.events.AppSearchResultLoadEvent;
import org.iplantc.core.uiapps.client.views.widgets.events.AppSearchResultLoadEventHandler;
import org.iplantc.core.uiapps.client.views.widgets.proxy.AppSearchRpcProxy;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.CommonModelAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.presenter.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * The presenter for the AppsView.
 *
 * TODO JDS Document responsibility and intent of presenter.
 * <p>
 * Events fired from this presenter:
 * <ul>
 * <li> {@link AppDeleteEvent}</li>
 * <li> {@link AppGroupCountUpdateEvent}</li>
 * <li> {@link CreateNewAppEvent}</li>
 * <li> {@link CreateNewWorkflowEvent}</li>
 * <ul>
 *
 * @author jstroot
 *
 */
public class AppsViewPresenter implements Presenter, AppsView.Presenter {

    private final EventBus eventBus = EventBus.getInstance();
    private static String WORKSPACE;
    private static String USER_APPS_GROUP;
    private static String FAVORITES;

    protected final AppsView view;
    protected Builder builder;

    private final AppGroupProxy appGroupProxy;
    private AppsViewToolbar toolbar;

    private HasId desiredSelectedAppId;

    private final CommonModelAutoBeanFactory factory = GWT.create(CommonModelAutoBeanFactory.class);

    @Inject
    public AppsViewPresenter(final AppsView view, final AppGroupProxy proxy, AppsViewToolbar toolbar) {
        this.view = view;

        builder = new MyBuilder(this);

        // Initialize AppGroup TreeStore proxy and loader
        this.appGroupProxy = proxy;

        if (toolbar != null) {
            this.toolbar = toolbar;
            this.view.setNorthWidget(this.toolbar);
            this.toolbar.setPresenter(this);
        }

        this.view.setPresenter(this);

        initHandlers();
        initConstants();
    }

    private void initHandlers() {
        eventBus.addHandler(AppSearchResultLoadEvent.TYPE, new AppSearchResultLoadEventHandler() {
            @Override
            public void onLoad(AppSearchResultLoadEvent event) {
                if (event.getSource() == getAppSearchRpcProxy()) {
                    view.selectAppGroup(null);
                    view.setCenterPanelHeading(I18N.DISPLAY.searchAppResultsHeader(event.getSearchText()));
                    view.setApps(event.getResults());
                    view.unMaskCenterPanel();
                }
            }
        });
        eventBus.addHandler(AppFavoritedEvent.TYPE, new AppFavoritedEventHander() {

            @Override
            public void onAppFavorited(AppFavoritedEvent event) {
                AppGroup favAppGrp = view.findAppGroupByName(FAVORITES);
                if (favAppGrp != null) {
                    int tmp = event.isFavorite() ? 1 : -1;

                    view.updateAppGroupAppCount(favAppGrp, favAppGrp.getAppCount() + tmp);
                }
                // If the current app group is Workspace or Favorites, remove the app from the list.
                if (getSelectedAppGroup().getName().equalsIgnoreCase(WORKSPACE)
                        || getSelectedAppGroup().getName().equalsIgnoreCase(FAVORITES)) {
                    view.removeApp(view.findApp(event.getAppId()));
                }
            }
        });

    }

    private void initConstants() {
        DEProperties properties = DEProperties.getInstance();

        WORKSPACE = properties.getPrivateWorkspace();

        if (properties.getPrivateWorkspaceItems() != null) {
            JSONArray items = JSONParser.parseStrict(properties.getPrivateWorkspaceItems()).isArray();
            USER_APPS_GROUP = JsonUtil.getRawValueAsString(items.get(0));
            FAVORITES = JsonUtil.getRawValueAsString(items.get(1));
        }

    }

    /**
     * Sets a string which is a place holder for selection after a call to {@link #fetchApps(AppGroup)}
     *
     * @param selectedApp
     */
    private void setDesiredSelectedApp(HasId selectedApp) {
        this.desiredSelectedAppId = selectedApp;
    }

    private HasId getDesiredSelectedApp() {
        return desiredSelectedAppId;
    }

    @Override
    public void onAppGroupSelected(final AppGroup ag) {
        if (toolbar != null) {
        toolbar.setEditButtonEnabled(false);
        toolbar.setDeleteButtonEnabled(false);
        toolbar.setSubmitButtonEnabled(false);
        toolbar.setCopyButtonEnabled(false);
        toolbar.setAppInfoButtonEnabled(false);

        view.setCenterPanelHeading(ag.getName());
        fetchApps(ag);
        }
    }

    @Override
    public void onAppSelected(final App app) {
        if (app == null && toolbar != null) {
            toolbar.setEditButtonEnabled(false);
            toolbar.setDeleteButtonEnabled(false);
            toolbar.setSubmitButtonEnabled(false);
            toolbar.setCopyButtonEnabled(false);
            toolbar.setAppInfoButtonEnabled(false);
        } else if (app.isPublic()) {
            toolbar.setEditButtonEnabled(false);
            toolbar.setDeleteButtonEnabled(false);
            toolbar.setSubmitButtonEnabled(false);
            toolbar.setCopyButtonEnabled(true);
            toolbar.setAppInfoButtonEnabled(true);
        } else {
            toolbar.setEditButtonEnabled(true);
            toolbar.setDeleteButtonEnabled(true);
            toolbar.setSubmitButtonEnabled(true);
            toolbar.setCopyButtonEnabled(true);
            toolbar.setAppInfoButtonEnabled(true);
        }
    }

    /**
     * Retrieves the apps for the given group by updating and executing the list loader
     *
     * @param ag
     */
    protected void fetchApps(final AppGroup ag) {
        view.maskCenterPanel(I18N.DISPLAY.loadingMask());
        Services.APP_SERVICE.getApps(ag.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);
                AutoBean<AppList> bean = AutoBeanCodex.decode(factory, AppList.class, result);
                view.setApps(bean.as().getApps());

                if (getDesiredSelectedApp() != null) {
                    view.selectApp(getDesiredSelectedApp().getId());
                } else {
                    selectFirstApp();
                }
                setDesiredSelectedApp(null);
                view.unMaskCenterPanel();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
                view.unMaskCenterPanel();
            }
        });

    }

    protected void selectFirstApp() {
        view.selectFirstApp();
    }

    @Override
    public void go(HasOneWidget container, final HasId selectedAppGroup, final HasId selectedApp) {
        go(container);

        // Fetch AppGroups
        appGroupProxy.load(null, new AsyncCallback<List<AppGroup>>() {
            @Override
            public void onSuccess(List<AppGroup> result) {
                view.addAppGroups(null, result);
                view.expandAppGroups();
                // Select previous user selections
                if ((selectedAppGroup != null) && (selectedApp != null)) {
                    view.selectAppGroup(selectedAppGroup.getId());
                    AppsViewPresenter.this.setDesiredSelectedApp(selectedApp);
                    // view.selectApp(selectedApp.getId());
                } else {
                    view.selectFirstAppGroup();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO Add error message for the user.
                ErrorHandler.post(caught);
            }
        });
    }

    @Override
    public void go(final HasOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public App getSelectedApp() {
        return view.getSelectedApp();
    }

    @Override
    public AppGroup getSelectedAppGroup() {
        return view.getSelectedAppGroup();
    }

    @Override
    public void onAppInfoClicked() {
        showAppInfoWindow(getSelectedApp());
    }

    private void showAppInfoWindow(App app) {
        Dialog appInfoWin = new Dialog();
        appInfoWin.setModal(true);
        appInfoWin.setResizable(false);
        appInfoWin.setHeadingText(app.getName());
        appInfoWin.setPixelSize(450, 300);
        appInfoWin.add(new AppInfoView(app));
        appInfoWin.getButtonBar().clear();
        appInfoWin.show();
    }

    @Override
    public void onRequestToolClicked() {
        new NewToolRequestDialog().show();
    }

    @Override
    public void onCopyClicked() {
        final App selectedApp = getSelectedApp();
        Services.USER_APP_SERVICE.appExportable(selectedApp.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                JSONObject exportable = JsonUtil.getObject(result);

                if (JsonUtil.getBoolean(exportable, "can-export", false)) { //$NON-NLS-1$
                    if (selectedApp.getStepCount() > 1) {
                        copyWorkflow(selectedApp);
                    } else {
                        copyApp(selectedApp);
                    }
                } else {
                    ErrorHandler.post(JsonUtil.getString(exportable, "cause")); //$NON-NLS-1$
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO Add error message for the user.
                ErrorHandler.post(caught);
            }
        });

    }

    private void copyWorkflow(final App app) {
        Services.USER_APP_SERVICE.copyWorkflow(app.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                // Update the user's private apps group count.
                AppGroup userAppsGrp = view.findAppGroupByName(USER_APPS_GROUP);
                if (userAppsGrp != null) {
                    view.updateAppGroupAppCount(userAppsGrp, userAppsGrp.getAppCount() + 1);
                }

                // If the current app group is Workspace or the user's private apps, reload that group.
                AppGroup selectedAppGroup = getSelectedAppGroup();
                if (selectedAppGroup != null) {
                    String selectedGroupName = selectedAppGroup.getName();

                    if (selectedGroupName.equalsIgnoreCase(WORKSPACE)
                            || selectedGroupName.equalsIgnoreCase(USER_APPS_GROUP)) {
                        fetchApps(selectedAppGroup);
                    }
                }

                // Fire an EditWorkflowEvent for the new workflow copy.
                Splittable serviceWorkflowJson = StringQuoter.split(result);
                eventBus.fireEvent(new EditWorkflowEvent(app, serviceWorkflowJson));
            }

            @Override
            public void onFailure(Throwable caught) {
                // TODO Add error message for the user.
                ErrorHandler.post(caught);
            }
        });
    }

    private void copyApp(final App app) {
        Services.USER_APP_SERVICE.copyApp(app.getId(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Splittable splitResult = StringQuoter.createSplittable();
                StringQuoter.split(result).get("analysis_id").assign(splitResult, "id");

                AutoBean<HasId> hasId = AutoBeanCodex.decode(factory, HasId.class, splitResult);

                if (!hasId.as().getId().isEmpty()) {
                    AppGroup selectedAppGroup = getSelectedAppGroup();
                    if (selectedAppGroup != null) {
                        fetchApps(selectedAppGroup);
                    }
                    AppGroup appGroup = view.findAppGroup(app.getGroupId());
                    if (appGroup != null) {
                        view.updateAppGroupAppCount(appGroup, appGroup.getAppCount() + 1);
                    }
                    eventBus.fireEvent(new EditAppEvent(hasId.as()));
                    fetchTemplateAndFireEditAppEvent(hasId.as());
                }
            }


            @Override
            public void onFailure(Throwable caught) {
                // TODO Add error message for the user.
                ErrorHandler.post(caught);
            }
        });
    }

    private void fetchTemplateAndFireEditAppEvent(final HasId app) {
        Services.USER_APP_SERVICE.getTemplate(app.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Splittable legacyAppTemplate = StringQuoter.split(result);
                eventBus.fireEvent(new EditAppEvent(app, legacyAppTemplate));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.failToRetrieveApp(), caught);
            }
        });
    }

    private void fetchWorkflowAndFireEditEvent(final App app) {
        Services.USER_APP_SERVICE.editWorkflow(app.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Splittable serviceWorkflowJson = StringQuoter.split(result);
                eventBus.fireEvent(new EditWorkflowEvent(app, serviceWorkflowJson));
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.failToRetrieveApp(), caught);
            }
        });
    }

    @Override
    public void onDeleteClicked() {
        final App app = getSelectedApp();
        ConfirmMessageBox msgBox = new ConfirmMessageBox(I18N.DISPLAY.warning(),
                I18N.DISPLAY.appDeleteWarning());

        msgBox.addHideHandler(new HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                Dialog btn = (Dialog)event.getSource();
                String text = btn.getHideButton().getItemId();
                if (text.equals(PredefinedButton.YES.name())) {
                    Services.USER_APP_SERVICE.deleteAppFromWorkspace(UserInfo.getInstance()
                            .getUsername(), UserInfo.getInstance().getFullUsername(), app.getId(),
                            new AsyncCallback<String>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    ErrorHandler.post(I18N.ERROR.appRemoveFailure(), caught);
                                }

                                @Override
                                public void onSuccess(String result) {
                                    // Remove from visible list and update AppGroup app counts
                                    view.removeApp(app);
                                    AppGroup appGroup = view.findAppGroup(app.getGroupId());
                                    if (appGroup != null) {
                                        view.updateAppGroupAppCount(appGroup, appGroup.getAppCount() - 1);
                                    }

                                    eventBus.fireEvent(new AppDeleteEvent(app.getId()));
                                }
                            });
                }

            }
        });
        msgBox.show();

    }

    @Override
    public void submitClicked() {
        App selectedApp = getSelectedApp();
        SubmitAppForPublicDialog dialog = new SubmitAppForPublicDialog(selectedApp);
        dialog.show();

    }

    @Override
    public void createNewAppClicked() {
        eventBus.fireEvent(new CreateNewAppEvent());
    }

    @Override
    public void createWorkflowClicked() {
        eventBus.fireEvent(new CreateNewWorkflowEvent());
    }

    @Override
    public void onAppInfoClick(App app) {
        showAppInfoWindow(app);
    }

    @Override
    public void onEditClicked() {
        App selectedApp = getSelectedApp();

        if (selectedApp.getStepCount() > 1) {
            fetchWorkflowAndFireEditEvent(selectedApp);
        } else {
            fetchTemplateAndFireEditAppEvent(selectedApp);
        }
    }

    @Override
    public AppsViewToolbar getToolbar() {
        return toolbar;
    }

    @Override
    public Builder builder() {
        return builder;
    }

    private class MyBuilder implements Builder {

        private final AppsViewPresenter presenter;

        MyBuilder(AppsViewPresenter presenter) {
            this.presenter = presenter;
        }

        @Override
        public void go(HasOneWidget container) {
            presenter.go(container);
        }

        @Override
        public void go(HasOneWidget container, AppGroup selectedAppGroup, App selectedApp) {
            presenter.go(container, selectedAppGroup, selectedApp);
        }

        @Override
        public Builder hideToolbarButtonCreate() {
            presenter.getToolbar().setCreateButtonVisible(false);
            return this;
        }

        @Override
        public Builder hideToolbarButtonCopy() {
            presenter.getToolbar().setCopyButtonVisible(false);
            return this;
        }

        @Override
        public Builder hideToolbarButtonEdit() {
            presenter.getToolbar().setEditButtonVisible(false);
            return this;
        }

        @Override
        public Builder hideToolbarButtonDelete() {
            presenter.getToolbar().setDeleteButtonVisible(false);
            return this;
        }

        @Override
        public Builder hideToolbarButtonSubmit() {
            presenter.getToolbar().setSubmitButtonVisible(false);
            return this;
        }

        @Override
        public Builder hideToolbarButtonRequestTool() {
            presenter.getToolbar().setRequestToolButtonVisible(false);
            return this;
        }

    }

    @Override
    public Grid<App> getAppsGrid() {
        return view.getAppsGrid();
    }

    @Override
    public void onAppRunClick() {
        RunAppEvent event = new RunAppEvent(getSelectedApp());
        EventBus.getInstance().fireEvent(event);
    }

    public AppSearchRpcProxy getAppSearchRpcProxy() {
        return toolbar.getAppSearchRpcProxy();

    }

    @Override
    public void onAppNameSelected(final App app) {
        EventBus.getInstance().fireEvent(new RunAppEvent(app));
    }
}
