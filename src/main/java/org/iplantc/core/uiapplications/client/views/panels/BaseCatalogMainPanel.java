package org.iplantc.core.uiapplications.client.views.panels;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.models.Analysis;
import org.iplantc.core.uiapplications.client.models.AnalysisFeedback;
import org.iplantc.core.uiapplications.client.models.AnalysisGroup;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uicommons.client.Constants;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.widgets.BetterQuickTip;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.core.XTemplate;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.IconButton;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.RowExpander;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;

/**
 * 
 * Displays a list of analyses in grid. Actions to edit, copy and run are supported
 * 
 * @author sriram
 * 
 */
public class BaseCatalogMainPanel extends ContentPanel {

    protected ToolBar toolBar;
    protected Grid<Analysis> analysisGrid;
    private RowExpander expander;
    private String appIdToSelect;
    protected AnalysisGroup current_category;
    protected String tag;
    protected AppTemplateServiceFacade templateService;

    public static enum RATING_CONSTANT {

        HATE_IT(I18N.DISPLAY.hateIt()), 
        DID_NOT_LIKE_IT(I18N.DISPLAY.didNotLike()), 
        LIKED_IT(I18N.DISPLAY.likedIt()), 
        REALLY_LIKED_IT(I18N.DISPLAY.reallyLikedIt()), 
        LOVED_IT(I18N.DISPLAY.lovedIt());

        private String displayText;

        private RATING_CONSTANT(String displaytext) {
            this.displayText = displaytext;
        }

        /**
         * Returns a string that identifies the RATING_CONSTANT.
         * 
         * @return
         */
        public String getTypeString() {
            return toString().toLowerCase();
        }

        /**
         * Null-safe and case insensitive variant of valueOf(String)
         * 
         * @param typeString name of an EXECUTION_STATUS constant
         * @return
         */
        public static RATING_CONSTANT fromTypeString(String typeString) {
            if (typeString == null || typeString.isEmpty()) {
                return null;
            }

            return valueOf(typeString.toUpperCase());
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    public BaseCatalogMainPanel(String tag, AppTemplateServiceFacade templateService) {
        this.tag = tag;
        this.templateService = templateService;

        setLayout(new FitLayout());
        initToolBar();
        initGrid();
    }

    private void initGrid() {
        analysisGrid = new Grid<Analysis>(new ListStore<Analysis>(), buildColumnModel());

        new BetterQuickTip(analysisGrid);

        analysisGrid.setSelectionModel(buildSelectionModel());
        analysisGrid.addPlugin(expander);
        analysisGrid.getView().setEmptyText(I18N.DISPLAY.noAnalyses());
        analysisGrid.getView().setForceFit(true);
        add(analysisGrid);
    }

    public void addGridSelectionChangeListener(Listener<BaseEvent> listener) {
        analysisGrid.getSelectionModel().addListener(Events.SelectionChange, listener);
    }

    protected GridSelectionModel<Analysis> buildSelectionModel() {
        GridSelectionModel<Analysis> selectionModel = new GridSelectionModel<Analysis>();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        return selectionModel;
    }

    private void initExpander() {
        XTemplate tpl = XTemplate.create(buildAppDetailsTemplate());

        expander = new RowExpander();
        expander.setTemplate(tpl);
    }

    /**
     * Builds the string used for the XTemplate in the row expander that displays the App details.
     * 
     * @return A string used for the RowExpander XTemplate for the App grid.
     */
    protected String buildAppDetailsTemplate() {
        StringBuilder tmpl = new StringBuilder();

        // Description line
        tmpl.append("<tpl if=\"description\"><p>{description}</p></tpl>"); //$NON-NLS-1$

        // Contact line showing the integrator's email
        tmpl.append("<p><b>"); //$NON-NLS-1$
        tmpl.append(I18N.DISPLAY.integrator());
        tmpl.append(":</b> "); //$NON-NLS-1$
        tmpl.append("<a href='mailto:{integrator_email}' style='color:#0098AA;'>{integrator_email}</a>"); //$NON-NLS-1$
        tmpl.append("<br/>"); //$NON-NLS-1$

        // Links line
        tmpl.append("<b>"); //$NON-NLS-1$
        tmpl.append(I18N.DISPLAY.links());
        tmpl.append(":</b> "); //$NON-NLS-1$

        // Wiki link
        tmpl.append("<tpl if=\"wiki_url\">&nbsp;&nbsp;"); //$NON-NLS-1$
        tmpl.append("<a href='{wiki_url}' target= '_blank' style='color:#0098AA;'><b>"); //$NON-NLS-1$
        tmpl.append(I18N.DISPLAY.documentation());
        tmpl.append("</b></a></tpl>"); //$NON-NLS-1$

        // Forums link
        tmpl.append("&nbsp;&nbsp;<a href=' "); //$NON-NLS-1$
        tmpl.append(Constants.CLIENT.forumsUrl());
        tmpl.append("' target='_blank' style='color:#0098AA;'><b>"); //$NON-NLS-1$
        tmpl.append(I18N.DISPLAY.forums());
        tmpl.append("</b></a>"); //$NON-NLS-1$

        // Avg. Rating line
        tmpl.append("<br /><b>"); //$NON-NLS-1$
        tmpl.append(I18N.DISPLAY.avgCommunityRating());
        tmpl.append(":</b> {average} "); //$NON-NLS-1$
        tmpl.append(I18N.DISPLAY.ratingOutOfTotal());

        tmpl.append("</p>"); //$NON-NLS-1$

        return tmpl.toString();
    }

    public void seed(List<Analysis> analyses, AnalysisGroup group) {
        current_category = group;
        if (analysisGrid != null) {
            analysisGrid.getStore().removeAll();
            analysisGrid.getStore().add(analyses);
            analysisGrid.unmask();

            if (appIdToSelect != null) {
                selectToolNow(appIdToSelect);
                appIdToSelect = null;
            }
        }
    }

    protected void initToolBar() {
        toolBar = new CatalogMainToolBar(tag, templateService);

        setTopComponent(toolBar);
    }

    protected void addToToolBar(Component component) {
        toolBar.add(component);
    }

    protected ColumnModel buildColumnModel() {
        initExpander();

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(expander);

        ColumnConfig name = new ColumnConfig();
        name.setRenderer(new RenderCellWithToolTip());
        name.setId(Analysis.NAME);
        name.setHeader(I18N.DISPLAY.name());
        name.setWidth(180);

        ColumnConfig integrator = new ColumnConfig();
        integrator.setId(Analysis.INTEGRATOR_NAME);
        integrator.setHeader(I18N.DISPLAY.integratedby());
        integrator.setWidth(130);

        ColumnConfig date = new ColumnConfig();
        date.setId(Analysis.INTEGRATION_DATE);
        date.setHeader(I18N.DISPLAY.publishedOn());
        date.setWidth(125);
        date.setDateTimeFormat(DateTimeFormat
                .getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM));

        ColumnConfig rate = new ColumnConfig();
        rate.setId(Analysis.RATING);
        rate.setRenderer(new RenderVotingCell());
        rate.setHeader("Rating"); //$NON-NLS-1$
        rate.setWidth(105);

        configs.add(name);
        configs.add(integrator);
        configs.add(date);
        configs.add(rate);

        return new ColumnModel(configs);
    }

    /**
     * Selects an application by ID. If the grid store isn't loaded with this application yet, it's
     * selected the next time the grid store is reloaded.
     * 
     * @param appID the ID of the application to select
     * @see #seed(List)
     */
    public void selectTool(String appID) {
        appIdToSelect = appID;

        if (appID == null) {
            return;
        }

        if (analysisGrid.getStore().findModel(Analysis.ID, appIdToSelect) != null) {
            selectToolNow(appIdToSelect);
        }
    }

    /**
     * Selects an application by appIdToSelect. If the grid store isn't loaded with this application, an
     * error pop-up is displayed.
     * 
     * @param appID
     */
    private void selectToolNow(String appID) {
        Analysis app = analysisGrid.getStore().findModel(Analysis.ID, appID);

        if (app != null) {
            analysisGrid.getSelectionModel().select(app, false);
        } else {
            ErrorHandler.post(org.iplantc.core.uicommons.client.I18N.ERROR.appNotFound());
        }
    }

    /**
     * Returns the selected app, or null if no app is selected.
     * 
     * @return the selected app
     */
    public Analysis getSelectedApp() {
        return analysisGrid.getSelectionModel().getSelectedItem();
    }
    
    private class RenderCellWithToolTip implements GridCellRenderer<Analysis> {
        @Override
        public Object render(Analysis model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<Analysis> store, Grid<Analysis> grid) {
            String html = ""; //$NON-NLS-1$
            if (model.isUser_favourite()) {
                html = "<img src='./images/fav.png'></img> &nbsp;"; //$NON-NLS-1$
            }

            if (model.getDescription() != null && !model.getDescription().isEmpty()
                    && !model.getDescription().equalsIgnoreCase("none")) { //$NON-NLS-1$
                return html + "<span qtip='" + model.getDescription() + "'>" + model.getName() //$NON-NLS-1$ //$NON-NLS-2$
                        + "</span>"; //$NON-NLS-1$
            } else {
                return html + "<span qtip='" + model.getName() + "'>" + model.getName() + "</span>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }

        }

    }

    protected class RenderVotingCell implements GridCellRenderer<Analysis> {

        protected final ArrayList<String> ratings;

        public RenderVotingCell() {
            super();

            ratings = new ArrayList<String>();
            ratings.add(0, RATING_CONSTANT.HATE_IT.displayText);
            ratings.add(1, RATING_CONSTANT.DID_NOT_LIKE_IT.displayText);
            ratings.add(2, RATING_CONSTANT.LIKED_IT.displayText);
            ratings.add(3, RATING_CONSTANT.REALLY_LIKED_IT.displayText);
            ratings.add(4, RATING_CONSTANT.LOVED_IT.displayText);
        }

        @Override
        public Object render(Analysis model, String property, ColumnData config, int rowIndex,
                int colIndex, ListStore<Analysis> store, Grid<Analysis> grid) {
            // only show rating stars if the app is public
            if (!model.isPublic()) {
                return new Label();
            }
            
            final AnalysisFeedback af = model.getFeedback();

            final HorizontalPanel hp = buildPanel(model);

            if (af != null) {
                resetRatingStarColors(af, hp);

                hp.addListener(Events.OnMouseOut, new Listener<BaseEvent>() {
                    @Override
                    public void handleEvent(BaseEvent be) {
                        resetRatingStarColors(af, hp);
                    }
                });
            }

            return hp;
        }

        protected HorizontalPanel buildPanel(Analysis model) {
            final HorizontalPanel hp = new HorizontalPanel();
            hp.setSize(100, 20);

            for (int i = 0; i < ratings.size(); i++) {
                hp.add(buildStar("apps_rating_white_button", i, model)); //$NON-NLS-1$
            }

            return hp;
        }

        protected IconButton buildStar(String style, int index, final Analysis model) {
            IconButton icon = new IconButton(style);

            String ratingDisplay = ratings.get(index);

            icon.setToolTip(ratingDisplay);
            icon.setId(ratingDisplay);
            icon.setData("index", index); //$NON-NLS-1$

            return icon;
        }

        protected void resetRatingStarColors(AnalysisFeedback userFeedback, LayoutContainer pnlParent) {
            IconButton icon = null;

            int i = 0;
            if (userFeedback.getUser_score() != 0) {
                for (i = 0; i < userFeedback.getUser_score(); i++) {
                    icon = getIcon(pnlParent, i);
                    icon.setStyleName("apps_rating_gold_button"); //$NON-NLS-1$
                }
            } else {
                for (i = 0; i < Math.floor(userFeedback.getAverage_score()); i++) {
                    icon = getIcon(pnlParent, i);
                    icon.setStyleName("apps_rating_red_button"); //$NON-NLS-1$
                }
            }

            for (int j = i; j < ratings.size(); j++) {
                icon = getIcon(pnlParent, j);
                icon.setStyleName("apps_rating_white_button"); //$NON-NLS-1$
            }

        }

        /**
         * Returns the index-th icon button in RenderVotingCell
         * 
         * @param pnlParent
         * @param index
         * @return
         */
        protected IconButton getIcon(LayoutContainer pnlParent, int index) {
            return (IconButton)pnlParent.getItem(index);
        }

    }
}
