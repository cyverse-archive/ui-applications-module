package org.iplantc.core.uiapplications.client.presenter;

import java.util.List;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.models.CatalogWindowConfig;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisAutoBeanFactory;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisList;
import org.iplantc.core.uiapplications.client.presenter.proxy.AnalysisGroupProxy;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uiapplications.client.views.AppsView;
import org.iplantc.core.uiapplications.client.views.widgets.AppsViewToolbar;
import org.iplantc.core.uiapplications.client.views.widgets.AppsViewToolbarImpl;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.presenter.Presenter;
import org.iplantc.de.client.CommonDisplayStrings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

public class AppsViewPresenter implements Presenter, AppsView.Presenter, AppsViewToolbar.Presenter {

    private final AppsView view;
    private final AppTemplateServiceFacade templateService;

    private final AnalysisGroupProxy analysisGroupProxy;
    private final CommonDisplayStrings displayStrings;
    private final CatalogWindowConfig config;
    private final AppsViewToolbar toolbar;


    public AppsViewPresenter(final AppsView view, final CatalogWindowConfig config) {
        /*
         * When the view comes in, it will already have: -- all of its stores
         */
        this.view = view;
        toolbar = new AppsViewToolbarImpl();
        view.setNorthWidget(toolbar);
        this.templateService = Services.TEMPLATE_SERVICE;
        this.displayStrings = I18N.DISPLAY;
        this.config = config;


        // Initialize AnalysisGroup TreeStore proxy and loader
        analysisGroupProxy = new AnalysisGroupProxy();

        this.view.setPresenter(this);
    }

    @Override
    public void onAnalysisGroupSelected(final AnalysisGroup ag) {
        toolbar.setEditButtonEnabled(false);
        toolbar.setDeleteButtonEnabled(false);
        toolbar.setSubmitButtonEnabled(false);

        view.setMainPanelHeading(ag.getName());
        fetchApps(ag);
    }

    @Override
    public void onAnalysisSelected(final Analysis analysis) {
        if (analysis == null) {
            return;
        }
        if (analysis.isPublic()) {
            toolbar.setEditButtonEnabled(false);
            toolbar.setDeleteButtonEnabled(false);
            toolbar.setSubmitButtonEnabled(false);
        } else {
            toolbar.setEditButtonEnabled(true);
            toolbar.setDeleteButtonEnabled(true);
            toolbar.setSubmitButtonEnabled(true);
        }
    
    }

    /**
     * Retrieves the apps for the given group by updating and executing the list loader
     * 
     * @param ag
     */
    private void fetchApps(final AnalysisGroup ag) {
        view.maskMainPanel(displayStrings.loadingMask());
        templateService.getAnalysis(ag.getId(), new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                AnalysisAutoBeanFactory factory = GWT.create(AnalysisAutoBeanFactory.class);
                AutoBean<AnalysisList> bean = AutoBeanCodex.decode(factory, AnalysisList.class, result);

                view.setAnalyses(bean.as().getAnalyses());
                view.selectFirstAnalysis();
                view.unMaskMainPanel();
            }

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.post(I18N.ERROR.retrieveFolderInfoFailed(), caught);
                view.unMaskMainPanel();
            }
        });
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
    public Analysis getSelectedAnalysis() {
        return view.getSelectedAnalysis();
    }

    @Override
    public AnalysisGroup getSelectedAnalysisGroup() {
        return view.getSelectedAnalysisGroup();
    }

    @Override
    public void onAppInfoClicked() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRequestToolClicked() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCopyClicked() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDeleteClicked() {
        // TODO Auto-generated method stub

    }

    @Override
    public void submitClicked() {
        // TODO Auto-generated method stub

    }

}
