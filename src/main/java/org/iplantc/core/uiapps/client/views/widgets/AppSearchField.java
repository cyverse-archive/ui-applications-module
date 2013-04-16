package org.iplantc.core.uiapps.client.views.widgets;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.widgets.SearchField;

/**
 * A SearchField for the App Catalog main toolbar that performs remote app searches.
 * 
 * @author psarando
 * 
 */
public class AppSearchField extends SearchField {
    protected String tag;

    public AppSearchField(String tag) {
        // Set the dataIndex to something that is not in the App grid in order to prevent the filter
        // field from appearing in the column menus, and to prevent the column heading from being
        // emphasized by the GridFilters using this field's StringFilter.
        super("n/a"); //$NON-NLS-1$

        this.tag = tag;

        setWidth(255);
        setEmptyText(I18N.DISPLAY.searchApps());
        setMaxLength(255);
    }

    @Override
    protected void clearFilter() {
        // Do not set the StringFilter value to null in order to prevent a remote load from triggering.
    }
}
