/**
 * 
 */
package org.iplantc.core.uiapps.client.views.dialogs;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.presenter.SubmitAppForPublicPresenter;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseViewImpl;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView.Presenter;

import com.sencha.gxt.widget.core.client.Dialog;

/**
 * @author sriram
 *
 */
public class SubmitAppForPublicDialog extends Dialog {

    public SubmitAppForPublicDialog(final App selectedApp) {
        setHeadingText(I18N.DISPLAY.publicSubmissionForm()); //$NON-NLS-1$
        setPixelSize(615, 480);
        setResizable(false);
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        getButtonById(PredefinedButton.OK.toString()).setText(I18N.DISPLAY.submit());
        SubmitAppForPublicUseView view = new SubmitAppForPublicUseViewImpl(selectedApp);
        Presenter p = new SubmitAppForPublicPresenter(view);
        view.setPresenter(p);
        p.go(this);
    }

}
