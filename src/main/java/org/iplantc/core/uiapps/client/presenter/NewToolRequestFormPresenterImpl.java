/**
 * 
 */
package org.iplantc.core.uiapps.client.presenter;

import java.util.List;
import java.util.Set;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.client.views.NewToolRequestFormView;
import org.iplantc.core.uiapps.client.views.NewToolRequestFormView.Presenter;
import org.iplantc.core.uiapps.client.views.Uploader;
import org.iplantc.core.uicommons.client.gin.ServicesInjector;
import org.iplantc.core.uicommons.client.models.HasPaths;
import org.iplantc.core.uicommons.client.models.UserInfo;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceAutoBeanFactory;
import org.iplantc.core.uicommons.client.models.diskresources.DiskResourceExistMap;
import org.iplantc.core.uicommons.client.models.toolrequests.Architecture;
import org.iplantc.core.uicommons.client.models.toolrequests.NewToolRequest;
import org.iplantc.core.uicommons.client.models.toolrequests.RequestedToolDetails;
import org.iplantc.core.uicommons.client.models.toolrequests.ToolRequestFactory;
import org.iplantc.core.uicommons.client.models.toolrequests.YesNoMaybe;
import org.iplantc.core.uicommons.client.services.DiskResourceServiceFacade;
import org.iplantc.core.uicommons.client.services.ToolRequestProvider;
import org.iplantc.core.uicommons.client.util.DiskResourceUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;

/**
 * @author sriram
 *
 */
public class NewToolRequestFormPresenterImpl implements Presenter {

    private static final DiskResourceAutoBeanFactory FS_FACTORY = GWT.create(DiskResourceAutoBeanFactory.class);
    private static final ToolRequestFactory REQ_FACTORY = GWT.create(ToolRequestFactory.class);

    private final DiskResourceServiceFacade fsServices = ServicesInjector.INSTANCE.getDiskResourceServiceFacade();
    private final ToolRequestProvider reqServices = ServicesInjector.INSTANCE.getToolRequestServiceProvider();

    private NewToolRequestFormView<Architecture, YesNoMaybe> view;
    private Command callback;
    private boolean toolByUpload;
    
    public NewToolRequestFormPresenterImpl(final NewToolRequestFormView<Architecture, YesNoMaybe> view, final Command callbackCmd) {
        this.view = view;
        this.callback = callbackCmd;
        this.toolByUpload = true;
        view.setPresenter(this);
        view.setToolByUpload(toolByUpload);
    }

    /* (non-Javadoc)
     * @see org.iplantc.core.uicommons.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasOneWidget)
     */
    @Override
    public void go(final HasOneWidget container) {
        container.setWidget(view);
    }

    /**
     * @see Presenter#onCancelBtnClick()
     */
    @Override
    public void onCancelBtnClick() {
        executeCallback();
    }

    /**
     * @see Presenter#onSubmitBtnClick()
     */
    @Override
    public void onSubmitBtnClick() {
        if (isFormValid()) {
            view.indicateSubmissionStart();
            validateUploadsNew(getUploadersToSubmit(), submitCmd(indicateSuccessCmd));
        } else {
            view.indicateSubmissionFailure(I18N.ERROR.invalidToolRequest());
        }
    }

    /**
     * @see Presenter#onToolByUpload(boolean)
     */
    @Override
    public void onToolByUpload(final boolean byUpload) {
        toolByUpload = byUpload;
        view.setToolByUpload(byUpload);
    }

    private final Command indicateSuccessCmd = new Command() {
        @Override
        public void execute() {
            view.indicateSubmissionSuccess();
            executeCallback();
        }};

    private Command submitCmd(final Command onSuccess) {
        return new Command() {
            @Override
            public void execute() {
                final List<Uploader> uploaders = getUploadersToSubmit();
                UploadMux.startUploads(uploaders, new Callback<Void, Iterable<Uploader>>() {
                    @Override
                    public void onFailure(final Iterable<Uploader> failedUploaders) {
                        onUploadFailure(uploaders, failedUploaders);
                    }
                    @Override
                    public void onSuccess(Void unused) {
                        submitRequest(uploaders, onSuccess);
                    }
                });
        }};
    }

    private void validateUploadsNew(final Iterable<Uploader> uploaders, final Command onValid) {
        final List<String> destFiles = Lists.newArrayList();
        for (Uploader uploader : uploaders) {
            destFiles.add(makeDestinationPath(uploader.getValue()));
        }
        getDiskResourceExistMap(destFiles, checkExistenceCmd(uploaders, onValid));
    }

    private Continuation<DiskResourceExistMap> checkExistenceCmd(final Iterable<Uploader> uploaders, final Command onNoneExist) {
        return new Continuation<DiskResourceExistMap>() {
            @Override
            public void execute(final DiskResourceExistMap fsExistMap) {
                boolean allNew = true;
                for (Uploader uploader : uploaders) {
                    if (fsExistMap.get(makeDestinationPath(uploader.getValue()))) {
                        uploader.markInvalid(I18N.ERROR.fileExist());
                        allNew = false;
                    }
                }
                if (allNew) {
                    onNoneExist.execute();
                } else {
                    String dups = "";
                    for (Uploader uploader : uploaders) {
                        dups += " " + uploader.getValue();
                    }
                    view.indicateSubmissionFailure(I18N.ERROR.fileExists(dups));
                }
            }
        };
    }
    
    private void getDiskResourceExistMap(final Iterable<String> files, final Continuation<DiskResourceExistMap> checkExistence) {
        final HasPaths dto = FS_FACTORY.pathsList().as();
        dto.setPaths(Lists.newArrayList(files));
        fsServices.diskResourcesExist(dto, new AsyncCallback<DiskResourceExistMap>() {
            @Override
            public void onFailure(final Throwable caught) {
                view.indicateSubmissionFailure(I18N.ERROR.newToolRequestError());
            }
            @Override
            public void onSuccess(final DiskResourceExistMap fsExistMap) {
                checkExistence.execute(fsExistMap);
            }
        });
    }

    private void submitRequest(final Iterable<Uploader> uploaders, final Command onSuccess) {
        final NewToolRequest req = getToolRequest();
        reqServices.requestInstallation(req, new AsyncCallback<RequestedToolDetails>() {
            @Override
            public void onFailure(final Throwable caught) {
                view.indicateSubmissionFailure(I18N.ERROR.newToolRequestError());
                removeUploads(uploaders);
            }
            @Override
            public void onSuccess(final RequestedToolDetails response) {
                onSuccess.execute();
            }
        });
    }

    private boolean isFormValid() {
        boolean valid = view.isValid();
        if (toolByUpload) {
            valid = !areUploadsSame(view.getToolBinaryUploader(), view.getTestDataUploader()) && valid;
            valid = !areUploadsSame(view.getToolBinaryUploader(), view.getOtherDataUploader()) && valid;
        }
        return !areUploadsSame(view.getTestDataUploader(), view.getOtherDataUploader()) && valid;
    }

    private boolean areUploadsSame(final Uploader lhs, final Uploader rhs) {
        if (lhs.getValue().equals(rhs.getValue())) {
            lhs.markInvalid(I18N.ERROR.duplicateUpload());
            rhs.markInvalid(I18N.ERROR.duplicateUpload());
            return true;
        }
        return false;
    }

    private void onUploadFailure(final Iterable<Uploader> allUploaders, final Iterable<Uploader> failedUploaders) {
        final Set<Uploader> succUploaders = Sets.newHashSet(allUploaders);
        final List<String> failedFiles = Lists.newArrayList();
        for (Uploader failure : failedUploaders) {
            failure.markInvalid(I18N.ERROR.fileUploadFailedAnon());
            succUploaders.remove(failure);
            failedFiles.add(failure.getValue());
        }
        view.indicateSubmissionFailure(I18N.ERROR.fileUploadsFailed(failedFiles));
        removeUploads(succUploaders);
    }

    private NewToolRequest getToolRequest() {
        final NewToolRequest req = REQ_FACTORY.makeNewToolRequest().as();
        req.setName(view.getNameField().getValue());
        req.setDescription(view.getDescriptionField().getValue());
        req.setAttribution(view.getAttributionField().getValue());
        if (toolByUpload) {
            req.setSourceFile(makeDestinationPath(getToolBinaryName()));
        } else {
            req.setSourceURL(view.getSourceURLField().getValue());
        }
        req.setDocURL(view.getDocURLField().getValue());
        req.setVersion(view.getVersionField().getValue());
        req.setArchitecture(view.getArchitectureField().getValue());
        if (view.getMultithreadedField().getValue() != YesNoMaybe.MAYBE) {
            req.setMultithreaded(view.getMultithreadedField().getValue());
        }
        req.setTestDataFile(makeDestinationPath(getTestDataName()));
        req.setInstructions(view.getInstructionsField().getValue());
        if (!view.getOtherDataUploader().getValue().isEmpty()) {
            req.setAdditionalDataFile(makeDestinationPath(getOtherDataName()));
        }
        req.setAdditionaInfo(view.getAdditionalInfoField().getValue());
        return req;
    }

    private void executeCallback() {
        if (callback != null) {
            callback.execute();
        }
    }

    private String getOtherDataName() {
        return view.getOtherDataUploader().getValue();
    }

    private String getTestDataName() {
        return view.getTestDataUploader().getValue();
    }

    private String getToolBinaryName() {
        return view.getToolBinaryUploader().getValue();
    }

    private String makeDestinationPath(final String file) {
        return DiskResourceUtil.appendNameToPath(UserInfo.getInstance().getHomePath(), file);
    }

    private List<Uploader> getUploadersToSubmit() {
        final List<Uploader> uploaders = Lists.newArrayList();
        if (toolByUpload) {
            uploaders.add(view.getToolBinaryUploader());
        }
        uploaders.add(view.getTestDataUploader());
        if (!view.getOtherDataUploader().getValue().isEmpty()) {
            uploaders.add(view.getOtherDataUploader());
        }
        return uploaders;
    }

    private void removeUploads(final Iterable<Uploader> uploaders) {
        final List<String> filesToDelete = Lists.newArrayList();
        for (Uploader uploader : uploaders) {
            filesToDelete.add(makeDestinationPath(uploader.getValue()));
        }
        if (!filesToDelete.isEmpty()) {
            final HasPaths dto = FS_FACTORY.pathsList().as();
            dto.setPaths(filesToDelete);
            fsServices.deleteDiskResources(dto, new AsyncCallback<HasPaths>() {
                @Override
                public void onFailure(final Throwable unused) {}
                @Override
                public void onSuccess(final HasPaths unused) {}
            });
        }
    }

}
