package org.iplantc.core.uiapplications.client.views;

import java.util.ArrayList;
import java.util.List;

import org.iplantc.core.uiapplications.client.CommonAppDisplayStrings;
import org.iplantc.core.uiapplications.client.I18N;
import org.iplantc.core.uiapplications.client.Services;
import org.iplantc.core.uiapplications.client.models.autobeans.App;
import org.iplantc.core.uiapplications.client.models.autobeans.AppProperties;
import org.iplantc.core.uiapplications.client.services.AppUserServiceFacade;
import org.iplantc.core.uiapplications.client.views.cells.AppHyperlinkCell;
import org.iplantc.core.uiapplications.client.views.cells.AppRatingCell;
import org.iplantc.core.uiapplications.client.views.cells.AppRunCell;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class AppColumnModel extends ColumnModel<App> {


    public AppColumnModel() {
        super(createColumnConfigList(EventBus.getInstance(), I18N.DISPLAY,
                Services.USER_APP_SERVICE));
    }

    public static List<ColumnConfig<App, ?>> createColumnConfigList(final EventBus eventBus,
            final CommonAppDisplayStrings displayStrings, AppUserServiceFacade templateService) {
        AppProperties props = GWT.create(AppProperties.class);
        List<ColumnConfig<App, ?>> list = new ArrayList<ColumnConfig<App, ?>>();

        ColumnConfig<App, App> run = new ColumnConfig<App, App>(
                new IdentityValueProvider<App>(), 20);

        ColumnConfig<App, App> name = new ColumnConfig<App, App>(
                new IdentityValueProvider<App>(), 180, I18N.DISPLAY.name());
        ColumnConfig<App, String> integrator = new ColumnConfig<App, String>(
                props.integratorName(), 130, I18N.DISPLAY.integratedby());
        ColumnConfig<App, App> rating = new ColumnConfig<App, App>(
                new IdentityValueProvider<App>(), 80, "Rating"); //$NON-NLS-1$

        run.setResizable(false);
        name.setResizable(true);
        rating.setResizable(false);

        run.setCell(new AppRunCell());
        name.setCell(new AppHyperlinkCell());
        rating.setCell(new AppRatingCell());

        rating.setAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        list.add(run);
        list.add(name);
        list.add(integrator);
        list.add(rating);
        return list;
    }

}


