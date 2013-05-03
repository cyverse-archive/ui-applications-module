/**
 * 
 */
package org.iplantc.core.uiapps.client.views.dialogs;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.events.AppGroupCountUpdateEvent;
import org.iplantc.core.uiapps.client.events.AppGroupCountUpdateEvent.AppGroupType;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.presenter.SubmitAppForPublicPresenter;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseView.Presenter;
import org.iplantc.core.uiapps.client.views.SubmitAppForPublicUseViewImpl;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IplantInfoBox;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * @author sriram
 * 
 */
public class SubmitAppForPublicDialog extends Dialog {

    private ToolButton tool_help;

    private final class SubmitAppForPublicCallbackImpl implements AsyncCallback<String> {
        @Override
        public void onSuccess(String url) {
            hide();

            new IplantInfoBox(I18N.DISPLAY.makePublicSuccessTitle(),
                    I18N.DISPLAY.makePublicSuccessMessage(url)).show();

            // Create and fire event
            AppGroupCountUpdateEvent event = new AppGroupCountUpdateEvent(false,
                    AppGroupType.BETA);
            EventBus.getInstance().fireEvent(event);
        }

        @Override
        public void onFailure(Throwable caught) {
            hide();
            if (caught != null) {
                ErrorHandler.post(I18N.DISPLAY.makePublicFail(), caught);
            }
        }
    }

    public SubmitAppForPublicDialog(final App selectedApp) {
        initDialog();
        TextButton okBtn = getButtonById(PredefinedButton.OK.toString());


        SubmitAppForPublicUseView view = new SubmitAppForPublicUseViewImpl(selectedApp);
        final Presenter p = new SubmitAppForPublicPresenter(view, new SubmitAppForPublicCallbackImpl());
        p.go(this);
        okBtn.setText(I18N.DISPLAY.submit());
        okBtn.addSelectHandler(new SelectHandler() {

            @Override
            public void onSelect(SelectEvent event) {
                p.onSubmit();
            }
        });
    }

    private void initDialog() {
        setHeadingText(I18N.DISPLAY.publicSubmissionForm()); //$NON-NLS-1$
        setPixelSize(615, 480);
        setResizable(false);
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        tool_help = new ToolButton(ToolButton.QUESTION);
        getHeader().addTool(tool_help);
    }

}
