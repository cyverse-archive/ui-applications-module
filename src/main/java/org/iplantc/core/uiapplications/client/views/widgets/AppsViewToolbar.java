package org.iplantc.core.uiapplications.client.views.widgets;

import com.google.gwt.user.client.ui.IsWidget;

public interface AppsViewToolbar extends IsWidget {
    public interface Presenter {

        void onAppInfoClicked();

        void onRequestToolClicked();

        void onCopyClicked();

        void onDeleteClicked();

        void submitClicked();
    }

    void setEditButtonEnabled(boolean enabled);

    void setSubmitButtonEnabled(boolean enabled);

    void setDeleteButtonEnabled(boolean enabled);

}
