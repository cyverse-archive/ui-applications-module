package org.iplantc.core.uiapps.client.views.panels;

import org.iplantc.core.uicommons.client.I18N;
import org.iplantc.core.uicommons.client.validators.UrlValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * 
 * A form to submit request to install new tools in condor
 * 
 * @author sriram
 * 
 */
public class NewToolRequestFormViewImpl extends Composite implements NewToolRequestFormView {

    private static NewToolRequestFormViewUiBinder uiBinder = GWT
            .create(NewToolRequestFormViewUiBinder.class);

    final private Widget widget;

    public final String YES = "Yes";
    public final String NO = "No";
    public final String DONT_KNOW = "Don't know";

    public final String ARCH32 = "32-bit Generic";
    public final String ARCH64 = "64-bit Generic";
    public final String OTHERS = "Others";

    private Presenter presenter;

    @UiField(provided = true)
    SimpleComboBox<String> multiThreadCbo;

    @UiField
    VerticalLayoutContainer container;

    @UiField
    FieldSet toolSet;

    @UiField
    FieldSet otherSet;

    @UiField
    Radio toolLink;

    @UiField
    Radio toolUpld;

    @UiField(provided = true)
    SimpleComboBox<String> archCbo;

    @UiField
    FileUploadField binUpld;

    @UiField
    FileUploadField testDataUpld;

    @UiField
    FileUploadField otherDataUpld;

    @UiField
    TextField binLink;

    @UiField
    TextField toolDoc;


    @UiField
    TextButton submitBtn;

    @UiField
    TextButton cancelBtn;

    @UiField
    FormPanel formPanel;



    @UiTemplate("NewToolRequestFormView.ui.xml")
    interface NewToolRequestFormViewUiBinder extends UiBinder<Widget, NewToolRequestFormViewImpl> {
    }

    public NewToolRequestFormViewImpl() {
        multiThreadCbo = new SimpleComboBox<String>(new StringLabelProvider<String>());
        multiThreadCbo.add(YES);
        multiThreadCbo.add(NO);
        multiThreadCbo.add(DONT_KNOW);
        multiThreadCbo.setValue(DONT_KNOW);

        archCbo = new SimpleComboBox<String>(new StringLabelProvider<String>());
        archCbo.add(ARCH32);
        archCbo.add(ARCH64);
        archCbo.add(OTHERS);
        archCbo.add(DONT_KNOW);
        archCbo.setValue(ARCH64);

        widget = uiBinder.createAndBindUi(this);
        container.setScrollMode(ScrollMode.AUTOY);
        container.setAdjustForScroll(true);

        initSrcModeSelection();
        initValidators();
    }



    private void initValidators() {
        binLink.addValidator(new UrlValidator());
        toolDoc.addValidator(new UrlValidator());
    }

    void initSrcModeSelection() {
        ToggleGroup toggle = new ToggleGroup();
        toggle.add(toolLink);
        toggle.add(toolUpld);
        toggle.addValueChangeHandler(new ValueChangeHandler<HasValue<Boolean>>() {

            @Override
            public void onValueChange(ValueChangeEvent<HasValue<Boolean>> event) {
                if (toolLink.getValue()) {
                    binLink.setVisible(true);
                    binLink.setAllowBlank(false);
                    binUpld.setVisible(false);
                    binUpld.setAllowBlank(true);

                } else {
                    binLink.setVisible(false);
                    binLink.setAllowBlank(true);
                    binUpld.setVisible(true);
                    binUpld.setAllowBlank(false);
                }

            }
        });
    }

    @UiHandler({"submitBtn"})
    public void onSubmitBtnClick(SelectEvent event) {
        final AutoProgressMessageBox box = new AutoProgressMessageBox("Progress",
                "Submitting your request, please wait...");
        box.setProgressText("Submitting...");

        if (formPanel.isValid()) {
            // remove unused file upload fields
            if (!binUpld.isVisible()) {
                binUpld.removeFromParent();
            }
            
            if (testDataUpld.getValue() == null || testDataUpld.getValue().isEmpty()) {
                testDataUpld.removeFromParent();
            }

            if (otherDataUpld.getValue() == null || otherDataUpld.getValue().isEmpty()) {
                otherDataUpld.removeFromParent();
            }

            box.auto();
            box.show();
            formPanel.submit();
        }
        formPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {
            @Override
            public void onSubmitComplete(SubmitCompleteEvent event) {
                box.hide();
                AlertMessageBox amb = new AlertMessageBox(I18N.DISPLAY.alert(), event.getResults());
                amb.show();
                presenter.onRequestComplete();
            }
        });
    }

    @UiHandler({"cancelBtn"})
    public void onCancelBtnClick(SelectEvent event) {
        presenter.onRequestError();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter p) {
        this.presenter = p;
    }

}
