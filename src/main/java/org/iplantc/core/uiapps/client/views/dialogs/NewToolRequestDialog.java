/**
 *
 */
package org.iplantc.core.uiapps.client.views.dialogs;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.presenter.NewToolRequestFormPresenterImpl;
import org.iplantc.core.uiapps.client.views.NewToolRequestFormView;
import org.iplantc.core.uiapps.client.views.NewToolRequestFormView.Presenter;
import org.iplantc.core.uiapps.client.views.NewToolRequestFormViewImpl;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;

import com.google.gwt.user.client.Command;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * @author sriram
 *
 */
public class NewToolRequestDialog extends IPlantDialog {

    private ToolButton tool_help;

    public NewToolRequestDialog() {
        setHeadingText(I18N.DISPLAY.requestNewTool());
        setPixelSize(450, 400);
        this.setResizable(false);
        setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
        setHideOnButtonClick(false);
        setOkButtonText(I18N.DISPLAY.submit());
        tool_help = new ToolButton(ToolButton.QUESTION);
        getHeader().addTool(tool_help);
        final NewToolRequestFormView view = new NewToolRequestFormViewImpl();
        Presenter p = new NewToolRequestFormPresenterImpl(view, new Command() {

            @Override
            public void execute() {
                hide();

            }
        });
        p.go(this);
        
        addOkButtonSelectHandler(new SelectHandler() {
            
            @Override
            public void onSelect(SelectEvent event) {
                view.onSubmitBtnClick();
            }
        });
        
        addCancelButtonSelectHandler(new SelectHandler() {
            
            @Override
            public void onSelect(SelectEvent event) {
                view.onCancelBtnClick();
            }
        });

    }
    


}
