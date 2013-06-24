package org.iplantc.core.uiapps.client.views;

import com.google.gwt.user.client.ui.IsWidget;

public interface NewToolRequestFormView extends IsWidget {

    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter {
        void onRequestError();

        /**
         * This is the callback for when a user requests to submit the form.
         */
        void onSubmitForm();

        /**
         * This is the callback for when a form has been received by the backend.
         * 
         * @param results The HTML-encoded results of the submission.
         */
        void onSubmissionComplete(String results);
    }

    void setPresenter(Presenter p);

    /**
     * Show the user a failed submission message
     */
    void indicateSubmissionFailure();

    /**
     * Show the user a successful submission message
     */
    void indicateSubmissionSuccess();

    /**
     * Checks if all of the user-provided fields are valid.
     */
    boolean isValid();

    void onSubmitBtnClick();

    void onCancelBtnClick();

    /**
     * Submits the tool request.
     */
    void submit();

    /**
     * Remove the unused optional fields from the form.
     */
    void trimFields();

}
