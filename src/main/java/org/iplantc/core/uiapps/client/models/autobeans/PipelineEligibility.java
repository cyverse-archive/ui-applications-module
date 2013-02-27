package org.iplantc.core.uiapps.client.models.autobeans;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

public interface PipelineEligibility {

    String getReason();

    @PropertyName("is_valid")
    boolean isValid();

    void setReason(String reason);

    @PropertyName("is_valid")
    void setValid(boolean valid);
}
