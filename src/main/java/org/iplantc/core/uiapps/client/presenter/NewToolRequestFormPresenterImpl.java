/**
 * 
 */
package org.iplantc.core.uiapps.client.presenter;



import org.iplantc.core.uiapps.client.views.panels.NewToolRequestFormView;
import org.iplantc.core.uiapps.client.views.panels.NewToolRequestFormView.Presenter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasOneWidget;

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
    public void onRequestComplete() {
        if (callback != null) {
            callback.execute();
        }
        
    }

    @Override
    public void onRequestError() {
        if (callback != null) {
            callback.execute();
        }

    }

}
