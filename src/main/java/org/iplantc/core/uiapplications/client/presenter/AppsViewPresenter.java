package org.iplantc.core.uiapplications.client.presenter;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.Constants;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.events.AppDeleteEvent;
import org.iplantc.core.uiapplications.client.events.AppFavoritedEvent;
import org.iplantc.core.uiapplications.client.events.AppFavoritedEventHander;
import org.iplantc.core.uiapplications.client.events.AppGroupCountUpdateEvent;
import org.iplantc.core.uiapplications.client.events.AppGroupCountUpdateEvent.AppGroupType;
import org.iplantc.core.uiapplications.client.events.CreateNewAppEvent;
import org.iplantc.core.uiapplications.client.events.CreateNewWorkflowEvent;
import org.iplantc.core.uiapplications.client.events.TemplateLoadEvent;
import org.iplantc.core.uiapplications.client.events.TemplateLoadEvent.MODE;
import org.iplantc.core.uiapplications.client.models.CatalogWindowConfig;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapplications.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapplications.client.models.autobeans.AppList;
import org.iplantc.core.uiapplications.client.presenter.proxy.AppGroupProxy;
import org.iplantc.core.uiapplications.client.views.AppsView;
import org.iplantc.core.uiapplications.client.views.panels.SubmitAppForPublicUsePanel;
import org.iplantc.core.uiapplications.client.views.widgets.AppInfoView;
import org.iplantc.core.uiapplications.client.views.widgets.AppsViewToolbar;
import org.iplantc.core.uiapplications.client.views.widgets.AppsViewToolbarImpl;
import org.iplantc.core.uiapplications.client.views.widgets.events.AppSearch3ResultLoadEvent;
import org.iplantc.core.uiapplications.client.views.widgets.events.AppSearch3ResultLoadEventHandler;
import org.iplantc.core.uiapplications.client.views.widgets.events.AppSearch3ResultSelectedEvent;
import org.iplantc.core.uiapplications.client.views.widgets.events.AppSearch3ResultSelectedEventHandler;
import org.iplantc.core.uiapplications.client.views.windows.NewToolRequestWindow;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.events.UserEvent;
import org.iplantc.core.uicommons.client.models.DEProperties;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.presenter.Presenter;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

/**
 * The presenter for the AppsView.
 * 
 * 
 * TODO JDS Document responsibility and intent of presenter.
 * 
 * 
 * @author jstroot
 * 
 */
public class AppsViewPresenter implements Presenter, AppsView.Presenter, AppsViewToolbar.Presenter {

    public static String WORKSPACE;
    public static String FAVORITES;
    public static String APPLICATIONS_UNDER_DEVLOPMENT;
    public static String BETA_GROUP_ID;

    protected final AppsView view;

    private final AppGroupProxy appGroupProxy;
    private CatalogWindowConfig config;
    private final AppsViewToolbar toolbar;

    private String desiredSelectedAppId;

    protected AppsViewPresenter(final AppsView view) {
        this.view = view;

        // Initialize AppGroup TreeStore proxy and loader
        appGroupProxy = new AppGroupProxy();
        toolbar = new AppsViewToolbarImpl();
        view.setNorthWidget(toolbar);

        this.view.setPresenter(this);
        this.toolbar.setPresenter(this);

        initHandlers();
        initConstants();
    }

    private void initHandlers() {
        EventBus.getInstance().addHandler(AppSearch3ResultSelectedEvent.TYPE,
                new AppSearch3ResultSelectedEventHandler() {
                    @Override
                    public void onSelect(AppSearch3ResultSelectedEvent event) {
                        setDesiredSelectedApp(event.getAppId());
                        view.selectAppGroup(event.getAppGroupId());
                    }

                });
        EventBus.getInstance().addHandler(AppSearch3ResultLoadEvent.TYPE,
                new AppSearch3ResultLoadEventHandler() {
                    @Override
                    public void onLoad(AppSearch3ResultLoadEvent event) {
                        view.setApps(event.getResults());
                    }
                });
        EventBus.getInstance().addHandler(AppFavoritedEvent.TYPE, new AppFavoritedEventHander() {

            @Override
            public void onAppFavorited(AppFavoritedEvent event) {
                AppGroup favAppGrp = null;
                for (AppGroup appGroup : view.getTreeStore().getAll()) {
                    if (appGroup.getName().equalsIgnoreCase(FAVORITES)) {
                        favAppGrp = appGroup;
                        int tmp = event.isFavorite() ? 1 : -1;

                        view.updateAppGroupAppCount(favAppGrp, favAppGrp.getAppCount() + tmp);
                        break;
                    }
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

        JSONArray items = JSONParser.parseStrict(properties.getPrivateWorkspaceItems()).isArray();
        if (items != null) {
            APPLICATIONS_UNDER_DEVLOPMENT = JsonUtil.getRawValueAsString(items.get(0));
            FAVORITES = JsonUtil.getRawValueAsString(items.get(1));
        }

        BETA_GROUP_ID = properties.getDefaultBetaCategoryId();
    }

    /**
     * Sets a string which is a place holder for selection after a call to {@link #fetchApps(AppGroup)}
     * 
     * @param appId
     */
    private void setDesiredSelectedApp(String appId) {
        this.desiredSelectedAppId = appId;
    }

    private String getDesiredSelectedApp() {
        return desiredSelectedAppId;
    }

    public AppsViewPresenter(final AppsView view, final CatalogWindowConfig config) {
        this(view);
        this.config = config;
    }

    @Override
    public void onAppGroupSelected(final AppGroup ag) {
        toolbar.setEditButtonEnabled(false);
        toolbar.setDeleteButtonEnabled(false);
        toolbar.setSubmitButtonEnabled(false);
        toolbar.setCopyButtonEnabled(false);
        toolbar.setAppInfoButtonEnabled(false);

        view.setCenterPanelHeading(ag.getName());
        fetchApps(ag);
    }

    @Override
    public void onAppSelected(final App app) {
        if (app == null) {
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

                if ((getDesiredSelectedApp() != null) && (!getDesiredSelectedApp().isEmpty())) {
                    view.selectApp(getDesiredSelectedApp());
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
    public void go(final HasOneWidget container) {
        container.setWidget(view.asWidget());

        // Fetch AppGroups
        appGroupProxy.load(null, new AsyncCallback<List<AppGroup>>() {
            @Override
            public void onSuccess(List<AppGroup> result) {
                addAppGroup(null, result);
                // Select previous user selections
                if (config != null) {
                    view.selectAppGroup(config.getCategoryId());
                    view.selectApp(config.getAppId());
                } else {
                    view.selectFirstAppGroup();
                }
            }

            private void addAppGroup(AppGroup parent, List<AppGroup> children) {
                if ((children == null) || children.isEmpty()) {
                    return;
                }
                if (parent == null) {
                    view.getTreeStore().add(children);
                } else {
                    view.getTreeStore().replaceChildren(parent, children);
                }

                for (AppGroup ag : children) {
                    addAppGroup(ag, ag.getGroups());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
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
        Window appInfoWin = new Window();
        appInfoWin.setPixelSize(200, 200);
        appInfoWin.add(new AppInfoView());
        appInfoWin.show();
    }

    @Override
    public void onRequestToolClicked() {
        new NewToolRequestWindow().show();
    }

    @Override
    public void onCopyClicked() {
        final App selectedApp = getSelectedApp();
        Services.USER_APP_SERVICE.appExportable(selectedApp.getId(),
                new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        JSONObject exportable = JsonUtil.getObject(result);

                        if (JsonUtil.getBoolean(exportable, "can-export", false)) { //$NON-NLS-1$
                            copyApp(selectedApp);
                        } else {
                            ErrorHandler.post(JsonUtil.getString(exportable, "cause")); //$NON-NLS-1$
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                    }
                });

    }

    private void copyApp(final App app) {
        Services.USER_APP_SERVICE.copyApp(app.getId(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String copiedAppId = JsonUtil.getString(JsonUtil.getObject(result), "analysis_id"); //$NON-NLS-1$

                if (!copiedAppId.isEmpty()) {
                    AppGroup selectedAppGroup = getSelectedAppGroup();
                    if (selectedAppGroup != null) {
                        fetchApps(selectedAppGroup);
                    }
                    AppGroup appGroup = view.findAppGroup(app.getGroupId());
                    if (appGroup != null) {
                        view.updateAppGroupAppCount(appGroup, appGroup.getAppCount() + 1);
                    }
                    // Open TITO
                    // TODO JDS Rename the "TemplateLoadEvent"
                    EventBus.getInstance().fireEvent(new TemplateLoadEvent(copiedAppId, MODE.EDIT));
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
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

                                    // TODO Launch Tito window
                                    EventBus.getInstance().fireEvent(new AppDeleteEvent(app.getId()));
                                }
                            });
                }

            }
        });
        msgBox.show();

    }

    /*
     * TODO JDS Verify functionality
     */
    @Override
    public void submitClicked() {
        App selectedApp = getSelectedApp();
        final Window makePublicWin = new Window();
        makePublicWin.setModal(true);

        SubmitAppForPublicUsePanel requestForm = new SubmitAppForPublicUsePanel(selectedApp,
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String url) {
                        makePublicWin.hide();

                        MessageBox.info(I18N.DISPLAY.makePublicSuccessTitle(),
                                I18N.DISPLAY.makePublicSuccessMessage(url), null);

                        // Create and fire event
                        AppGroupCountUpdateEvent event = new AppGroupCountUpdateEvent(false,
                                AppGroupType.BETA);
                        EventBus.getInstance().fireEvent(event);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        makePublicWin.hide();
                        if (caught != null) {
                            ErrorHandler.post(I18N.DISPLAY.makePublicFail(), caught);
                        }
                    }
                });

        makePublicWin.setHeadingText(selectedApp.getName()
                + " " + I18N.DISPLAY.publicSubmissionForm()); //$NON-NLS-1$
        makePublicWin.setPixelSize(615, 480);
        makePublicWin.setResizable(false);
        makePublicWin.add(requestForm);

        makePublicWin.show();

    }

    @Override
    public void createNewAppClicked() {
        EventBus.getInstance().fireEvent(new CreateNewAppEvent());
    }

    @Override
    public void createWorkflowClicked() {
        EventBus.getInstance().fireEvent(new CreateNewWorkflowEvent());
    }

    @Override
    public void onAppNameSelected(App app) {
        UserEvent e = new UserEvent(Constants.CLIENT.windowTag(), app.getId());
        EventBus.getInstance().fireEvent(e);
    }

}
