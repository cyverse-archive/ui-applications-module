package org.iplantc.core.uiapplications.client.views.widgets;

import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uiapplications.client.views.form.AppSearchField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class AppsViewToolbar implements IsWidget {

    private static AppsViewToolbarUiBinder uiBinder = GWT.create(AppsViewToolbarUiBinder.class);

    interface AppsViewToolbarUiBinder extends UiBinder<Widget, AppsViewToolbar> {
    }

    private final Widget widget;

    @UiField
    ToolBar toolBar;

    @UiField
    MenuItem createNewApp;

    @UiField
    MenuItem createWorkflow;

    @UiField
    TextButton appInfo;

    @UiField
    TextButton requestTool;

    @UiField
    TextButton copy;

    @UiField
    TextButton edit;

    @UiField
    TextButton delete;

    @UiField
    TextButton submit;

    public AppsViewToolbar() {
        widget = uiBinder.createAndBindUi(this);
        AppTemplateServiceFacade templateService = GWT.create(AppTemplateServiceFacade.class);
        // FIXME JDS Remove hard-coded window "tag", 'de_catalog'
        toolBar.add(new AppSearchField("de_catalog", templateService));
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @UiHandler("appInfo")
    public void appInfoClicked(SelectEvent event) {

    }

    @UiHandler("requestTool")
    public void requestToolClicked(SelectEvent event) {

    }

    @UiHandler("copy")
    public void copyClicked(SelectEvent event) {

    }

    @UiHandler("delete")
    public void deleteClicked(SelectEvent event) {

    }

    @UiHandler("submit")
    public void submitClicked(SelectEvent event) {

    }


}
