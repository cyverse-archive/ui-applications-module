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

}
