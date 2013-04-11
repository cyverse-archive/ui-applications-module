package org.iplantc.core.uiapps.client.views;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.iplantc.core.resources.client.IplantResources;
import org.iplantc.core.uiapps.client.models.autobeans.App;
import org.iplantc.core.uiapps.client.models.autobeans.AppAutoBeanFactory;
import org.iplantc.core.uiapps.client.models.autobeans.AppGroup;
import org.iplantc.core.uiapps.client.models.autobeans.AppRefLink;
import org.iplantc.core.uicommons.client.validators.UrlValidator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckNodes;
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
    TextArea appDesc;

    @UiField
    VerticalLayoutContainer container;

    @UiField(provided = true)
    TreeStore<AppGroup> treeStore;

    @UiField(provided = true)
    Tree<AppGroup, String> tree;

    @UiField
    Grid<AppRefLink> grid;

    @UiField
    ListStore<AppRefLink> listStore;

    @UiField
    TextButton addBtn;

    @UiField
    TextButton delBtn;

    private GridEditing<AppRefLink> editing;

    private Presenter presenter;

    private App selectedApp;

    @UiTemplate("SubmitAppForPublicUseView.ui.xml")
    interface SubmitAppForPublicUseViewUiBinder extends UiBinder<Widget, SubmitAppForPublicUseViewImpl> {
    }

    public SubmitAppForPublicUseViewImpl(App selectedApp) {
        initCategoryTree();
        this.selectedApp = selectedApp;
        setTreeIcons();
        tree.setCheckable(true);
        tree.setCheckStyle(CheckCascade.CHILDREN);
        tree.setCheckNodes(CheckNodes.LEAF);
        widget = uiBinder.createAndBindUi(this);
        CellSelectionModel<AppRefLink> csm = new CellSelectionModel<AppRefLink>();
        csm.addCellSelectionChangedHandler(new CellSelectionChangedHandler<AppRefLink>() {
            @Override
            public void onCellSelectionChanged(CellSelectionChangedEvent<AppRefLink> event) {
                if (event.getSelection().size() > 0) {
                    delBtn.enable();
                } else {
                    delBtn.disable();
                }

            }
        });
        grid.setSelectionModel(csm);
        editing = createGridEditing();
        ColumnConfig<AppRefLink, String> cc = grid.getColumnModel().getColumn(0);
        editing.addEditor(cc, buildRefLinkEditor());
        container.setScrollMode(ScrollMode.AUTOY);
        appName.setValue(selectedApp.getName());
    }

    private void initCategoryTree() {
        treeStore = new TreeStore<AppGroup>(new ModelKeyProvider<AppGroup>() {

            @Override
            public String getKey(AppGroup item) {
                return item.getId();
            }
        });
        tree = new Tree<AppGroup, String>(treeStore, new ValueProvider<AppGroup, String>() {

            @Override
            public String getValue(AppGroup object) {
                return object.getName();
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
    }

    @UiFactory
    ListStore<AppRefLink> buildRefLinksStore() {
        return new ListStore<AppRefLink>(new ModelKeyProvider<AppRefLink>() {

            @Override
            public String getKey(AppRefLink item) {
                return item.getId();
            }
        });

    }

    @UiFactory
    ColumnModel<AppRefLink> buildRefLinkColumn() {
        List<ColumnConfig<AppRefLink, ?>> columns = new ArrayList<ColumnConfig<AppRefLink, ?>>();

        ColumnConfig<AppRefLink, String> link = new ColumnConfig<AppRefLink, String>(
                new ValueProvider<AppRefLink, String>() {

                    @Override
                    public String getValue(AppRefLink object) {
                        return object.getRefLink();
                    }

                    @Override
                    public void setValue(AppRefLink object, String value) {
                        // do nothing

                    }

                    @Override
                    public String getPath() {

                        return null;
                    }
                }, 200);

        link.setHideable(false);
        link.setMenuDisabled(true);
        columns.add(link);
        return new ColumnModel<AppRefLink>(columns);
    }

    private TextField buildRefLinkEditor() {
        TextField field = new TextField();
        field.addValidator(new UrlValidator());
        field.setAllowBlank(false);
        return field;
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
        this.presenter = p;

    }

    @UiHandler("addBtn")
    public void addClicked(SelectEvent event) {
        AppAutoBeanFactory factory = GWT.create(AppAutoBeanFactory.class);
        AutoBean<AppRefLink> bean = AutoBeanCodex.decode(factory, AppRefLink.class, "{}");
        editing.cancelEditing();
        AppRefLink link = bean.as();
        link.setId(new Date().getTime() + "");
        listStore.add(0, link);
        editing.startEditing(new GridCell(0, 0));
    }

    @UiHandler("delBtn")
    public void delClicked(SelectEvent event) {
        List<AppRefLink> links = grid.getSelectionModel().getSelectedItems();
        for (AppRefLink link : links) {
            listStore.remove(link);
        }
    }

    @Override
    public TreeStore<AppGroup> getTreeStore() {
        return treeStore;
    }

    @Override
    public void expandAppGroups() {
        tree.expandAll();
    }

    private GridEditing<AppRefLink> createGridEditing() {
        return new GridInlineEditing<AppRefLink>(grid);
    }

    @Override
    public boolean validate() {
        return appName.getCurrentValue() != null && appName.getCurrentValue().length() <= 255
                && appDesc.getCurrentValue() != null && checkRefLinksGrid() && checkAppGroups();

    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("analysis_id", getJsonString(selectedApp.getId())); //$NON-NLS-1$
        json.put("name", getJsonString(appName.getValue()));
        json.put("desc", getJsonString(appDesc.getValue())); //$NON-NLS-1$
        json.put("groups", buildSelectedCategoriesAsJson()); //$NON-NLS-1$
        json.put("references", buildRefLinksAsJson()); //$NON-NLS-1$

        return json;
    }

    private boolean checkRefLinksGrid() {
        if (listStore.size() > 0) {
            for (AppRefLink link : listStore.getAll()) {
                if (link.getRefLink() == null || link.getRefLink().isEmpty()) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }

    }

    private boolean checkAppGroups() {
        if (tree.getCheckedSelection().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private JSONValue buildRefLinksAsJson() {
        JSONArray arr = new JSONArray();
        int index = 0;
        for (AppRefLink model : listStore.getAll()) {
            arr.set(index++, new JSONString(model.getRefLink()));
        }
        return arr;
    }

    private JSONString getJsonString(String value) {
        return new JSONString(value == null ? "" : value); //$NON-NLS-1$
    }

    private JSONArray buildSelectedCategoriesAsJson() {
        JSONArray arr = new JSONArray();
        int index = 0;
        for (AppGroup model : tree.getCheckedSelection()) {
            arr.set(index++, new JSONString(model.getId()));
        }
        return arr;
    }
    //

}
