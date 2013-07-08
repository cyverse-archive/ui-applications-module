package org.iplantc.core.uiapps.client.views;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uicommons.client.validators.LengthRangeValidator;
import org.iplantc.core.uicommons.client.validators.NameValidator3;
import org.iplantc.core.uicommons.client.validators.UrlValidator;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IplantInfoBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 *
 * A form to submit request to install new tools in condor
 *
 * @author sriram
 *
 */
public final class NewToolRequestFormViewImpl<A, Y> extends Composite implements NewToolRequestFormView<A, Y> {

    @UiTemplate("NewToolRequestFormView.ui.xml")
    interface NewToolRequestFormViewUiBinder extends UiBinder<Widget, NewToolRequestFormViewImpl<?, ?>> {
    }

    private static final NewToolRequestFormViewUiBinder uiBinder = GWT.create(NewToolRequestFormViewUiBinder.class);

    private static String buildRequiredFieldLabel(final String label) {
        if (label == null) {
            return null;
        }

        return "<span style='color:red; top:-5px;' >*</span> " + label; //$NON-NLS-1$
    }

    @UiField
    VerticalLayoutContainer container;

    @UiField
    FieldLabel toolNameLbl;

    @UiField
    FieldLabel toolDescLbl;

    @UiField
    FieldLabel srcLbl;

    @UiField
    Radio toolLink;

    @UiField
    Radio toolUpld;

    @UiField
    FieldLabel docUrlLbl;

    @UiField
    FieldLabel versionLbl;

    @UiField
    FieldLabel archLbl;

    @UiField
    FieldLabel multiLbl;

    @UiField
    FieldLabel upldTestLbl;

    @UiField
    FieldLabel cmdLineLbl;

    @UiField
    TextField toolName;

    @UiField
    TextArea toolDesc;

    @UiField
    TextArea toolAttrib;

    @UiField
    TextField binLink;

    @UiField
    TextField toolDoc;

    @UiField
    TextField toolVersion;

    @UiField
    TextArea runInfo;

    @UiField
    TextArea otherInfo;

    @UiField(provided = true)
    final ComboBox<A> archCbo;

    @UiField(provided = true)
    final ComboBox<Y> multiThreadCbo;

    @UiField
    UploadForm binUpld;

    @UiField
    UploadForm testDataUpld;

    @UiField
    UploadForm otherDataUpld;

    private final AutoProgressMessageBox submissionProgressBox;

    private Presenter presenter;

    public NewToolRequestFormViewImpl(final ComboBox<A> archChooser, final ComboBox<Y> multithreadChooser) {
        archCbo = archChooser;
        multiThreadCbo = multithreadChooser;
        initWidget(uiBinder.createAndBindUi(this));
        submissionProgressBox = new AutoProgressMessageBox(I18N.DISPLAY.submitRequest());
        submissionProgressBox.auto();
        container.setScrollMode(ScrollMode.AUTOY);
        container.setAdjustForScroll(true);
        initValidators();
        initRequiredLabels();
    }

    private void initRequiredLabels() {
        toolNameLbl.setHTML(buildRequiredFieldLabel(I18N.DISPLAY.toolNameLabel()));
        toolDescLbl.setHTML(buildRequiredFieldLabel(I18N.DISPLAY.toolDesc()));
        srcLbl.setHTML(buildRequiredFieldLabel(I18N.DISPLAY.srcLinkPrompt()));
        docUrlLbl.setHTML(buildRequiredFieldLabel(I18N.DISPLAY.docLink()));
        versionLbl.setHTML(buildRequiredFieldLabel(I18N.DISPLAY.version()));
        archLbl.setHTML(buildRequiredFieldLabel(I18N.DISPLAY.architecture()));
        multiLbl.setHTML(buildRequiredFieldLabel(I18N.DISPLAY.isMultiThreaded()));
        upldTestLbl.setHTML(buildRequiredFieldLabel(I18N.DISPLAY.upldTestData()));
        cmdLineLbl.setHTML(buildRequiredFieldLabel(I18N.DISPLAY.cmdLineRun()));
    }

    private void initValidators() {
        toolName.addValidator(new LengthRangeValidator(I18N.DISPLAY.toolName(), 1, I18N.V_CONSTANTS.maxToolNameLength()));
        toolName.addValidator(new NameValidator3());
        binLink.addValidator(new UrlValidator());
        toolDoc.addValidator(new UrlValidator());
        binUpld.addValidator(new NameValidator3());
        testDataUpld.addValidator(new NameValidator3());
        otherDataUpld.addValidator(new NameValidator3());
    }

    @UiHandler("toolLink")
    void onLinkSelect(final ChangeEvent unused) {
        if (presenter != null) {
            presenter.onToolByUpload(false);
        }
    }

    @UiHandler("toolUpld")
    void onUploadSelect(final ChangeEvent unused) {
        if (presenter != null) {
            presenter.onToolByUpload(true);
        }
    }

    @Override
    public Uploader getOtherDataUploader() {
        return otherDataUpld;
    }

    @Override
    public Uploader getTestDataUploader() {
        return testDataUpld;
    }

    @Override
    public Uploader getToolBinaryUploader() {
        return binUpld;
    }

    @Override
    public final void indicateSubmissionStart() {
        submissionProgressBox.setProgressText(I18N.DISPLAY.submitting());
        submissionProgressBox.getProgressBar().reset();
        submissionProgressBox.show();
    }

    @Override
    public final void indicateSubmissionFailure(final String reason) {
        submissionProgressBox.hide();
        final AlertMessageBox amb = new AlertMessageBox(I18N.DISPLAY.alert(), reason);
        amb.show();
    }

    @Override
    public final void indicateSubmissionSuccess() {
        submissionProgressBox.hide();
        final IplantInfoBox successMsg = new IplantInfoBox(I18N.DISPLAY.success(), I18N.DISPLAY.requestConfirmMsg());
        successMsg.show();
    }

    @Override
    public boolean isValid() {
        return FormPanelHelper.isValid(container, false);
    }

    @Override
    public void setPresenter(final Presenter p) {
        this.presenter = p;
    }

    @Override
    public void setToolByUpload(final boolean byUpload) {
        toolUpld.setValue(byUpload);
        binUpld.setVisible(byUpload);
        binUpld.setEnabled(byUpload);
        binLink.setVisible(!byUpload);
        binLink.setEnabled(!byUpload);
    }

    @Override
    public IsField<String> getNameField() {
        return toolName;
    }

    @Override
    public IsField<String> getDescriptionField() {
        return toolDesc;
    }

    @Override
    public IsField<String> getAttributionField() {
        return toolAttrib;
    }

    @Override
    public IsField<String> getSourceURLField() {
        return binLink;
    }

    @Override
    public IsField<String> getDocURLField() {
        return toolDoc;
    }

    @Override
    public IsField<String> getVersionField() {
        return toolVersion;
    }

    @Override
    public IsField<Y> getMultithreadedField() {
        return multiThreadCbo;
    }

    @Override
    public IsField<String> getInstructionsField() {
        return runInfo;
    }

    @Override
    public IsField<String> getAdditionalInfoField() {
        return otherInfo;
    }

    @Override
    public IsField<A> getArchitectureField() {
        return archCbo;
    }

}
