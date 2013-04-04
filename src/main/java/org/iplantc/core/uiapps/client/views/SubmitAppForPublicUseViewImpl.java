package org.iplantc.core.uiapps.client.views;

import org.iplantc.core.uiapps.client.models.autobeans.App;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * A form that enables the user to make an app public
 * 
 * @author sriram
 * 
 */
public class SubmitAppForPublicUseViewImpl implements SubmitAppForPublicUseView {

    private static SubmitAppForPublicUseViewUiBinder uiBinder = GWT
            .create(SubmitAppForPublicUseViewUiBinder.class);

    final private Widget widget;

    @UiField
    TextField appName;

    @UiField
    TextField appDesc;

    @UiTemplate("SubmitAppForPublicUseView.ui.xml")
    interface SubmitAppForPublicUseViewUiBinder extends UiBinder<Widget, SubmitAppForPublicUseViewImpl> {
    }

    public SubmitAppForPublicUseViewImpl(App selectedApp) {
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter p) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSubmitBtnClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCancelBtnClick() {
        // TODO Auto-generated method stub

    }

}
