package org.iplantc.core.uiapplications.client.views.widgets;


import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.events.AppSearchResultSelectedEvent;
import org.iplantc.core.uiapplications.client.models.autobeans.Analysis;
import org.iplantc.core.uiapplications.client.models.autobeans.AnalysisProperties;
import org.iplantc.core.uiapplications.client.services.AppTemplateServiceFacade;
import org.iplantc.core.uiapplications.client.views.widgets.proxy.AnalysisListLoadResult;
import org.iplantc.core.uiapplications.client.views.widgets.proxy.AnalysisLoadConfig;
import org.iplantc.core.uiapplications.client.views.widgets.proxy.AppSearchAutoBeanFactory;
import org.iplantc.core.uiapplications.client.views.widgets.proxy.AppSearchRpcProxy3;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent;
import com.sencha.gxt.data.shared.loader.BeforeLoadEvent.BeforeLoadHandler;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent.TriggerClickHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class AppSearchField3 implements IsWidget {

    private ComboBox<Analysis> combo;

    public AppSearchField3() {
        // Create liststore
        // Create Listview and set listView cell

        // Create combobox cell
    }

    @Override
    public Widget asWidget() {
        if (combo == null) {

            AppTemplateServiceFacade templateService = GWT.create(AppTemplateServiceFacade.class);
            AppSearchRpcProxy3 proxy = new AppSearchRpcProxy3(templateService);

            PagingLoader<AnalysisLoadConfig, AnalysisListLoadResult> loader = new PagingLoader<AnalysisLoadConfig, AnalysisListLoadResult>(
                    proxy);
            loader.useLoadConfig(AppSearchAutoBeanFactory.instance.loadConfig().as());
            // Ensure that the query string is placed into the
            loader.addBeforeLoadHandler(new BeforeLoadHandler<AnalysisLoadConfig>() {
                @Override
                public void onBeforeLoad(BeforeLoadEvent<AnalysisLoadConfig> event) {
                    String query = combo.getText();
                    if (query != null && !query.equals("")) {
                        event.getLoadConfig().setQuery(query);
                    }
                }
            });

            AnalysisProperties props = GWT.create(AnalysisProperties.class);

            ListStore<Analysis> store = new ListStore<Analysis>(props.id());
            loader.addLoadHandler(new LoadResultListStoreBinding<AnalysisLoadConfig, Analysis, AnalysisListLoadResult>(
                    store));

            ListView<Analysis, Analysis> view = new ListView<Analysis, Analysis>(store,
                    new IdentityValueProvider<Analysis>());
            view.setCell(new AnalysisListViewCell());
            

            ComboBoxCell<Analysis> cell = new ComboBoxCell<Analysis>(store, props.name(), view);

            cell.setLoader(loader);
            combo = new ComboBox<Analysis>(cell);
            combo.setWidth(255);
            combo.setMinChars(3);
            combo.setLoader(loader);
            combo.addBeforeSelectionHandler(new BeforeSelectionHandler<Analysis>() {
                @Override
                public void onBeforeSelection(BeforeSelectionEvent<Analysis> event) {
                    event.cancel();
                    Analysis selectedAnalysis = event.getItem();
                    EventBus.getInstance().fireEvent(
                            new AppSearchResultSelectedEvent("", selectedAnalysis.getGroupId(),
                                    selectedAnalysis.getId()));
                }
            });
            combo.addTriggerClickHandler(new TriggerClickHandler() {

                @Override
                public void onTriggerClick(TriggerClickEvent event) {
                    // TODO Auto-generated method stub

                }
            });

            combo.getElement().getStyle().setMargin(10, Unit.PX);

        }

        return combo;
    }

    interface ExampleTemplate extends XTemplates {
        @XTemplate("<div class=\"search-item\"><h3>{post.name}</h3></div>")
        SafeHtml render(Analysis post);
    }

    /**
     * TODO JDS
     * 
     * @author jstroot
     * 
     */
    private final class AnalysisListViewCell extends AbstractCell<Analysis> {
        // final ExampleTemplate template = GWT.create(ExampleTemplate.class);

        @Override
        public void render(com.google.gwt.cell.client.Cell.Context context, Analysis value,
                SafeHtmlBuilder sb) {
            String searchItem = "search-item";//$NON-NLS-1$
            // sb.append(template.render(value));
            sb.append(SafeHtmlUtils.fromTrustedString("<div class=\"" + searchItem + "\""));
            sb.append(SafeHtmlUtils.fromTrustedString("<h3>"));
            if (value.isFavorite()) {
                // TODO JDS Fix hard-coded use of image.
                sb.append(SafeHtmlUtils.fromTrustedString("<img src='./images/fav.png'></img> &nbsp;"));//$NON-NLS-1$
            }
            sb.append(SafeHtmlUtils.fromString(value.getName()));
            sb.append(SafeHtmlUtils.fromTrustedString("<span><b>"));
            sb.append(SafeHtmlUtils.fromTrustedString(I18N.DISPLAY.avgRating()));
            sb.append(SafeHtmlUtils.fromTrustedString("</b> " + value.getRating().getAverageRating()
                    + " "));
            sb.append(SafeHtmlUtils.fromTrustedString(I18N.DISPLAY.ratingOutOfTotal()));
            sb.append(SafeHtmlUtils.fromTrustedString("</span>"));
            sb.append(SafeHtmlUtils.fromTrustedString("</h3>"));
            sb.append(SafeHtmlUtils.fromTrustedString("<h4>"));
            sb.append(SafeHtmlUtils.fromTrustedString("<span>"));
            sb.append(SafeHtmlUtils.fromString(value.getGroupName()));
            sb.append(SafeHtmlUtils.fromTrustedString("</span>"));
            sb.append(SafeHtmlUtils.fromTrustedString("<br />"));
            sb.append(SafeHtmlUtils.fromString(value.getDescription()));
            sb.append(SafeHtmlUtils.fromTrustedString("</h4>"));
            sb.append(SafeHtmlUtils.fromTrustedString("</div>"));

            //            template.append("<tpl for=\".\"><div class=\"search-item\">"); //$NON-NLS-1$
            //
            //            template.append("<h3>"); //$NON-NLS-1$
            //
            //            template.append("<tpl if=\"is_favorite\">"); //$NON-NLS-1$
            //            template.append("<img src='./images/fav.png'></img> &nbsp;"); //$NON-NLS-1$
            //            template.append("</tpl>"); //$NON-NLS-1$
            //
            //            template.append("{name}"); //$NON-NLS-1$
            //
            //            template.append("<span><b>"); //$NON-NLS-1$
            // template.append(I18N.DISPLAY.avgRating());
            //            template.append(":</b> {average} "); //$NON-NLS-1$
            // template.append(I18N.DISPLAY.ratingOutOfTotal());
            //            template.append("</span>"); //$NON-NLS-1$
            //
            //            template.append("</h3>"); //$NON-NLS-1$
            //
            //            template.append("<h4>"); //$NON-NLS-1$
            //            template.append("<span>{group_name}</span>"); //$NON-NLS-1$
            //            template.append("<br />{description}"); //$NON-NLS-1$
            //            template.append("</h4>"); //$NON-NLS-1$
            //
            //            template.append("</div></tpl>"); //$NON-NLS-1$
        }
    }

}
