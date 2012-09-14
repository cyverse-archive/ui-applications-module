package org.iplantc.core.uiapplications.client.models.autobeans;


import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface AnalysisProperties extends PropertyAccess<Analysis>{

    ModelKeyProvider<Analysis> id();

    LabelProvider<Analysis> name();

    ValueProvider<Analysis, String> integratorName();

    ValueProvider<Analysis, AnalysisFeedback> rating();

}
