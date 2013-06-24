/**
 * 
 */
package org.iplantc.core.uiapps.client.presenter;

import org.iplantc.core.jsonutil.JsonUtil;
import org.iplantc.core.uiapps.client.views.NewToolRequestFormView;
import org.iplantc.core.uiapps.client.views.NewToolRequestFormView.Presenter;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.sencha.gxt.core.client.util.Format;

/**
 * @author sriram
 *
 */
public class NewToolRequestFormPresenterImpl implements Presenter {

    private static String toRelPath(final String path) {
        if (path == null || path.equalsIgnoreCase("null")) {
            return "";
        }
        return path.replaceAll(".*[\\\\/]", "");
    }

    private NewToolRequestFormView view;
    private Command callback;

    public NewToolRequestFormPresenterImpl(NewToolRequestFormView view, Command callbackCmd) {
        this.view = view;
        this.callback = callbackCmd;
        view.setPresenter(this);
    }

    /* (non-Javadoc)
     * @see org.iplantc.core.uicommons.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasOneWidget)
     */
    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
    }

    @Override
    public void onRequestError() {
        executeCallback();
    }

    @Override
    public final void onSubmitForm() {
        if (isFormValid()) {
            // uploadToolBinary();
            // uploadTestData();
            // uploadOtherData();
            view.trimUploadFields();
            view.submit();
        } else {
            // TODO indicate form is invalid
        }
    }

    @Override
    public final void onSubmissionComplete(final String results) {
        JSONObject obj = JsonUtil.getObject(Format.stripTags(results));
        if (JsonUtil.getBoolean(obj, "success", false)) { //$NON-NLS-1$
            view.indicateSubmissionSuccess();
        } else {
        }
        executeCallback();
    }

    private void executeCallback() {
        if (callback != null) {
            callback.execute();
        }

    }

    private boolean isFormValid() {
        boolean valid = view.isValid();
        
        if (valid) {
            final String binPath = toRelPath(view.getToolBinaryPath());
            final String testDataPath = toRelPath(view.getTestDataPath());
            final String otherDataPath = toRelPath(view.getOtherDataPath());
            valid = binPath.equals(testDataPath) || binPath.equals(otherDataPath);
        }
        
        return valid;
    }

}
