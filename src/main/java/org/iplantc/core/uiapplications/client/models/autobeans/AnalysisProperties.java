package org.iplantc.core.uiapplications.client.models.autobeans;


import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface AnalysisProperties extends PropertyAccess<Analysis>{

    // ModelKeyProvider<Analysis> id();

    ValueProvider<Analysis, String> name();

    ValueProvider<Analysis, String> integratorName();

    ValueProvider<Analysis, AnalysisFeedback> rating();

}
