package org.iplantc.core.uiapplications.client.presenter;

import java.util.List;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.events.AnalysisDeleteEvent;
import org.iplantc.core.uiapplications.client.events.AnalysisGroupCountUpdateEvent;
import org.iplantc.core.uiapplications.client.events.AnalysisGroupCountUpdateEvent.AnalysisGroupType;
import org.iplantc.core.uiapplications.client.events.CreateNewAppEvent;
import org.iplantc.core.uiapplications.client.events.CreateNewWorkflowEvent;
import org.iplantc.core.uiapplications.client.events.TemplateLoadEvent;
import org.iplantc.core.uiapplications.client.events.TemplateLoadEvent.MODE;
import org.iplantc.core.uiapplications.client.models.CatalogWindowConfig;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisAutoBeanFactory;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisList;
import org.iplantc.core.uiapplications.client.presenter.proxy.AnalysisGroupProxy;
import org.iplantc.core.uiapplications.client.views.AppsView;
import org.iplantc.core.uiapplications.client.views.panels.SubmitAppForPublicUsePanel;
import org.iplantc.core.uiapplications.client.views.widgets.AppInfoView;
import org.iplantc.core.uiapplications.client.views.widgets.AppsViewToolbar;
import org.iplantc.core.uiapplications.client.views.widgets.AppsViewToolbarImpl;
import org.iplantc.core.uiapplications.client.views.windows.NewToolRequestWindow;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.presenter.Presenter;

import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
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
 * The presenter for the AppsView. TODO JDS Document responsibility and intent of presenter.
 * 
 * @author jstroot
 * 
 */
public class AppsViewPresenter implements Presenter, AppsView.Presenter, AppsViewToolbar.Presenter {

    protected final AppsView view;

    private final AnalysisGroupProxy analysisGroupProxy;
    private CatalogWindowConfig config;
    private final AppsViewToolbar toolbar;

    protected AppsViewPresenter(final AppsView view) {
        this.view = view;

        // Initialize AnalysisGroup TreeStore proxy and loader
        analysisGroupProxy = new AnalysisGroupProxy();
        toolbar = new AppsViewToolbarImpl();
        view.setNorthWidget(toolbar);

        this.view.setPresenter(this);
        this.toolbar.setPresenter(this);
    }

    public AppsViewPresenter(final AppsView view, final CatalogWindowConfig config) {
        this(view);
        this.config = config;
    }

    @Override
    public void onAppGroupSelected(final AnalysisGroup ag) {
        toolbar.setEditButtonEnabled(false);
        toolbar.setDeleteButtonEnabled(false);
        toolbar.setSubmitButtonEnabled(false);
        toolbar.setCopyButtonEnabled(false);
        toolbar.setAppInfoButtonEnabled(false);

        view.setCenterPanelHeading(ag.getName());
        fetchApps(ag);
    }

    @Override
    public void onAppSelected(final Analysis analysis) {
        if (analysis == null) {
            toolbar.setEditButtonEnabled(false);
            toolbar.setDeleteButtonEnabled(false);
            toolbar.setSubmitButtonEnabled(false);
            toolbar.setCopyButtonEnabled(false);
            toolbar.setAppInfoButtonEnabled(false);
        } else if (analysis.isPublic()) {
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
    protected void fetchApps(final AnalysisGroup ag) {
        view.maskCenterPanel(I18N.DISPLAY.loadingMask());
        Services.TEMPLATE_SERVICE.getAnalysis(ag.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                AnalysisAutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);
                AutoBean<AnalysisList> bean = AutoBeanCodex.decode(factory, AnalysisList.class, result);

                view.setAnalyses(bean.as().getAnalyses());

                selectFirstApp();
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
        view.selectFirstAnalysis();
    }

    @Override
    public void go(final HasOneWidget container) {
        container.setWidget(view.asWidget());

        // Fetch AnalysisGroups
        analysisGroupProxy.load(null, new AsyncCallback<List<AnalysisGroup>>() {
            @Override
            public void onSuccess(List<AnalysisGroup> result) {
                addAnalysisGroup(null, result);
                // Select previous user selections
                if (config != null) {
                    view.selectAnalysisGroup(config.getCategoryId());
                    view.selectAnalysis(config.getAppId());
                } else {
                    view.selectFirstAnalysisGroup();
                }
            }

            private void addAnalysisGroup(AnalysisGroup parent, List<AnalysisGroup> children) {
                if ((children == null) || children.isEmpty()) {
                    return;
                }
                if (parent == null) {
                    view.getTreeStore().add(children);
                } else {
                    view.getTreeStore().replaceChildren(parent, children);
                }

                for (AnalysisGroup ag : children) {
                    addAnalysisGroup(ag, ag.getGroups());
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(caught);
            }
        });
    }

    @Override
    public Analysis getSelectedApp() {
        return view.getSelectedAnalysis();
    }

    @Override
    public AnalysisGroup getSelectedAppGroup() {
        return view.getSelectedAnalysisGroup();
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
        // TODO JDS Needs to be tested
        final Analysis selectedAnalysis = getSelectedApp();
        Services.USER_TEMPLATE_SERVICE.analysisExportable(selectedAnalysis.getId(),
                new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        JSONObject exportable = JsonUtil.getObject(result);

                        if (JsonUtil.getBoolean(exportable, "can-export", false)) { //$NON-NLS-1$
                            copyAnalysis(selectedAnalysis);
                            EventBus.getInstance().fireEvent(
                                    new AnalysisGroupCountUpdateEvent(true, null));
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

    private void copyAnalysis(final Analysis analysis) {
        Services.USER_TEMPLATE_SERVICE.copyAnalysis(analysis.getId(), new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String copiedAnalysisId = JsonUtil.getString(JsonUtil.getObject(result), "analysis_id"); //$NON-NLS-1$

                if (!copiedAnalysisId.isEmpty()) {
                    AnalysisGroup analysisGroup = view.getTreeStore().findModelWithKey(
                            analysis.getGroupId());
                    if (analysisGroup != null) {
                        fetchApps(analysisGroup);
                    }
                    // Open TITO
                    EventBus.getInstance().fireEvent(new TemplateLoadEvent(copiedAnalysisId, MODE.EDIT));
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
        final Analysis app = getSelectedApp();
        ConfirmMessageBox msgBox = new ConfirmMessageBox(I18N.DISPLAY.warning(),
                I18N.DISPLAY.appDeleteWarning());

        msgBox.addHideHandler(new HideHandler() {

            @Override
            public void onHide(HideEvent event) {
                Dialog btn = (Dialog)event.getSource();
                String text = btn.getHideButton().getItemId();
                if (text.equals(PredefinedButton.YES.name())) {
                    Services.USER_TEMPLATE_SERVICE.deleteAnalysisFromWorkspace(UserInfo.getInstance()
                            .getUsername(), UserInfo.getInstance().getFullUsername(), app.getId(),
                            new AsyncCallback<String>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    ErrorHandler.post(I18N.ERROR.appRemoveFailure(), caught);
                                }

                                @Override
                                public void onSuccess(String result) {
                                    // Remove from visible list
                                    view.removeAnalysis(app);

                                    // TODO Launch Tito window
                                    EventBus.getInstance().fireEvent(new AnalysisDeleteEvent(app.getId()));
                                    EventBus.getInstance().fireEvent(
                                            new AnalysisGroupCountUpdateEvent(false, null));
                                }
                            });
                }

            }
        });
        msgBox.show();

    }

    @Override
    public void submitClicked() {
        Analysis selectedAnalysis = getSelectedApp();
        final Window makePublicWin = new Window();
        makePublicWin.setModal(true);

        SubmitAppForPublicUsePanel requestForm = new SubmitAppForPublicUsePanel(selectedAnalysis,
                new AsyncCallback<String>() {
                    @Override
                    public void onSuccess(String url) {
                        makePublicWin.hide();

                        MessageBox.info(I18N.DISPLAY.makePublicSuccessTitle(),
                                I18N.DISPLAY.makePublicSuccessMessage(url), null);

                        // Create and fire event
                        AnalysisGroupCountUpdateEvent event = new AnalysisGroupCountUpdateEvent(false,
                                AnalysisGroupType.BETA);
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

        makePublicWin.setHeadingText(selectedAnalysis.getName()
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

}
