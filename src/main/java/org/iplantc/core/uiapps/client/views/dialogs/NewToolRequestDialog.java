/**
 *
 */
package org.iplantc.core.uiapps.client.views.dialogs;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.presenter.NewToolRequestFormPresenterImpl;
import org.iplantc.core.uiapps.client.views.panels.NewToolRequestFormView;
import org.iplantc.core.uiapps.client.views.panels.NewToolRequestFormView.Presenter;
import org.iplantc.core.uiapps.client.views.panels.NewToolRequestFormViewImpl;

import com.google.gwt.user.client.Command;
import com.sencha.gxt.widget.core.client.Dialog;

/**
 * @author sriram
 *
 */
public class NewToolRequestDialog extends Dialog {

    public NewToolRequestDialog() {
        setHeadingText(I18N.DISPLAY.requestNewTool());
        setPixelSize(450, 400);
        this.setResizable(false);
        getButtonBar().clear();
        NewToolRequestFormView view = new NewToolRequestFormViewImpl();
        Presenter p = new NewToolRequestFormPresenterImpl(view, new Command() {

            @Override
            public void execute() {
                hide();

            }
        });
        p.go(this);

    }

}
