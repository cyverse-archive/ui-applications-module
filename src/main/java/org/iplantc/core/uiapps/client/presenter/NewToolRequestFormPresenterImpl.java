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
        if (view.isValid()) {
            view.trimFields();
            view.submit();
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

}
