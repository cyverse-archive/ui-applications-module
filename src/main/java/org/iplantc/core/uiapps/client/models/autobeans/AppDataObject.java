package org.iplantc.core.uiapps.client.models.autobeans;

import org.iplantc.core.uicommons.client.models.HasDescription;
import org.iplantc.core.uicommons.client.models.HasId;
import org.iplantc.core.uicommons.client.models.HasLabel;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * An App Property wrapper for Input and Output DataObjects.
 * 
 * @author psarando
 * 
 */
public interface AppDataObject extends HasId, HasLabel, HasDescription {

    String getName();

    void setName(String name);

    String getValue();

    void setValue(String value);

    @PropertyName("isVisible")
    boolean isVisible();

    @PropertyName("isVisible")
    void setVisible(boolean visible);

    String getType();

    void setType(String type);

    @PropertyName("data_object")
    DataObject getDataObject();

    @PropertyName("data_object")
    void setDataObject(DataObject dataObj);
}
