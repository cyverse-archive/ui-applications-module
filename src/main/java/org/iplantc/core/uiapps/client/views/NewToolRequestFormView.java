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
     * Returns the current value of the other data field
     */
    String getOtherDataPath();

    /**
     * Returns the current value of the test data field.
     */
    String getTestDataPath();

    /**
     * Returns the current value of the tool's binary field.
     */
    String getToolBinaryPath();

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
     * Remove the upload fields from the form.
     */
    void trimUploadFields();

}
