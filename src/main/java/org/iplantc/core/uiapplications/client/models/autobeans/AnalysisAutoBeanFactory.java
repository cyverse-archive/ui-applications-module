package org.iplantc.core.uiapplications.client.models.autobeans;

import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;

public interface AnalysisAutoBeanFactory extends AutoBeanFactory {

    AutoBean<Analysis> analysis();

    AutoBean<AnalysisFeedback> analysisFeedback();

    AutoBean<PipelineEligibility> pipelineEligibility();

    AutoBean<AnalysisList> analysisList();

    AutoBean<AnalysisGroup> analysisGroup();

    AutoBean<AnalysisGroupList> analysisGroups();
}
