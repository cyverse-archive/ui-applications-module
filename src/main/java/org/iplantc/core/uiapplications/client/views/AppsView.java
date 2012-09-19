package org.iplantc.core.uiapplications.client.views;

import java.util.List;

import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisGroup;

import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.TreeLoader;

public interface AppsView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {
        void onAnalysisSelected(final Analysis analysis);

        void onAnalysisGroupSelected(final AnalysisGroup ag);

        Analysis getSelectedAnalysis();

        AnalysisGroup getSelectedAnalysisGroup();
    }

    void setPresenter(final Presenter presenter);

    ListStore<Analysis> getListStore();

    TreeStore<AnalysisGroup> getTreeStore();

    void setListLoader(final ListLoader<ListLoadConfig, ListLoadResult<Analysis>> listLoader);

    void setTreeLoader(final TreeLoader<AnalysisGroup> treeLoader);

    void setMainPanelHeading(final String name);

    void maskMainPanel(final String loadingMask);

    void unMaskMainPanel();

    void selectAnalysis(String analysisId);

    void selectAnalysisGroup(String analysisGroupId);

    Analysis getSelectedAnalysis();

    AnalysisGroup getSelectedAnalysisGroup();

    void setAnalyses(List<Analysis> analyses);

    void setNorthWidget(IsWidget widget);

    void setEastWidget(IsWidget widget);

    void selectFirstAnalysis();

    void selectFirstAnalysisGroup();

    void removeAnalysis(Analysis analysis);

}
