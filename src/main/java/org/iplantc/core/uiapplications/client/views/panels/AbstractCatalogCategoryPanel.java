package org.iplantc.core.uiapplications.client.views.panels;

import java.util.List;

import org.iplantc.core.uiapplications.client.events.AnalysisCategorySelectedEvent;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uiapplications.client.models.AnalysisGroupTreeModel;
import org.iplantc.core.uicommons.client.I18N;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.images.Resources;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Status;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;
import com.extjs.gxt.ui.client.widget.treepanel.TreeStyle;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

/**
 * 
 * A panel that displays a list of categories
 * 
 * @author sriram
 * 
 */
public abstract class AbstractCatalogCategoryPanel extends ContentPanel {

    protected TreePanel<AnalysisGroupTreeModel> categoryPanel;
    private String defaultCategoryId;

    public AbstractCatalogCategoryPanel() {
        setHeading(I18N.DISPLAY.category());
        setLayout(new FitLayout());

        Status pleaseWait = new Status();
        pleaseWait.setBusy(""); //$NON-NLS-1$

        add(pleaseWait);
    }

    public void seed(TreeStore<AnalysisGroupTreeModel> models) {
        removeAll();
        categoryPanel = new TreePanel<AnalysisGroupTreeModel>(models);
        categoryPanel.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        initListeners();
        setTreeIcons();
        add(categoryPanel);
        categoryPanel.setDisplayProperty("display_name"); //$NON-NLS-1$
        layout(true);
    }

    protected void initListeners() {
        categoryPanel.addListener(Events.Render, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                categoryPanel.expandAll();
            }
        });

        categoryPanel.getSelectionModel().addListener(Events.SelectionChange, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                AnalysisGroup ag = (AnalysisGroup)categoryPanel.getSelectionModel().getSelectedItem();
                fireCategorySelectedEvent(ag);

            }
        });

        categoryPanel.addListener(Events.Render, new Listener<BaseEvent>() {
            @Override
            public void handleEvent(BaseEvent be) {
                selectDefault();
            }
        });

    }

    protected void fireCategorySelectedEvent(AnalysisGroup ag) {
        if (ag != null) {
            AnalysisCategorySelectedEvent event = new AnalysisCategorySelectedEvent(ag, this);
            EventBus.getInstance().fireEvent(event);
        }
    }

    private void setTreeIcons() {
        TreeStyle style = categoryPanel.getStyle();
        style.setNodeCloseIcon(AbstractImagePrototype.create(Resources.ICONS.category()));
        style.setNodeOpenIcon(AbstractImagePrototype.create(Resources.ICONS.category_open()));
        style.setLeafIcon(AbstractImagePrototype.create(Resources.ICONS.subCategory()));
    }

    /**
     * Selects a category by ID. If the tree isn't populated yet, the category is set after population.
     * 
     * @param categoryId The ID of the category to select.
     */
    public void selectCategory(String categoryId) {
        setDefaultCategoryId(categoryId);

        if (categoryPanel != null && categoryPanel.getStore() != null
                && categoryPanel.getStore().getAllItems() != null) {
            selectDefault();
        }
    }

    protected void setDefaultCategoryId(String categoryId) {
        defaultCategoryId = categoryId;
    }

    /**
     * Selects the root category, i.e. the workspace. If seed() hasn't been called yet, nothing happens.
     */
    public void selectDefault() {
        if (categoryPanel != null) {
            categoryPanel.getSelectionModel().deselectAll();

            AnalysisGroupTreeModel model = null;
            if (defaultCategoryId != null) {
                model = categoryPanel.getStore().findModel(AnalysisGroupTreeModel.ID, defaultCategoryId);
                defaultCategoryId = null;
            }

            if (model == null) {
                List<AnalysisGroupTreeModel> roots = categoryPanel.getStore().getRootItems();
                if (roots != null && !roots.isEmpty()) {
                    model = roots.get(0);
                }
            }

            if (model != null) {
                categoryPanel.setExpanded(model, true, true);
                categoryPanel.getSelectionModel().select(model, false);
            }
        }
    }
}
