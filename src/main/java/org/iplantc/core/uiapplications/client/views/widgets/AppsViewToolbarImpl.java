package org.iplantc.core.uiapplications.client.views.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class AppsViewToolbarImpl implements AppsViewToolbar {

    private static AppsViewToolbarUiBinder uiBinder = GWT.create(AppsViewToolbarUiBinder.class);

    @UiTemplate("AppsViewToolbar.ui.xml")
    interface AppsViewToolbarUiBinder extends UiBinder<Widget, AppsViewToolbarImpl> {
    }

    private final Widget widget;
    private Presenter presenter;

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

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
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

    @UiHandler("edit")
    public void editClicked(SelectEvent event) {
        presenter.onEditClicked();
    }

    @UiHandler("delete")
    public void deleteClicked(SelectEvent event) {
        presenter.onDeleteClicked();
    }

    @UiHandler("submit")
    public void submitClicked(SelectEvent event) {
        presenter.submitClicked();
    }

    @UiHandler("createNewApp")
    public void createNewAppClicked(SelectionEvent<Item> event) {
        presenter.createNewAppClicked();
    }

    @UiHandler("createWorkflow")
    public void createWorkflowClicked(SelectionEvent<Item> event) {
        presenter.createWorkflowClicked();
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

    @Override
    public void setCopyButtonEnabled(boolean enabled) {
        copy.setEnabled(enabled);
    }

    @Override
    public void setAppInfoButtonEnabled(boolean enabled) {
        appInfo.setEnabled(enabled);
    }

}
