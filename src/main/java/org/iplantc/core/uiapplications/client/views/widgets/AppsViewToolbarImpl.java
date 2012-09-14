package org.iplantc.core.uiapplications.client.views.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

public class AppsViewToolbarImpl implements AppsViewToolbar {

    private static AppsViewToolbarUiBinder uiBinder = GWT.create(AppsViewToolbarUiBinder.class);

    @UiTemplate("AppsViewToolbar.ui.xml")
    interface AppsViewToolbarUiBinder extends UiBinder<Widget, AppsViewToolbarImpl> {
    }

    private final Widget widget;
    private Presenter presenter;

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

    @UiField
    AppSearchField3 appSearch;

    public AppsViewToolbarImpl() {
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @UiHandler("appInfo")
    public void appInfoClicked(SelectEvent event) {
        presenter.onAppInfoClicked();
    }

    @UiHandler("requestTool")
    public void requestToolClicked(SelectEvent event) {
        presenter.onRequestToolClicked();
    }

    @UiHandler("copy")
    public void copyClicked(SelectEvent event) {
        presenter.onCopyClicked();
    }

    @UiHandler("delete")
    public void deleteClicked(SelectEvent event) {
        presenter.onDeleteClicked();
    }

    @UiHandler("submit")
    public void submitClicked(SelectEvent event) {
        presenter.submitClicked();
    }

    @Override
    public void setEditButtonEnabled(boolean enabled) {
        edit.setEnabled(enabled);
    }

    @Override
    public void setSubmitButtonEnabled(boolean enabled) {
        submit.setEnabled(enabled);
    }

    @Override
    public void setDeleteButtonEnabled(boolean enabled) {
        delete.setEnabled(enabled);
    }

}
