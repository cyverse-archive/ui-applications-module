package org.iplantc.core.uiapplications.client.views.panels;

import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uiapplications.client.views.form.AppSearchField;

import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;

/**
 * A ToolBar for the App Catalog main panel that includes a remote search field.
 * 
 * @author psarando
 * 
 */
public class CatalogMainToolBar extends ToolBar {

    private final AppSearchField searchField;

    public CatalogMainToolBar(String tag, AppTemplateServiceFacade templateService) {
        searchField = new AppSearchField(tag, templateService);

        setHeight(25);
        add(searchField);
    }

    public AppSearchField getSearchField() {
        return searchField;
    }
}
