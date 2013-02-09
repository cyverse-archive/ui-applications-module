package org.iplantc.core.uiapplications.client.views.widgets;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * FIXME JDS Ensure that all buttons have the appropriate debug ids.
 * 
 * @author jstroot
 * 
 */
public interface AppsViewToolbar extends IsWidget {
    public interface Presenter {
        interface Builder extends org.iplantc.core.uicommons.client.presenter.Presenter {
            Builder hideToolbarButtonCreate();

            Builder hideToolbarButtonCopy();

            Builder hideToolbarButtonEdit();

            Builder hideToolbarButtonDelete();

            Builder hideToolbarButtonSubmit();

            Builder hideToolbarButtonRequestTool();
        }

        public Builder builder();

        AppsViewToolbar getToolbar();

        void onAppInfoClicked();

        void onRequestToolClicked();

        void onCopyClicked();

        void onDeleteClicked();

        void submitClicked();

        void createNewAppClicked();

        void createWorkflowClicked();

        void onEditClicked();
    }

    void setPresenter(Presenter presenter);

    void setEditButtonEnabled(boolean enabled);

    void setSubmitButtonEnabled(boolean enabled);

    void setDeleteButtonEnabled(boolean enabled);

    void setCopyButtonEnabled(boolean enabled);

    void setAppInfoButtonEnabled(boolean enabled);

    void setCreateButtonVisible(boolean visible);

    void setCopyButtonVisible(boolean visible);

    void setEditButtonVisible(boolean visible);

    void setDeleteButtonVisible(boolean visible);

    void setSubmitButtonVisible(boolean visible);

    void setRequestToolButtonVisible(boolean visible);
}
