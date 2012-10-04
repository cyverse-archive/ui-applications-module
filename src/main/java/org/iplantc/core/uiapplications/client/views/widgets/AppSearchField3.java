package org.iplantc.core.uiapplications.client.views.widgets;


import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppProperties;
import org.iplantc.core.uiapplications.client.views.cells.AppListViewCell;
import org.iplantc.core.uiapplications.client.views.widgets.events.AppSearch3ResultLoadEvent;
import org.iplantc.core.uiapplications.client.views.widgets.events.AppSearch3ResultSelectedEvent;
import org.iplantc.core.uiapplications.client.views.widgets.proxy.AppListLoadResult;
import org.iplantc.core.uiapplications.client.views.widgets.proxy.AppLoadConfig;
import org.iplantc.core.uiapplications.client.views.widgets.proxy.AppSearchAutoBeanFactory;
import org.iplantc.core.uiapplications.client.views.widgets.proxy.AppSearchRpcProxy3;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
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

    private ComboBox<App> combo;

    public AppSearchField3() {
        // Create liststore
        // Create Listview and set listView cell

        // Create combobox cell
    }

    @Override
    public Widget asWidget() {
        if (combo == null) {

            AppSearchRpcProxy3 proxy = new AppSearchRpcProxy3();

            PagingLoader<AppLoadConfig, AppListLoadResult> loader = new PagingLoader<AppLoadConfig, AppListLoadResult>(
                    proxy);
            loader.useLoadConfig(AppSearchAutoBeanFactory.instance.loadConfig().as());
            // Ensure that the query string is placed into the
            loader.addBeforeLoadHandler(new BeforeLoadHandler<AppLoadConfig>() {
                @Override
                public void onBeforeLoad(BeforeLoadEvent<AppLoadConfig> event) {
                    String query = combo.getText();
                    if (query != null && !query.equals("")) {
                        event.getLoadConfig().setQuery(query);
                    }
                }
            });

            AppProperties props = GWT.create(AppProperties.class);

            final ListStore<App> store = new ListStore<App>(props.id());
            loader.addLoadHandler(new LoadResultListStoreBinding<AppLoadConfig, App, AppListLoadResult>(
                    store));

            ListView<App, App> view = new ListView<App, App>(store,
                    new IdentityValueProvider<App>());
            view.setCell(new AppListViewCell());

            ComboBoxCell<App> cell = new ComboBoxCell<App>(store, props.nameLabel(), view);

            cell.setLoader(loader);
            combo = new ComboBox<App>(cell);
            combo.setMinChars(3);
            combo.setLoader(loader);
            combo.addBeforeSelectionHandler(new BeforeSelectionHandler<App>() {
                @Override
                public void onBeforeSelection(BeforeSelectionEvent<App> event) {
                    event.cancel();
                    App selectedApp = event.getItem();
                    EventBus.getInstance().fireEvent(
                            new AppSearch3ResultSelectedEvent(selectedApp.getId(), selectedApp
                                    .getGroupId()));
                }
            });
            combo.addTriggerClickHandler(new TriggerClickHandler() {

                @Override
                public void onTriggerClick(TriggerClickEvent event) {
                    EventBus.getInstance().fireEvent(new AppSearch3ResultLoadEvent(store.getAll()));
                }
            });

            combo.getElement().getStyle().setMargin(10, Unit.PX);

        }

        return combo;
    }

    public Cell<?> getListViewCell() {
        return combo.getCell().getListView().getCell();
    }

}
