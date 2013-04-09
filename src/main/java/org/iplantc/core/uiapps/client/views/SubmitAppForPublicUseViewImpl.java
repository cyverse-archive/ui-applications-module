package org.iplantc.core.uiapps.client.views;

import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.TreeAppearance;

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

    @UiField
    VerticalLayoutContainer container;

    @UiField(provided = true)
    TreeStore<AppGroup> treeStore;

    @UiField(provided = true)
    Tree<AppGroup, String> tree;

    @UiTemplate("SubmitAppForPublicUseView.ui.xml")
    interface SubmitAppForPublicUseViewUiBinder extends UiBinder<Widget, SubmitAppForPublicUseViewImpl> {
    }

    public SubmitAppForPublicUseViewImpl(App selectedApp) {
        treeStore = new TreeStore<AppGroup>(new ModelKeyProvider<AppGroup>() {

            @Override
            public String getKey(AppGroup item) {
                return item.getId();
            }
        });
        tree = new Tree<AppGroup, String>(treeStore, new ValueProvider<AppGroup, String>() {

            @Override
            public String getValue(AppGroup object) {
                return object.getName() + " (" + object.getAppCount() + ")";
            }

            @Override
            public void setValue(AppGroup object, String value) {
                // do nothing intentionally
            }

            @Override
            public String getPath() {
                return null;
            }
        });

        setTreeIcons();
        tree.setCheckable(true);
        widget = uiBinder.createAndBindUi(this);
        container.setScrollMode(ScrollMode.AUTOY);
        appName.setValue(selectedApp.getName());
    }

    /**
     * FIXME JDS This needs to be implemented in an {@link TreeAppearance}
     */
    private void setTreeIcons() {
        com.sencha.gxt.widget.core.client.tree.TreeStyle style = tree.getStyle();
        style.setNodeCloseIcon(IplantResources.RESOURCES.category());
        style.setNodeOpenIcon(IplantResources.RESOURCES.category_open());
        style.setLeafIcon(IplantResources.RESOURCES.subCategory());
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

    @Override
    public TreeStore<AppGroup> getTreeStore() {
        return treeStore;
    }

    @Override
    public void expandAppGroups() {
        tree.expandAll();
    }

}
