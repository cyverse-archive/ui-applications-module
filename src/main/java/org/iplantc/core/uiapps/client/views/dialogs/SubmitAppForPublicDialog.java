/**
 * 
 */
package org.iplantc.core.uiapps.client.views.dialogs;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.presenter.SubmitAppForPublicPresenter;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView.Presenter;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseViewImpl;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.tree.Tree;

/**
 * @author sriram
 *
 */
public class SubmitAppForPublicDialog extends Dialog {

    public SubmitAppForPublicDialog(final App selectedApp, TreeStore<AppGroup> treeStore) {
        setHeadingText(I18N.DISPLAY.publicSubmissionForm()); //$NON-NLS-1$
        setPixelSize(615, 480);
        setResizable(false);
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        getButtonById(PredefinedButton.OK.toString()).setText(I18N.DISPLAY.submit());

        SubmitAppForPublicUseView view = new SubmitAppForPublicUseViewImpl(selectedApp, treeStore);
        Presenter p = new SubmitAppForPublicPresenter(view);
        p.go(this);
    }

}
